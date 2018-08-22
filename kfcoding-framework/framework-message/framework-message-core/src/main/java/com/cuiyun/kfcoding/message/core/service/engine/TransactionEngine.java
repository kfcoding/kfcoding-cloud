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

package com.cuiyun.kfcoding.message.core.service.engine;

import cn.hutool.core.util.StrUtil;
import com.cuiyun.kfcoding.message.core.bean.context.TransactionContext;
import com.cuiyun.kfcoding.message.core.bean.entity.Participant;
import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;
import com.cuiyun.kfcoding.message.core.concurrent.threadlocal.TransactionContextLocal;
import com.cuiyun.kfcoding.message.core.disruptor.publisher.TransactionEventPublisher;
import com.cuiyun.kfcoding.message.core.enums.EventTypeEnum;
import com.cuiyun.kfcoding.message.core.enums.RoleEnum;
import com.cuiyun.kfcoding.message.core.enums.StatusEnum;
import com.cuiyun.kfcoding.message.core.service.SendMessageService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * TransactionEngine.
 * @author maple
 */
@Slf4j
@Component
@SuppressWarnings("unchecked")
public class TransactionEngine {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionEngine.class);

    /**
     * save Transaction in threadLocal.
     */
    private static final ThreadLocal<Transaction> CURRENT = new ThreadLocal<>();

    @Autowired
    private SendMessageService sendMessageService;

    @Autowired
    private TransactionEventPublisher publishEvent;

    /**
     * this is stater begin Transaction.
     *
     * @param point cut point.
     */
    public void begin(final ProceedingJoinPoint point) {
        log.debug("开始执行Message分布式事务！start");
        Transaction transaction = buildTransaction(point, RoleEnum.START.getCode(), StatusEnum.BEGIN.getCode(), "");
        //发布事务保存事件，异步保存
        publishEvent.publishEvent(transaction, EventTypeEnum.SAVE.getCode());
        //当前事务保存到ThreadLocal
        CURRENT.set(transaction);
        //设置tcc事务上下文，这个类会传递给远端
        TransactionContext context = new TransactionContext();
        //设置事务id
        context.setTransId(transaction.getTransId());
        //设置为发起者角色
        context.setRole(RoleEnum.START.getCode());
        TransactionContextLocal.getInstance().set(context);
    }

    /**
     * save errorMsg into Transaction .
     *
     * @param errorMsg errorMsg
     */
    public void failTransaction(final String errorMsg) {
        Transaction transaction = getCurrentTransaction();
        if (Objects.nonNull(transaction)) {
            transaction.setStatus(StatusEnum.FAILURE.getCode());
            transaction.setErrorMsg(errorMsg);
            publishEvent.publishEvent(transaction, EventTypeEnum.UPDATE_FAIR.getCode());
        }
    }

    /**
     * this is actor begin transaction.
     *
     * @param point                  cut point
     * @param transactionContext {@linkplain TransactionContext}
     */
    public void actorTransaction(final ProceedingJoinPoint point, final TransactionContext transactionContext) {
        Transaction transaction =
                buildTransaction(point, RoleEnum.PROVIDER.getCode(),
                        StatusEnum.BEGIN.getCode(), transactionContext.getTransId());
        //发布事务保存事件，异步保存
        publishEvent.publishEvent(transaction, EventTypeEnum.SAVE.getCode());
        //当前事务保存到ThreadLocal
        CURRENT.set(transaction);
        //设置提供者角色
        transactionContext.setRole(RoleEnum.PROVIDER.getCode());
        TransactionContextLocal.getInstance().set(transactionContext);
    }

    /**
     * update transaction status.
     * @param status {@linkplain StatusEnum}
     */
    public void updateStatus(final int status) {
        Transaction transaction = getCurrentTransaction();
        Optional.ofNullable(transaction)
                .map(t -> {
                    t.setStatus(status);
                    return t;
                }).ifPresent(t -> publishEvent.publishEvent(t, EventTypeEnum.UPDATE_STATUS.getCode()));
        transaction.setStatus(StatusEnum.COMMIT.getCode());
    }

    /**
     * send message.
     */
    public void sendMessage() {
        Optional.ofNullable(getCurrentTransaction()).ifPresent(c -> sendMessageService.sendMessage(c));
    }

    /**
     * transaction is begin.
     * @return true
     */
    public boolean isBegin() {
        return CURRENT.get() != null;
    }

    /**
     * help gc.
     */
    public void cleanThreadLocal() {
        CURRENT.remove();
    }

    private Transaction getCurrentTransaction() {
        return CURRENT.get();
    }

    /**
     * add participant into transaction.
     * @param participant {@linkplain Participant}
     */
    public void registerParticipant(final Participant participant) {
        final Transaction transaction = this.getCurrentTransaction();
        transaction.registerParticipant(participant);
        publishEvent.publishEvent(transaction, EventTypeEnum.UPDATE_PARTICIPANT.getCode());
    }

    private Transaction buildTransaction(final ProceedingJoinPoint point, final int role,
                                                 final int status, final String transId) {
        Transaction transaction;
        if (StrUtil.isNotBlank(transId)) {
            transaction = new Transaction(transId);
        } else {
            transaction = new Transaction();
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = point.getTarget().getClass();
        transaction.setStatus(status);
        transaction.setRole(role);
        transaction.setTargetClass(clazz.getName());
        transaction.setTargetMethod(method.getName());
        return transaction;
    }

}
