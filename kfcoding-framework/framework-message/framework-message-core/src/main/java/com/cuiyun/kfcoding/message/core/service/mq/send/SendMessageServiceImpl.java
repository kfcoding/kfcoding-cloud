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

package com.cuiyun.kfcoding.message.core.service.mq.send;

import cn.hutool.core.util.ArrayUtil;
import com.cuiyun.kfcoding.message.core.bean.entity.Participant;
import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;
import com.cuiyun.kfcoding.message.core.bean.mq.MessageEntity;
import com.cuiyun.kfcoding.message.core.disruptor.publisher.TransactionEventPublisher;
import com.cuiyun.kfcoding.message.core.enums.EventTypeEnum;
import com.cuiyun.kfcoding.message.core.enums.StatusEnum;
import com.cuiyun.kfcoding.message.core.helper.SpringBeanUtils;
import com.cuiyun.kfcoding.message.core.serializer.ObjectSerializer;
import com.cuiyun.kfcoding.message.core.service.MqSendService;
import com.cuiyun.kfcoding.message.core.service.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * SendMessageServiceImpl.
 * @author maple(Message)
 */
@Service("sendMessageService")
public class SendMessageServiceImpl implements SendMessageService {

    private volatile ObjectSerializer serializer;

    private volatile MqSendService mqSendService;

    @Autowired
    private TransactionEventPublisher publisher;

    @Override
    public Boolean sendMessage(final Transaction transaction) {
        if (Objects.isNull(transaction)) {
            return false;
        }
        final List<Participant> participants = transaction.getParticipants();
        /*
         * 这里的这个判断很重要，不为空，表示本地的方法执行成功，需要执行远端的rpc方法
         * 为什么呢，因为我会在切面的finally里面发送消息，意思是切面无论如何都需要发送mq消息
         * 那么考虑问题，如果本地执行成功，调用rpc的时候才需要发
         * 如果本地异常，则不需要发送mq ，此时participants为空
         */
        if (ArrayUtil.isNotEmpty(participants)) {
            for (Participant participant : participants) {
                MessageEntity messageEntity = new MessageEntity(participant.getTransId(), participant.getInvocation());
                try {
                    final byte[] message = getObjectSerializer().serialize(messageEntity);
                    getMqSendService().sendMessage(participant.getDestination(), participant.getPattern(), message);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Boolean.FALSE;
                }
            }
            //这里为什么要这么做呢？ 主要是为了防止在极端情况下，发起者执行过程中，突然自身down 机
            //造成消息未发送，新增一个状态标记，如果出现这种情况，通过定时任务发送消息
            transaction.setStatus(StatusEnum.COMMIT.getCode());
            publisher.publishEvent(transaction, EventTypeEnum.UPDATE_STATUS.getCode());
        }
        return Boolean.TRUE;
    }

    private synchronized MqSendService getMqSendService() {
        if (mqSendService == null) {
            synchronized (SendMessageServiceImpl.class) {
                if (mqSendService == null) {
                    mqSendService = SpringBeanUtils.getInstance().getBean(MqSendService.class);
                }
            }
        }
        return mqSendService;
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
