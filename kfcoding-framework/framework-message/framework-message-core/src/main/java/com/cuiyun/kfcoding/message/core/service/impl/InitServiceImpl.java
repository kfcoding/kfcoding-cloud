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

package com.cuiyun.kfcoding.message.core.service.impl;

import com.cuiyun.kfcoding.message.core.config.AutoConfig;
import com.cuiyun.kfcoding.message.core.coordinator.CoordinatorService;
import com.cuiyun.kfcoding.message.core.disruptor.publisher.TransactionEventPublisher;
import com.cuiyun.kfcoding.message.core.enums.RepositorySupportEnum;
import com.cuiyun.kfcoding.message.core.enums.SerializeEnum;
import com.cuiyun.kfcoding.message.core.helper.SpringBeanUtils;
import com.cuiyun.kfcoding.message.core.schedule.ScheduledService;
import com.cuiyun.kfcoding.message.core.serializer.KryoSerializer;
import com.cuiyun.kfcoding.message.core.serializer.ObjectSerializer;
import com.cuiyun.kfcoding.message.core.service.InitService;
import com.cuiyun.kfcoding.message.core.spi.CoordinatorRepository;
import com.cuiyun.kfcoding.message.core.spi.repository.JdbcCoordinatorRepository;
import com.cuiyun.kfcoding.message.core.util.ServiceBootstrap;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * myth init.
 * @author maple(Message)
 */
@Slf4j
@Service
public class InitServiceImpl implements InitService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InitServiceImpl.class);

    private final CoordinatorService coordinatorService;

    private final TransactionEventPublisher publisher;

    private final ScheduledService scheduledService;

    @Autowired
    public InitServiceImpl(final CoordinatorService coordinatorService,
                           final TransactionEventPublisher publisher,
                           final ScheduledService scheduledService) {
        this.coordinatorService = coordinatorService;
        this.publisher = publisher;
        this.scheduledService = scheduledService;
    }

    @Override
    public void initialization(final AutoConfig autoConfig) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> LOGGER.error("message have error!")));
        try {
            loadSpiSupport(autoConfig);
            publisher.start(autoConfig.getBufferSize());
            coordinatorService.start(autoConfig);
            //如果需要自动恢复 开启线程 调度线程池，进行恢复
            if (autoConfig.getNeedRecover()) {
                scheduledService.scheduledAutoRecover(autoConfig);
            }
        } catch (Exception ex) {
            log.error("message init fail:{}", ex.getMessage());
            //非正常关闭
            System.exit(1);
        }
        log.info("message init success");
    }

    /**
     * load spi support.
     *
     * @param autoConfig {@linkplain AutoConfig}
     */
    private void loadSpiSupport(final AutoConfig autoConfig) {
        //spi  serialize
        final SerializeEnum serializeEnum = SerializeEnum.acquire(autoConfig.getSerializer());
        final ServiceLoader<ObjectSerializer> objectSerializers = ServiceBootstrap.loadAll(ObjectSerializer.class);
        final ObjectSerializer serializer =
                StreamSupport.stream(objectSerializers.spliterator(),
                        true)
                        .filter(objectSerializer -> Objects.equals(objectSerializer.getScheme(), serializeEnum.getSerialize()))
                        .findFirst()
                        .orElse(new KryoSerializer());
        coordinatorService.setSerializer(serializer);
        SpringBeanUtils.getInstance().registerBean(ObjectSerializer.class.getName(), serializer);
        //spi  repository support
        final RepositorySupportEnum repositorySupportEnum = RepositorySupportEnum.acquire(autoConfig.getRepositorySupport());
        final ServiceLoader<CoordinatorRepository> recoverRepositories = ServiceBootstrap.loadAll(CoordinatorRepository.class);
        final CoordinatorRepository repository =
                StreamSupport.stream(recoverRepositories.spliterator(), false)
                        .filter(recoverRepository -> Objects.equals(recoverRepository.getScheme(), repositorySupportEnum.getSupport()))
                        .findFirst()
                        .orElse(new JdbcCoordinatorRepository());
        //将CoordinatorRepository实现注入到spring容器
        repository.setSerializer(serializer);
        SpringBeanUtils.getInstance().registerBean(CoordinatorRepository.class.getName(), repository);
    }

}
