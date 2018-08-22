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

package com.cuiyun.kfcoding.message.core.service.handler;

import com.cuiyun.kfcoding.message.core.bean.context.TransactionContext;
import com.cuiyun.kfcoding.message.core.concurrent.threadlocal.TransactionContextLocal;
import com.cuiyun.kfcoding.message.core.service.TransactionHandler;
import com.cuiyun.kfcoding.message.core.service.engine.TransactionEngine;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * this myth transaction starter.
 * @author maple(Message)
 */
@Component
public class StartTransactionHandler implements TransactionHandler {

    private final TransactionEngine transactionEngine;

    @Autowired
    public StartTransactionHandler(final TransactionEngine transactionEngine) {
        this.transactionEngine = transactionEngine;
    }

    @Override
    public Object handler(final ProceedingJoinPoint point, final TransactionContext transactionContext) throws Throwable {
        try {
            transactionEngine.begin(point);
            return point.proceed();
        } catch (Throwable throwable) {
            //更新失败的日志信息
            transactionEngine.failTransaction(throwable.getMessage());
            throw throwable;
        } finally {
            //发送消息
            transactionEngine.sendMessage();
            transactionEngine.cleanThreadLocal();
            TransactionContextLocal.getInstance().remove();
        }
    }

}
