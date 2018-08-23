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

package com.cuiyun.kfcoding.framwork.message.springcloud.interceptor;

import com.alibaba.fastjson.JSON;
import com.cuiyun.kfcoding.message.core.bean.context.TransactionContext;
import com.cuiyun.kfcoding.message.core.concurrent.threadlocal.TransactionContextLocal;
import com.cuiyun.kfcoding.message.core.constant.CommonConstant;
import com.cuiyun.kfcoding.message.core.enums.RoleEnum;
import com.cuiyun.kfcoding.message.core.interceptor.TransactionInterceptor;
import com.cuiyun.kfcoding.message.core.service.TransactionAspectService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * SpringCloudTransactionInterceptor.
 * @author maple
 */
@Component
public class SpringCloudTransactionInterceptor implements TransactionInterceptor {

    private final TransactionAspectService transactionAspectService;

    @Autowired
    public SpringCloudTransactionInterceptor(final TransactionAspectService transactionAspectService) {
        this.transactionAspectService = transactionAspectService;
    }

    @Override
    public Object interceptor(final ProceedingJoinPoint pjp) throws Throwable {
        TransactionContext transactionContext = TransactionContextLocal.getInstance().get();
        if (Objects.nonNull(transactionContext)
                && transactionContext.getRole() == RoleEnum.LOCAL.getCode()) {
            transactionContext = TransactionContextLocal.getInstance().get();
        } else {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = requestAttributes == null ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
            String context = request == null ? null : request.getHeader(CommonConstant.KFCODING_TRANSACTION_CONTEXT);
            if (StringUtils.isNoneBlank(context)) {
                transactionContext = JSON.parseObject(context, TransactionContext.class);
            }
        }
        return transactionAspectService.invoke(transactionContext, pjp);
    }

}
