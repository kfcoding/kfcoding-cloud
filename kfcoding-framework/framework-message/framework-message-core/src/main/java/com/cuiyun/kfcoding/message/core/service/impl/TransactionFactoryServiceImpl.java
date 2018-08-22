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
import com.cuiyun.kfcoding.message.core.enums.RoleEnum;
import com.cuiyun.kfcoding.message.core.service.TransactionFactoryService;
import com.cuiyun.kfcoding.message.core.service.engine.TransactionEngine;
import com.cuiyun.kfcoding.message.core.service.handler.ActorTransactionHandler;
import com.cuiyun.kfcoding.message.core.service.handler.LocalTransactionHandler;
import com.cuiyun.kfcoding.message.core.service.handler.StartTransactionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * TransactionFactoryServiceImpl.
 * @author maple(Message)
 */
@Component
public class TransactionFactoryServiceImpl implements TransactionFactoryService {

    private final TransactionEngine transactionEngine;

    @Autowired
    public TransactionFactoryServiceImpl(final TransactionEngine transactionEngine) {
        this.transactionEngine = transactionEngine;
    }

    @Override
    public Class factoryOf(final TransactionContext context) throws Throwable {
        //如果事务还没开启或者 myth事务上下文是空， 那么应该进入发起调用
        if (!transactionEngine.isBegin() && Objects.isNull(context)) {
            return StartTransactionHandler.class;
        } else {
            if (context.getRole() == RoleEnum.LOCAL.getCode()) {
                return LocalTransactionHandler.class;
            }
            return ActorTransactionHandler.class;
        }
    }
}
