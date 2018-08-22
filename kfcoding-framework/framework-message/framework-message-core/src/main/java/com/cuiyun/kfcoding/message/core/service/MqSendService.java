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

package com.cuiyun.kfcoding.message.core.service;

/**
 * send mq message.
 * @author maple(Message)
 */
@FunctionalInterface
public interface MqSendService {

    /**
     * send message.
     * @param destination destination
     * @param pattern {@linkplain com.github.myth.annotation.MessageTypeEnum}
     * @param message convert MessageTransaction to byte[]
     */
    void sendMessage(String destination, Integer pattern, byte[] message);

}
