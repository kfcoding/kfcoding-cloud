/*
 *
 * Copyright 2017-2018 549477611@qq.com(xiaoyu)
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

package com.cuiyun.kfcoding.framework.message.rabbit;

import com.cuiyun.kfcoding.message.core.service.MqSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * RabbitmqSendServiceImpl.
 * @author xiaoyu(Myth)
 */
public class RabbitmqSendServiceImpl implements MqSendService, RabbitTemplate.ConfirmCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitmqSendServiceImpl.class);

    private AmqpTemplate amqpTemplate;

    public void setAmqpTemplate(final AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public void sendMessage(final String destination, final Integer pattern, final byte[] message) {
        amqpTemplate.convertAndSend(destination, message);
    }



    /**
     * Confirmation callback.
      * 消息的回调，主要是实现RabbitTemplate.ConfirmCallback接口
     * 注意，消息回调只能代表成功消息发送到RabbitMQ服务器，不能代表消息被成功处理和接受
     * @param correlationData correlation data for the callback.
     * @param ack             true for ack, false for nack
     * @param cause           An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(final CorrelationData correlationData, final boolean ack, final String cause) {
        if (ack) {
            LOGGER.info("rabbit mq send message success！");
        } else {
            LOGGER.info("rabbit mq send message fail！" + cause + " retry send!");

        }
    }
}
