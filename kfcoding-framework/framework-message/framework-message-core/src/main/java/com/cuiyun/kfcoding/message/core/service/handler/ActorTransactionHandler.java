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
import com.cuiyun.kfcoding.message.core.enums.StatusEnum;
import com.cuiyun.kfcoding.message.core.service.TransactionHandler;
import com.cuiyun.kfcoding.message.core.service.engine.TransactionEngine;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * this myth transaction actor.
 * @author maple(Message)
 */
@Component
public class ActorTransactionHandler implements TransactionHandler {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ActorTransactionHandler.class);

    private final TransactionEngine transactionEngine;

    @Autowired
    public ActorTransactionHandler(final TransactionEngine transactionEngine) {
        this.transactionEngine = transactionEngine;
    }

    @Override
    public Object handler(final ProceedingJoinPoint point, final TransactionContext transactionContext) throws Throwable {
        try {
            //先保存事务日志
            transactionEngine.actorTransaction(point, transactionContext);
            //发起调用 执行try方法
            final Object proceed = point.proceed();
            //执行成功 更新状态为commit
            transactionEngine.updateStatus(StatusEnum.COMMIT.getCode());
            return proceed;
        } catch (Throwable throwable) {
            LOGGER.error("执行分布式事务接口失败,事务id：{}", transactionContext.getTransId());
            transactionEngine.failTransaction(throwable.getMessage());
            throw throwable;
        } finally {
            TransactionContextLocal.getInstance().remove();
        }
    }
}
