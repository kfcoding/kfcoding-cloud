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

package com.cuiyun.kfcoding.message.core.disruptor.handler;

import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;
import com.cuiyun.kfcoding.message.core.coordinator.CoordinatorService;
import com.cuiyun.kfcoding.message.core.disruptor.event.TransactionEvent;
import com.cuiyun.kfcoding.message.core.enums.EventTypeEnum;
import com.lmax.disruptor.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TransactionEventHandler.
 * @author maple(Message)
 */
@Component
public class TransactionEventHandler implements EventHandler<TransactionEvent> {

    private final CoordinatorService coordinatorService;

    @Autowired
    public TransactionEventHandler(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    @Override
    public void onEvent(final TransactionEvent transactionEvent, final long sequence, final boolean endOfBatch) {
        if (transactionEvent.getType() == EventTypeEnum.SAVE.getCode()) {
            coordinatorService.save(transactionEvent.getTransaction());
        } else if (transactionEvent.getType() == EventTypeEnum.UPDATE_PARTICIPANT.getCode()) {
            coordinatorService.updateParticipant(transactionEvent.getTransaction());
        } else if (transactionEvent.getType() == EventTypeEnum.UPDATE_STATUS.getCode()) {
            final Transaction transaction = transactionEvent.getTransaction();
            coordinatorService.updateStatus(transaction.getTransId(), transaction.getStatus());
        } else if (transactionEvent.getType() == EventTypeEnum.UPDATE_FAIR.getCode()) {
            coordinatorService.updateFailTransaction(transactionEvent.getTransaction());
        }
        transactionEvent.clear();
    }
}
