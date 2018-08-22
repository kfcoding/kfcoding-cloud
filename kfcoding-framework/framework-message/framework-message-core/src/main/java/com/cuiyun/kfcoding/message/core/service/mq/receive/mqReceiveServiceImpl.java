/*
 *
 * Copyright 2017-2018 515186469@qq.com(maple)
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.cuiyun.kfcoding.message.core.service.mq.receive;

import com.cuiyun.kfcoding.message.core.bean.context.TransactionContext;
import com.cuiyun.kfcoding.message.core.bean.entity.Invocation;
import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;
import com.cuiyun.kfcoding.message.core.bean.mq.MessageEntity;
import com.cuiyun.kfcoding.message.core.concurrent.threadlocal.TransactionContextLocal;
import com.cuiyun.kfcoding.message.core.config.AutoConfig;
import com.cuiyun.kfcoding.message.core.coordinator.CoordinatorService;
import com.cuiyun.kfcoding.message.core.disruptor.publisher.TransactionEventPublisher;
import com.cuiyun.kfcoding.message.core.enums.EventTypeEnum;
import com.cuiyun.kfcoding.message.core.enums.RoleEnum;
import com.cuiyun.kfcoding.message.core.enums.StatusEnum;
import com.cuiyun.kfcoding.message.core.exception.MessageException;
import com.cuiyun.kfcoding.message.core.exception.MessageRuntimeException;
import com.cuiyun.kfcoding.message.core.helper.SpringBeanUtils;
import com.cuiyun.kfcoding.message.core.serializer.ObjectSerializer;
import com.cuiyun.kfcoding.message.core.service.MqReceiveService;
import com.cuiyun.kfcoding.message.core.service.mq.send.SendMessageServiceImpl;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * mqReceiveServiceImpl.
 * @author maple(Message)
 */
@Service("mqReceiveService")
public class mqReceiveServiceImpl implements MqReceiveService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(mqReceiveServiceImpl.class);

    private static final Lock LOCK = new ReentrantLock();

    private volatile ObjectSerializer serializer;

    @Autowired
    private CoordinatorService coordinatorService;

    @Autowired
    private TransactionEventPublisher publisher;

    @Autowired
    private AutoConfig autoConfig;

    @Override
    public Boolean processMessage(final byte[] message) {
        try {
            MessageEntity entity;
            try {
                entity = getObjectSerializer().deSerialize(message, MessageEntity.class);
            } catch (MessageException e) {
                e.printStackTrace();
                throw new MessageRuntimeException(e.getMessage());
            }
            /*
             * 1 检查该事务有没被处理过，已经处理过的 则不处理
             * 2 发起发射调用，调用接口，进行处理
             * 3 记录本地日志
             */
            LOCK.lock();
            final String transId = entity.getTransId();
            final Transaction transaction = coordinatorService.findByTransId(transId);
            //第一次调用 也就是服务down机，或者没有调用到的时候， 通过mq执行
            if (Objects.isNull(transaction)) {
                try {
                    execute(entity);
                    //执行成功 保存成功的日志
                    final Transaction log = buildTransactionLog(transId, "",
                            StatusEnum.COMMIT.getCode(),
                            entity.getInvocation().getTargetClass().getName(),
                            entity.getInvocation().getMethodName());
                    //submit(new CoordinatorAction(CoordinatorActionEnum.SAVE, log));
                    publisher.publishEvent(log, EventTypeEnum.SAVE.getCode());
                } catch (Exception e) {
                    //执行失败保存失败的日志
                    final Transaction log = buildTransactionLog(transId, e.getMessage(),
                            StatusEnum.FAILURE.getCode(),
                            entity.getInvocation().getTargetClass().getName(),
                            entity.getInvocation().getMethodName());
                    publisher.publishEvent(log, EventTypeEnum.SAVE.getCode());
                    throw new MessageRuntimeException(e);
                } finally {
                    TransactionContextLocal.getInstance().remove();
                }
            } else {
                //如果是执行失败的话
                if (transaction.getStatus() == StatusEnum.FAILURE.getCode()) {
                    //如果超过了最大重试次数 则不执行
                    if (transaction.getRetriedCount() >= autoConfig.getRetryMax()) {
                        LOGGER.error("此事务已经超过了最大重试次数:" + autoConfig.getRetryMax()
                                + " ,执行接口为:" + entity.getInvocation().getTargetClass() + " ,方法为:"
                                + entity.getInvocation().getMethodName() + ",事务id为：" + entity.getTransId());
                        return Boolean.FALSE;
                    }
                    try {
                        execute(entity);
                        //执行成功 更新日志为成功
                        transaction.setStatus(StatusEnum.COMMIT.getCode());
                        publisher.publishEvent(transaction, EventTypeEnum.UPDATE_STATUS.getCode());

                    } catch (Throwable e) {
                        //执行失败，设置失败原因和重试次数
                        transaction.setErrorMsg(e.getCause().getMessage());
                        transaction.setRetriedCount(transaction.getRetriedCount() + 1);
                        publisher.publishEvent(transaction, EventTypeEnum.UPDATE_FAIR.getCode());
                        throw new MessageRuntimeException(e);
                    } finally {
                        TransactionContextLocal.getInstance().remove();
                    }
                }
            }

        } finally {
            LOCK.unlock();
        }
        return Boolean.TRUE;
    }

    private void execute(final MessageEntity entity) throws Exception {
        //设置事务上下文，这个类会传递给远端
        TransactionContext context = new TransactionContext();
        //设置事务id
        context.setTransId(entity.getTransId());
        //设置为发起者角色
        context.setRole(RoleEnum.LOCAL.getCode());
        TransactionContextLocal.getInstance().set(context);
        executeLocalTransaction(entity.getInvocation());
    }

    @SuppressWarnings("unchecked")
    private void executeLocalTransaction(final Invocation invocation) throws Exception {
        if (Objects.nonNull(invocation)) {
            final Class clazz = invocation.getTargetClass();
            final String method = invocation.getMethodName();
            final Object[] args = invocation.getArgs();
            final Class[] parameterTypes = invocation.getParameterTypes();
            final Object bean = SpringBeanUtils.getInstance().getBean(clazz);
            MethodUtils.invokeMethod(bean, method, args, parameterTypes);
            LOGGER.debug("message执行本地协调事务:{}", invocation.getTargetClass() + ":" + invocation.getMethodName());

        }
    }

    private Transaction buildTransactionLog(final String transId, final String errorMsg, final Integer status,
                                                final String targetClass, final String targetMethod) {
        Transaction logTransaction = new Transaction(transId);
        logTransaction.setRetriedCount(1);
        logTransaction.setStatus(status);
        logTransaction.setErrorMsg(errorMsg);
        logTransaction.setRole(RoleEnum.PROVIDER.getCode());
        logTransaction.setTargetClass(targetClass);
        logTransaction.setTargetMethod(targetMethod);
        return logTransaction;
    }

    private synchronized ObjectSerializer getObjectSerializer() {
        if (serializer == null) {
            synchronized (SendMessageServiceImpl.class) {
                if (serializer == null) {
                    serializer = SpringBeanUtils.getInstance().getBean(ObjectSerializer.class);
                }
            }
        }
        return serializer;
    }
}
