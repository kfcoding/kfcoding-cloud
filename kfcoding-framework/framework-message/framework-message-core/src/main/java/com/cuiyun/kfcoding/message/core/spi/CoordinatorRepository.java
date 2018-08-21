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

package com.cuiyun.kfcoding.message.core.spi;

import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;
import com.cuiyun.kfcoding.message.core.config.AutoConfig;
import com.cuiyun.kfcoding.message.core.exception.MessageRuntimeException;
import com.cuiyun.kfcoding.message.core.serializer.ObjectSerializer;

import java.util.Date;
import java.util.List;

/**
 * CoordinatorRepository.
 * @author maple
 */
public interface CoordinatorRepository {

    /**
     * create transaction.
     *
     * @param transaction {@linkplain Transaction}
     * @return Influence row number
     */
    int create(Transaction transaction);

    /**
     * delete transaction.
     *
     * @param transId pk
     * @return Influence row number
     */
    int remove(String transId);


    /**
     * update transaction. {@linkplain Transaction}
     *
     * @param transaction 事务对象
     * @return Influence row number
     * @throws MessageRuntimeException  ex {@linkplain MessageRuntimeException}
     */
    int update(Transaction transaction) throws MessageRuntimeException;


    /**
     * update fail info in transaction.
     * @param transaction {@linkplain Transaction}
     * @throws MessageRuntimeException  ex {@linkplain MessageRuntimeException}
     */
    void updateFailTransaction(Transaction transaction) throws MessageRuntimeException;


    /**
     * update participants in transaction.
     * this have only update this participant filed.
     * @param transaction {@linkplain Transaction}
     * @throws MessageRuntimeException ex {@linkplain MessageRuntimeException}
     */
    void updateParticipant(Transaction transaction) throws MessageRuntimeException;


    /**
     * update status in transaction.
     *
     * @param transId pk
     * @param status  {@linkplain com.github.myth.common.enums.MythStatusEnum}
     * @return Influence row number
     * @throws MessageRuntimeException ex {@linkplain MessageRuntimeException}
     */
    int updateStatus(String transId, Integer status) throws MessageRuntimeException;

    /**
     * find transaction by transId.
     *
     * @param transId pk
     * @return {@linkplain Transaction}
     */
    Transaction findByTransId(String transId);


    /**
     * list all transaction by delay date.
     *
     * @param date delay date
     * @return list transaction
     */
    List<Transaction> listAllByDelay(Date date);


    /**
     * init CoordinatorRepository.
     *
     * @param modelName  model name
     * @param autoConfig {@linkplain AutoConfig}
     * @throws MessageRuntimeException ex {@linkplain MessageRuntimeException}
     */
    void init(String modelName, AutoConfig autoConfig) throws MessageRuntimeException;

    /**
     *  get scheme.
     *
     * @return scheme
     */
    String getScheme();


    /**
     * set objectSerializer.
     *
     * @param objectSerializer {@linkplain ObjectSerializer}
     */
    void setSerializer(ObjectSerializer objectSerializer);
}
