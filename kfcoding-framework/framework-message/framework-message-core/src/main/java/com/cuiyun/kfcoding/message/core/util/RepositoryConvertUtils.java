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

package com.cuiyun.kfcoding.message.core.util;

import com.cuiyun.kfcoding.message.core.bean.adapter.CoordinatorRepositoryAdapter;
import com.cuiyun.kfcoding.message.core.bean.entity.Participant;
import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;
import com.cuiyun.kfcoding.message.core.exception.MessageException;
import com.cuiyun.kfcoding.message.core.serializer.ObjectSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * RepositoryConvertUtils.
 * @author xiaoyu(Myth)
 */
public class RepositoryConvertUtils {

    public static byte[] convert(final Transaction transaction, final ObjectSerializer objectSerializer) throws MessageException {
        CoordinatorRepositoryAdapter adapter = new CoordinatorRepositoryAdapter();
        adapter.setTransId(transaction.getTransId());
        adapter.setLastTime(transaction.getLastTime());
        adapter.setCreateTime(transaction.getCreateTime());
        adapter.setRetriedCount(transaction.getRetriedCount());
        adapter.setStatus(transaction.getStatus());
        adapter.setTargetClass(transaction.getTargetClass());
        adapter.setTargetMethod(transaction.getTargetMethod());
        adapter.setRole(transaction.getRole());
        adapter.setErrorMsg(transaction.getErrorMsg());
        adapter.setVersion(transaction.getVersion());
        adapter.setContents(objectSerializer.serialize(transaction.getParticipants()));
        return objectSerializer.serialize(adapter);
    }

    @SuppressWarnings("unchecked")
    public static Transaction transformBean(final byte[] contents, final ObjectSerializer objectSerializer) throws MessageException {
        Transaction transaction = new Transaction();
        final CoordinatorRepositoryAdapter adapter = objectSerializer.deSerialize(contents, CoordinatorRepositoryAdapter.class);
        List<Participant> participants = objectSerializer.deSerialize(adapter.getContents(), ArrayList.class);
        transaction.setLastTime(adapter.getLastTime());
        transaction.setRetriedCount(adapter.getRetriedCount());
        transaction.setCreateTime(adapter.getCreateTime());
        transaction.setTransId(adapter.getTransId());
        transaction.setStatus(adapter.getStatus());
        transaction.setParticipants(participants);
        transaction.setRole(adapter.getRole());
        transaction.setTargetClass(adapter.getTargetClass());
        transaction.setTargetMethod(adapter.getTargetMethod());
        transaction.setVersion(adapter.getVersion());
        return transaction;
    }
}
