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

package com.cuiyun.kfcoding.message.core.coordinator;

import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;
import com.cuiyun.kfcoding.message.core.config.AutoConfig;
import com.cuiyun.kfcoding.message.core.exception.MessageException;
import com.cuiyun.kfcoding.message.core.exception.MessageRuntimeException;
import com.cuiyun.kfcoding.message.core.serializer.ObjectSerializer;

import java.util.Date;
import java.util.List;

/**
 * CoordinatorService.
 * @author maple
 */
public interface CoordinatorService {

    /**
     * start coordinator service.
     *
     * @param autoConfig {@linkplain AutoConfig}
     * @throws MessageException ex
     */
    void start(AutoConfig autoConfig) throws MessageException;

    /**
     * save Transaction.
     *
     * @param transaction {@linkplain Transaction}
     * @return pk
     */
    String save(Transaction transaction);

    /**
     * find Transaction by id.
     *
     * @param transId pk
     * @return {@linkplain Transaction}
     */
    Transaction findByTransId(String transId);


    /**
     * find  Transaction by Delay Date.
     *
     * @param date delay date
     * @return {@linkplain Transaction}
     */
    List<Transaction> listAllByDelay(Date date);

    /**
     * delete Transaction.
     *
     * @param transId pk
     * @return true  false
     */
    boolean remove(String transId);

    /**
     * update  Transaction.
     *
     * @param transaction {@linkplain Transaction}
     * @return rows 1
     * @throws MessageRuntimeException ex
     */
    int update(Transaction transaction) throws MessageRuntimeException;


    /**
     * update fail info.
     * @param transaction {@linkplain Transaction}
     * @throws MessageRuntimeException ex
     */
    void updateFailTransaction(Transaction transaction) throws MessageRuntimeException;

    /**
     * update Participant.
     * @param transaction {@linkplain Transaction}
     * @throws MessageRuntimeException ex
     */
    void updateParticipant(Transaction transaction) throws MessageRuntimeException;

    /**
     * update status.
     * @param transId pk
     * @param status  status
     * @return rows 1
     * @throws MessageRuntimeException ex
     */
    int updateStatus(String transId, Integer status) throws MessageRuntimeException;

    /**
     * set ObjectSerializer.
     * @param serializer {@linkplain ObjectSerializer}
     */
    void setSerializer(ObjectSerializer serializer);

}
