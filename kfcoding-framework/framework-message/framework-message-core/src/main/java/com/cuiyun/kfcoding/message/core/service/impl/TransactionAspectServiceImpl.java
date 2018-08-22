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

import com.cuiyun.kfcoding.message.core.bean.context.TransactionContext;
import com.cuiyun.kfcoding.message.core.helper.SpringBeanUtils;
import com.cuiyun.kfcoding.message.core.service.TransactionAspectService;
import com.cuiyun.kfcoding.message.core.service.TransactionFactoryService;
import com.cuiyun.kfcoding.message.core.service.TransactionHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TransactionAspectServiceImpl.
 *
 * @author maple(Message)
 */
@Component
public class TransactionAspectServiceImpl implements TransactionAspectService {

    private final TransactionFactoryService transactionFactoryService;

    @Autowired
    public TransactionAspectServiceImpl(final TransactionFactoryService transactionFactoryService) {
        this.transactionFactoryService = transactionFactoryService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(final TransactionContext transactionContext, final ProceedingJoinPoint point) throws Throwable {
        final Class clazz = transactionFactoryService.factoryOf(transactionContext);
        final TransactionHandler transactionHandler = (TransactionHandler) SpringBeanUtils.getInstance().getBean(clazz);
        return transactionHandler.handler(point, transactionContext);
    }
}
