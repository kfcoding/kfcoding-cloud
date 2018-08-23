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

import feign.Feign;
import feign.InvocationHandlerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * RestTemplateConfiguration.
 * @author maple
 */
@Configuration
public class RestTemplateConfiguration {

    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder().requestInterceptor(new RestTemplateInterceptor())
                .invocationHandlerFactory(invocationHandlerFactory());
    }

    @Bean
    public InvocationHandlerFactory invocationHandlerFactory() {
        return (target, dispatch) -> {
            FeignHandler handler = new FeignHandler();
            handler.setTarget(target);
            handler.setHandlers(dispatch);
            return handler;
        };
    }

}
