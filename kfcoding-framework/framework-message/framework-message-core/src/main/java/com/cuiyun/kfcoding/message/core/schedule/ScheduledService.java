/*
 *
 *  * Licensed to the Apache Software Foundation (ASF) under one or more
 *  * contributor license agreements.  See the NOTICE file distributed with
 *  * this work for additional information regarding copyright ownership.
 *  * The ASF licenses this file to You under the Apache License, Version 2.0
 *  * (the "License"); you may not use this file except in compliance with
 *  * the License.  You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.cuiyun.kfcoding.message.core.schedule;

import cn.hutool.core.util.ArrayUtil;
import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;
import com.cuiyun.kfcoding.message.core.concurrent.threadpool.TransactionThreadFactory;
import com.cuiyun.kfcoding.message.core.config.AutoConfig;
import com.cuiyun.kfcoding.message.core.coordinator.CoordinatorService;
import com.cuiyun.kfcoding.message.core.disruptor.publisher.TransactionEventPublisher;
import com.cuiyun.kfcoding.message.core.enums.EventTypeEnum;
import com.cuiyun.kfcoding.message.core.enums.StatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ScheduledService.
 * @author maple(Message)
 */
@Component
public class ScheduledService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledService.class);

    @Autowired
    private com.cuiyun.kfcoding.message.core.service.SendMessageService SendMessageService;

    @Autowired
    private CoordinatorService coordinatorService;

    @Autowired
    private TransactionEventPublisher publisher;

    public void scheduledAutoRecover(final AutoConfig AutoConfig) {
        new ScheduledThreadPoolExecutor(1, TransactionThreadFactory.create("MessageAutoRecoverService", true))
                .scheduleWithFixedDelay(() -> {
                    LOGGER.debug("auto recover execute delayTime:{}", AutoConfig.getScheduledDelay());
                    try {
                        final List<Transaction> TransactionList = coordinatorService.listAllByDelay(acquireData(AutoConfig));
                        if (ArrayUtil.isNotEmpty(TransactionList)) {
                            TransactionList.forEach(Transaction -> {
                                final Boolean success = SendMessageService.sendMessage(Transaction);
                                //发送成功 ，更改状态
                                if (success) {
                                    Transaction.setStatus(StatusEnum.COMMIT.getCode());
                                    publisher.publishEvent(Transaction, EventTypeEnum.UPDATE_STATUS.getCode());
                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, 30, AutoConfig.getScheduledDelay(), TimeUnit.SECONDS);

    }

    private Date acquireData(final AutoConfig AutoConfig) {
        return new Date(LocalDateTime.now().atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli() - (AutoConfig.getRecoverDelayTime() * 1000));
    }

}
