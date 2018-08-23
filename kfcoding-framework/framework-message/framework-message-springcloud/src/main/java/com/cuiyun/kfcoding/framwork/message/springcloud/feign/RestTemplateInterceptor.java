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

import com.alibaba.fastjson.JSON;
import com.cuiyun.kfcoding.message.core.bean.context.TransactionContext;
import com.cuiyun.kfcoding.message.core.concurrent.threadlocal.TransactionContextLocal;
import com.cuiyun.kfcoding.message.core.constant.CommonConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * RestTemplateInterceptor.
 * @author maple
 */
@Configuration
public class RestTemplateInterceptor implements RequestInterceptor {

    @Override
    public void apply(final RequestTemplate requestTemplate) {
        final TransactionContext transactionContext = TransactionContextLocal.getInstance().get();
        requestTemplate.header(CommonConstant.KFCODING_TRANSACTION_CONTEXT,
                JSON.toJSONString(transactionContext));
    }

}
