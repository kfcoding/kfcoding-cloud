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

package com.cuiyun.kfcoding.framwork.message.springcloud.feign;

import com.cuiyun.kfcoding.message.core.annotation.KfcodingMessaging;
import com.cuiyun.kfcoding.message.core.bean.context.TransactionContext;
import com.cuiyun.kfcoding.message.core.bean.entity.Invocation;
import com.cuiyun.kfcoding.message.core.bean.entity.Participant;
import com.cuiyun.kfcoding.message.core.concurrent.threadlocal.TransactionContextLocal;
import com.cuiyun.kfcoding.message.core.helper.SpringBeanUtils;
import com.cuiyun.kfcoding.message.core.service.engine.TransactionEngine;
import feign.InvocationHandlerFactory.MethodHandler;
import feign.Target;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;


/**
 * FeignHandler.
 * @author maple
 */
public class FeignHandler implements InvocationHandler {

    private Target<?> target;

    private Map<Method, MethodHandler> handlers;

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else {
            final KfcodingMessaging kfcodingMessaging = method.getAnnotation(KfcodingMessaging.class);
            if (Objects.isNull(kfcodingMessaging)) {
                return this.handlers.get(method).invoke(args);
            }
            try {
                final TransactionEngine mythTransactionEngine =
                        SpringBeanUtils.getInstance().getBean(TransactionEngine.class);
                final Participant participant = buildParticipant(kfcodingMessaging, method, args);
                if (Objects.nonNull(participant)) {
                    mythTransactionEngine.registerParticipant(participant);
                }
                return this.handlers.get(method).invoke(args);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return null;
            }
        }
    }

    private Participant buildParticipant(final KfcodingMessaging kfcodingMessaging, final Method method, final Object[] args) {
        final TransactionContext transactionContext = TransactionContextLocal.getInstance().get();

        Participant participant;
        if (Objects.nonNull(transactionContext)) {
            final Class declaringClass = kfcodingMessaging.target();
            Invocation mythInvocation =
                    new Invocation(declaringClass, method.getName(), method.getParameterTypes(), args);
            final Integer pattern = kfcodingMessaging.pattern().getCode();
            //封装调用点
            participant = new Participant(transactionContext.getTransId(),
                    kfcodingMessaging.destination(),
                    pattern,
                    mythInvocation);
            return participant;
        }
        return null;
    }

    public void setTarget(final Target<?> target) {
        this.target = target;
    }

    public void setHandlers(final Map<Method, MethodHandler> handlers) {
        this.handlers = handlers;
    }

}
