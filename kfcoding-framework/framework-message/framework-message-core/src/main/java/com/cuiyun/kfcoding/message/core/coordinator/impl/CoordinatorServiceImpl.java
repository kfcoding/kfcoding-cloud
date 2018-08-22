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

package com.cuiyun.kfcoding.message.core.coordinator.impl;

import cn.hutool.core.util.StrUtil;
import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;
import com.cuiyun.kfcoding.message.core.config.AutoConfig;
import com.cuiyun.kfcoding.message.core.coordinator.CoordinatorService;
import com.cuiyun.kfcoding.message.core.exception.MessageRuntimeException;
import com.cuiyun.kfcoding.message.core.helper.SpringBeanUtils;
import com.cuiyun.kfcoding.message.core.serializer.ObjectSerializer;
import com.cuiyun.kfcoding.message.core.service.RpcApplicationService;
import com.cuiyun.kfcoding.message.core.spi.CoordinatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * CoordinatorServiceImpl.
 * @author maple
 */
@Service("coordinatorService")
public class CoordinatorServiceImpl implements CoordinatorService {

    private CoordinatorRepository coordinatorRepository;

    private final RpcApplicationService rpcApplicationService;

    @Autowired
    public CoordinatorServiceImpl(final RpcApplicationService rpcApplicationService) {
        this.rpcApplicationService = rpcApplicationService;
    }

    @Override
    public void setSerializer(final ObjectSerializer serializer) {
    }

    @Override
    public void start(final AutoConfig autoConfig) {
        coordinatorRepository = SpringBeanUtils.getInstance().getBean(CoordinatorRepository.class);
        final String repositorySuffix = buildRepositorySuffix(autoConfig.getRepositorySuffix());
        //初始化spi 协调资源存储
        coordinatorRepository.init(repositorySuffix, autoConfig);
    }

    @Override
    public String save(final Transaction transaction) {
        final int rows = coordinatorRepository.create(transaction);
        if (rows > 0) {
            return transaction.getTransId();
        }
        return null;
    }

    @Override
    public Transaction findByTransId(final String transId) {
        return coordinatorRepository.findByTransId(transId);
    }

    @Override
    public List<Transaction> listAllByDelay(final Date date) {
        return coordinatorRepository.listAllByDelay(date);
    }

    @Override
    public boolean remove(final String transId) {
        return coordinatorRepository.remove(transId) > 0;
    }

    @Override
    public int update(final Transaction transaction) throws MessageRuntimeException {
        return coordinatorRepository.update(transaction);
    }

    @Override
    public void updateFailTransaction(final Transaction transaction) throws MessageRuntimeException {
        coordinatorRepository.updateFailTransaction(transaction);
    }

    @Override
    public void updateParticipant(final Transaction transaction) throws MessageRuntimeException {
        coordinatorRepository.updateParticipant(transaction);
    }

    @Override
    public int updateStatus(final String transId, final Integer status) {
        return coordinatorRepository.updateStatus(transId, status);
    }

    private String buildRepositorySuffix(final String repositorySuffix) {
        if (StrUtil.isNotBlank(repositorySuffix)) {
            return repositorySuffix;
        } else {
            return rpcApplicationService.acquireName();
        }
    }

}
