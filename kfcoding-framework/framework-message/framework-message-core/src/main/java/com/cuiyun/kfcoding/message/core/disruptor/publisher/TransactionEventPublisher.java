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

package com.cuiyun.kfcoding.message.core.disruptor.publisher;

import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;
import com.cuiyun.kfcoding.message.core.concurrent.threadpool.TransactionThreadFactory;
import com.cuiyun.kfcoding.message.core.disruptor.event.TransactionEvent;
import com.cuiyun.kfcoding.message.core.disruptor.factory.TransactionEventFactory;
import com.cuiyun.kfcoding.message.core.disruptor.handler.TransactionEventHandler;
import com.cuiyun.kfcoding.message.core.disruptor.translator.TransactionEventTranslator;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TransactionEventPublisher.
 *
 * @author maple(Kfcoding)
 */
@Slf4j
@Component
public class TransactionEventPublisher implements DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionEventPublisher.class);

    private static final int MAX_THREAD = Runtime.getRuntime().availableProcessors() << 1;

    private Executor executor;

    private Disruptor<TransactionEvent> disruptor;

    private final TransactionEventHandler transactionEventHandler;

    @Autowired
    public TransactionEventPublisher(TransactionEventHandler transactionEventHandler) {
        this.transactionEventHandler = transactionEventHandler;
    }

    /**
     * start disruptor.
     *
     * @param bufferSize bufferSize
     */
    public void start(final int bufferSize) {

        disruptor = new Disruptor<>(new TransactionEventFactory(), bufferSize, r -> {
            AtomicInteger index = new AtomicInteger(1);
            return new Thread(null, r, "disruptor-thread-" + index.getAndIncrement());
        }, ProducerType.MULTI, new BlockingWaitStrategy());

        executor = new ThreadPoolExecutor(MAX_THREAD, MAX_THREAD, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                TransactionThreadFactory.create("kfcoding-log-disruptor", false),
                new ThreadPoolExecutor.AbortPolicy());
        disruptor.handleEventsWith(transactionEventHandler);
        disruptor.setDefaultExceptionHandler(new ExceptionHandler<TransactionEvent>() {
            @Override
            public void handleEventException(Throwable ex, long sequence, TransactionEvent event) {
                log.error("Disruptor handleEventException:"
                        + event.getType() + event.getTransaction().toString() + ex.getMessage());
            }

            @Override
            public void handleOnStartException(Throwable ex) {
                log.error("Disruptor start exception");
            }

            @Override
            public void handleOnShutdownException(Throwable ex) {
                log.error("Disruptor close Exception ");
            }
        });

        disruptor.start();
    }


    /**
     * publish disruptor event.
     *
     * @param Transaction {@linkplain Transaction }
     * @param type            {@linkplain EventTypeEnum}
     */
    public void publishEvent(final Transaction transaction, final int type) {
        executor.execute(() -> {
            final RingBuffer<TransactionEvent> ringBuffer = disruptor.getRingBuffer();
            ringBuffer.publishEvent(new TransactionEventTranslator(type), transaction);
        });

    }

    @Override
    public void destroy() {
        disruptor.shutdown();
    }
}
