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

package com.cuiyun.kfcoding.message.core.interceptor;

import com.cuiyun.kfcoding.message.core.annotation.KfcodingMessaging;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * AbstractTransactionAspect.
 * @author maple
 */
@Aspect
public abstract class AbstractTransactionAspect {

    private TransactionInterceptor transactionInterceptor;

    /**
     * set TransactionInterceptor.
     * @param transactionInterceptor {@linkplain TransactionInterceptor}
     */
    protected void setTransactionInterceptor(final TransactionInterceptor transactionInterceptor) {
        this.transactionInterceptor = transactionInterceptor;
    }


    /**
     * this is point cut with {@linkplain KfcodingMessaging }.
     */
    @Pointcut("@annotation(com.cuiyun.kfcoding.message.core.annotation.KfcodingMessaging)")
    public void transactionInterceptor() {

    }

    /**
     * this is around in {@linkplain KfcodingMessaging }.
     * @param proceedingJoinPoint proceedingJoinPoint
     * @return Object
     * @throws Throwable  Throwable
     */
    @Around("transactionInterceptor()")
    public Object interceptAnnotationMethod(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return transactionInterceptor.interceptor(proceedingJoinPoint);
    }

    /**
     * spring bean Order.
     *
     * @return int
     */
    public abstract int getOrder();
}
