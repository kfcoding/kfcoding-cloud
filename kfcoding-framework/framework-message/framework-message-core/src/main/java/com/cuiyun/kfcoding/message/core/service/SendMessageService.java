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

import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;

/**
 * SendMessageService.
 * @author maple(Myth)
 */
public interface SendMessageService {

    /**
     * send message.
     * @param transaction {@linkplain Transaction}
     * @return true
     */
    Boolean sendMessage(Transaction transaction);
}
