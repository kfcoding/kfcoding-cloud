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

package com.cuiyun.kfcoding.message.core.spi.repository;

import cn.hutool.core.util.ArrayUtil;
import com.cuiyun.kfcoding.message.core.bean.adapter.MongoAdapter;
import com.cuiyun.kfcoding.message.core.bean.entity.Participant;
import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;
import com.cuiyun.kfcoding.message.core.config.AutoConfig;
import com.cuiyun.kfcoding.message.core.config.MongoConfig;
import com.cuiyun.kfcoding.message.core.constant.CommonConstant;
import com.cuiyun.kfcoding.message.core.enums.RepositorySupportEnum;
import com.cuiyun.kfcoding.message.core.enums.StatusEnum;
import com.cuiyun.kfcoding.message.core.exception.MessageException;
import com.cuiyun.kfcoding.message.core.exception.MessageRuntimeException;
import com.cuiyun.kfcoding.message.core.serializer.ObjectSerializer;
import com.cuiyun.kfcoding.message.core.spi.CoordinatorRepository;
import com.cuiyun.kfcoding.message.core.util.RepositoryPathUtils;
import com.google.common.base.Splitter;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * use mongo save transaction log.
 * @author maple
 */
public class MongoCoordinatorRepository implements CoordinatorRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoCoordinatorRepository.class);

    private static final String ERROR = "mongo update exception!";

    private ObjectSerializer objectSerializer;

    private MongoTemplate template;

    private String collectionName;

    @Override
    public int create(final Transaction transaction) {
        try {
            MongoAdapter mongoBean = new MongoAdapter();
            mongoBean.setTransId(transaction.getTransId());
            mongoBean.setCreateTime(transaction.getCreateTime());
            mongoBean.setLastTime(transaction.getLastTime());
            mongoBean.setRetriedCount(transaction.getRetriedCount());
            mongoBean.setStatus(transaction.getStatus());
            mongoBean.setRole(transaction.getRole());
            mongoBean.setTargetClass(transaction.getTargetClass());
            mongoBean.setTargetMethod(transaction.getTargetMethod());
            final byte[] cache = objectSerializer.serialize(transaction.getParticipants());
            mongoBean.setContents(cache);
            mongoBean.setErrorMsg(transaction.getErrorMsg());
            template.save(mongoBean, collectionName);
            return CommonConstant.SUCCESS;
        } catch (MessageException e) {
            e.printStackTrace();
            return CommonConstant.ERROR;
        }
    }

    @Override
    public int remove(final String transId) {
        Query query = new Query();
        query.addCriteria(new Criteria("transId").is(transId));
        template.remove(query, collectionName);
        return CommonConstant.SUCCESS;
    }

    @Override
    public int update(final Transaction transaction) throws MessageRuntimeException {
        Query query = new Query();
        query.addCriteria(new Criteria("transId").is(transaction.getTransId()));
        Update update = new Update();
        update.set("lastTime", new Date());
        update.set("retriedCount", transaction.getRetriedCount() + 1);
        update.set("version", transaction.getVersion() + 1);
        try {
            if (ArrayUtil.isNotEmpty(transaction.getParticipants())) {
                update.set("contents", objectSerializer.serialize(transaction.getParticipants()));
            }
        } catch (MessageException e) {
            e.printStackTrace();
        }
        final UpdateResult writeResult = template.updateFirst(query, update, MongoAdapter.class, collectionName);
        if (writeResult.getModifiedCount() <= 0) {
            throw new MessageRuntimeException(ERROR);
        }
        return CommonConstant.SUCCESS;
    }

    @Override
    public void updateFailTransaction(final Transaction transaction) throws MessageRuntimeException {
        Query query = new Query();
        query.addCriteria(new Criteria("transId").is(transaction.getTransId()));
        Update update = new Update();
        update.set("status", transaction.getStatus());
        update.set("errorMsg", transaction.getErrorMsg());
        update.set("lastTime", new Date());
        update.set("retriedCount", transaction.getRetriedCount());
        final UpdateResult writeResult = template.updateFirst(query, update, MongoAdapter.class, collectionName);
        if (writeResult.getModifiedCount() <= 0) {
            throw new MessageRuntimeException(ERROR);
        }
    }

    @Override
    public void updateParticipant(final Transaction transaction) throws MessageRuntimeException {
        Query query = new Query();
        query.addCriteria(new Criteria("transId").is(transaction.getTransId()));
        Update update = new Update();
        try {
            update.set("contents", objectSerializer.serialize(transaction.getParticipants()));
        } catch (MessageException e) {
            e.printStackTrace();
        }
        final UpdateResult writeResult = template.updateFirst(query, update, MongoAdapter.class, collectionName);
        if (writeResult.getModifiedCount() <= 0) {
            throw new MessageRuntimeException(ERROR);
        }
    }

    @Override
    public int updateStatus(final String id, final Integer status) throws MessageRuntimeException {
        Query query = new Query();
        query.addCriteria(new Criteria("transId").is(id));
        Update update = new Update();
        update.set("status", status);
        final UpdateResult writeResult = template.updateFirst(query, update, MongoAdapter.class, collectionName);
        if (writeResult.getModifiedCount() <= 0) {
            throw new MessageRuntimeException(ERROR);
        }
        return CommonConstant.SUCCESS;
    }

    @Override
    public Transaction findByTransId(final String transId) {
        Query query = new Query();
        query.addCriteria(new Criteria("transId").is(transId));
        MongoAdapter cache = template.findOne(query, MongoAdapter.class, collectionName);
        return buildByCache(cache);

    }

    @Override
    public List<Transaction> listAllByDelay(final Date date) {
        Query query = new Query();
        query.addCriteria(Criteria.where("lastTime").lt(date))
                .addCriteria(Criteria.where("status").is(StatusEnum.BEGIN.getCode()));
        final List<MongoAdapter> mongoBeans = template.find(query, MongoAdapter.class, collectionName);
        if (ArrayUtil.isNotEmpty(mongoBeans)) {
            return mongoBeans.stream().map(this::buildByCache).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public void init(final String modelName, final AutoConfig mythConfig) {
        collectionName = RepositoryPathUtils.buildMongoTableName(modelName);
        final MongoConfig tccMongoConfig = mythConfig.getMongoConfig();
        MongoClientFactoryBean clientFactoryBean = buildMongoClientFactoryBean(tccMongoConfig);
        try {
            clientFactoryBean.afterPropertiesSet();
            template = new MongoTemplate(clientFactoryBean.getObject(), tccMongoConfig.getMongoDbName());
        } catch (Exception e) {
            throw new MessageRuntimeException(e);
        }
    }

    private MongoClientFactoryBean buildMongoClientFactoryBean(final MongoConfig mongoConfig) {
        MongoClientFactoryBean clientFactoryBean = new MongoClientFactoryBean();
        MongoCredential credential = MongoCredential.createScramSha1Credential(mongoConfig.getMongoUserName(),
                mongoConfig.getMongoDbName(),
                mongoConfig.getMongoUserPwd().toCharArray());
        clientFactoryBean.setCredentials(new MongoCredential[]{credential});
        List<String> urls = Splitter.on(",").trimResults().splitToList(mongoConfig.getMongoDbUrl());
        final ServerAddress[] sds = urls.stream().map(url -> {
            List<String> adds = Splitter.on(":").trimResults().splitToList(url);
            InetSocketAddress address = new InetSocketAddress(adds.get(0), Integer.parseInt(adds.get(1)));
            return new ServerAddress(address);
        }).collect(Collectors.toList()).toArray(new ServerAddress[]{});
        clientFactoryBean.setReplicaSetSeeds(sds);
        return clientFactoryBean;
    }

    @Override
    public String getScheme() {
        return RepositorySupportEnum.MONGODB.getSupport();
    }

    @Override
    public void setSerializer(final ObjectSerializer objectSerializer) {
        this.objectSerializer = objectSerializer;
    }

    @SuppressWarnings("unchecked")
    private Transaction buildByCache(final MongoAdapter cache) {
        Transaction transaction = new Transaction();
        transaction.setTransId(cache.getTransId());
        transaction.setCreateTime(cache.getCreateTime());
        transaction.setLastTime(cache.getLastTime());
        transaction.setRetriedCount(cache.getRetriedCount());
        transaction.setVersion(cache.getVersion());
        transaction.setStatus(cache.getStatus());
        transaction.setRole(cache.getRole());
        transaction.setTargetClass(cache.getTargetClass());
        transaction.setTargetMethod(cache.getTargetMethod());
        try {
            List<Participant> participants = (List<Participant>) objectSerializer.deSerialize(cache.getContents(), CopyOnWriteArrayList.class);
            transaction.setParticipants(participants);
        } catch (MessageException e) {
            LOGGER.error("mongodb 反序列化异常:{}", e.getMessage());
        }
        return transaction;
    }
}
