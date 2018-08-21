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

import cn.hutool.core.util.StrUtil;
import com.cuiyun.kfcoding.message.core.bean.adapter.CoordinatorRepositoryAdapter;
import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;
import com.cuiyun.kfcoding.message.core.config.AutoConfig;
import com.cuiyun.kfcoding.message.core.config.RedisConfig;
import com.cuiyun.kfcoding.message.core.constant.CommonConstant;
import com.cuiyun.kfcoding.message.core.enums.RepositorySupportEnum;
import com.cuiyun.kfcoding.message.core.enums.StatusEnum;
import com.cuiyun.kfcoding.message.core.exception.MessageException;
import com.cuiyun.kfcoding.message.core.exception.MessageRuntimeException;
import com.cuiyun.kfcoding.message.core.jedis.JedisClient;
import com.cuiyun.kfcoding.message.core.jedis.JedisClientCluster;
import com.cuiyun.kfcoding.message.core.jedis.JedisClientSentinel;
import com.cuiyun.kfcoding.message.core.jedis.JedisClientSingle;
import com.cuiyun.kfcoding.message.core.serializer.ObjectSerializer;
import com.cuiyun.kfcoding.message.core.spi.CoordinatorRepository;
import com.cuiyun.kfcoding.message.core.util.RepositoryConvertUtils;
import com.cuiyun.kfcoding.message.core.util.RepositoryPathUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * use redis save transaction log.
 *
 * @author maple
 */
public class RedisCoordinatorRepository implements CoordinatorRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCoordinatorRepository.class);

    private ObjectSerializer objectSerializer;

    private JedisClient jedisClient;

    private String keyPrefix;

    @Override
    public int create(final Transaction transaction) {
        try {
            final String redisKey = RepositoryPathUtils.buildRedisKey(keyPrefix, transaction.getTransId());
            jedisClient.set(redisKey, RepositoryConvertUtils.convert(transaction, objectSerializer));
            return CommonConstant.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonConstant.ERROR;
        }
    }

    @Override
    public int remove(final String transId) {
        try {
            final String redisKey = RepositoryPathUtils.buildRedisKey(keyPrefix, transId);
            return jedisClient.del(redisKey).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonConstant.ERROR;
        }
    }

    @Override
    public int update(final Transaction transaction) throws MessageRuntimeException {
        try {
            final String redisKey = RepositoryPathUtils.buildRedisKey(keyPrefix, transaction.getTransId());
            transaction.setVersion(transaction.getVersion() + 1);
            transaction.setLastTime(new Date());
            transaction.setRetriedCount(transaction.getRetriedCount() + 1);
            jedisClient.set(redisKey, RepositoryConvertUtils.convert(transaction, objectSerializer));
            return CommonConstant.SUCCESS;
        } catch (Exception e) {
            throw new MessageRuntimeException(e);
        }
    }

    @Override
    public void updateFailTransaction(final Transaction transaction) throws MessageRuntimeException {
        try {
            final String redisKey = RepositoryPathUtils.buildRedisKey(keyPrefix, transaction.getTransId());
            transaction.setLastTime(new Date());
            jedisClient.set(redisKey, RepositoryConvertUtils.convert(transaction, objectSerializer));
        } catch (Exception e) {
            throw new MessageRuntimeException(e);
        }
    }

    @Override
    public void updateParticipant(final Transaction transaction) throws MessageRuntimeException {
        final String redisKey = RepositoryPathUtils.buildRedisKey(keyPrefix, transaction.getTransId());
        byte[] contents = jedisClient.get(redisKey.getBytes());
        try {
            if (contents != null) {
                CoordinatorRepositoryAdapter adapter = objectSerializer.deSerialize(contents, CoordinatorRepositoryAdapter.class);
                adapter.setContents(objectSerializer.serialize(transaction.getParticipants()));
                jedisClient.set(redisKey, objectSerializer.serialize(adapter));
            }
        }
        catch (MessageException e) {
            e.printStackTrace();
            throw new MessageRuntimeException(e);
        }
    }

    @Override
    public int updateStatus(final String id, final Integer status) throws MessageRuntimeException {
        final String redisKey = RepositoryPathUtils.buildRedisKey(keyPrefix, id);
        byte[] contents = jedisClient.get(redisKey.getBytes());
        try {
            if (contents != null) {
                CoordinatorRepositoryAdapter adapter = objectSerializer.deSerialize(contents, CoordinatorRepositoryAdapter.class);
                adapter.setStatus(status);
                jedisClient.set(redisKey, objectSerializer.serialize(adapter));
            }
        } catch (MessageException e) {
            e.printStackTrace();
            throw new MessageRuntimeException(e);
        }
        return CommonConstant.SUCCESS;
    }

    @Override
    public Transaction findByTransId(final String transId) {
        try {
            final String redisKey = RepositoryPathUtils.buildRedisKey(keyPrefix, transId);
            byte[] contents = jedisClient.get(redisKey.getBytes());
            return RepositoryConvertUtils.transformBean(contents, objectSerializer);
        } catch (Exception e) {
            throw new MessageRuntimeException(e);
        }
    }

    @Override
    public List<Transaction> listAllByDelay(final Date date) {
        final List<Transaction> transactionList = listAll();
        return transactionList.stream()
                .filter(transaction -> transaction.getLastTime().compareTo(date) > 0)
                .filter(transaction -> transaction.getStatus() == StatusEnum.BEGIN.getCode())
                .collect(Collectors.toList());
    }

    private List<Transaction> listAll() {
        try {
            List<Transaction> transactions = Lists.newArrayList();
            Set<byte[]> keys = jedisClient.keys((keyPrefix + "*").getBytes());
            for (final byte[] key : keys) {
                byte[] contents = jedisClient.get(key);
                if (contents != null) {
                    transactions.add(RepositoryConvertUtils.transformBean(contents, objectSerializer));
                }
            }
            return transactions;
        } catch (Exception e) {
            throw new MessageRuntimeException(e);
        }
    }

    @Override
    public void init(final String modelName, final AutoConfig config) {
        keyPrefix = RepositoryPathUtils.buildRedisKeyPrefix(modelName);
        final RedisConfig redisConfig = config.getRedisConfig();
        try {
            buildJedisPool(redisConfig);
        } catch (Exception e) {
            LOGGER.error("redis init error please check your config ! ex:{}", e.getMessage());
            throw new MessageRuntimeException(e);
        }
    }

    @Override
    public String getScheme() {
        return RepositorySupportEnum.REDIS.getSupport();
    }

    @Override
    public void setSerializer(final ObjectSerializer objectSerializer) {
        this.objectSerializer = objectSerializer;
    }

    private void buildJedisPool(final RedisConfig redisConfig) {
        LOGGER.debug("myth begin init redis....");
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(redisConfig.getMaxIdle());
        //最小空闲连接数, 默认0
        config.setMinIdle(redisConfig.getMinIdle());
        //最大连接数, 默认8个
        config.setMaxTotal(redisConfig.getMaxTotal());
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        config.setMaxWaitMillis(redisConfig.getMaxWaitMillis());
        //在获取连接的时候检查有效性, 默认false
        config.setTestOnBorrow(redisConfig.getTestOnBorrow());
        //返回一个jedis实例给连接池时，是否检查连接可用性（ping()）
        config.setTestOnReturn(redisConfig.getTestOnReturn());
        //在空闲时检查有效性, 默认false
        config.setTestWhileIdle(redisConfig.getTestWhileIdle());
        //逐出连接的最小空闲时间 默认1800000毫秒(30分钟 )
        config.setMinEvictableIdleTimeMillis(redisConfig.getMinEvictableIdleTimeMillis());
        //对象空闲多久后逐出, 当空闲时间>该值 ，且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)，默认30m
        config.setSoftMinEvictableIdleTimeMillis(redisConfig.getSoftMinEvictableIdleTimeMillis());
        //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        config.setTimeBetweenEvictionRunsMillis(redisConfig.getTimeBetweenEvictionRunsMillis());
        //每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        config.setNumTestsPerEvictionRun(redisConfig.getNumTestsPerEvictionRun());

        JedisPool jedisPool;
        //如果是集群模式
        if (redisConfig.getCluster()) {
            LOGGER.info("myth build redis cluster ............");
            final String clusterUrl = redisConfig.getClusterUrl();
            final Set<HostAndPort> hostAndPorts =
                    Splitter.on(";")
                            .splitToList(clusterUrl)
                            .stream()
                            .map(HostAndPort::parseString).collect(Collectors.toSet());
            JedisCluster jedisCluster = new JedisCluster(hostAndPorts, config);
            jedisClient = new JedisClientCluster(jedisCluster);
        } else if (redisConfig.getSentinel()) {
            LOGGER.info("myth build redis sentinel ............");
            final String sentinelUrl = redisConfig.getSentinelUrl();
            final Set<String> hostAndPorts =
                    new HashSet<>(Splitter.on(";")
                            .splitToList(sentinelUrl));

            JedisSentinelPool pool =
                    new JedisSentinelPool(redisConfig.getMasterName(), hostAndPorts,
                            config, redisConfig.getTimeOut(), redisConfig.getPassword());
            jedisClient = new JedisClientSentinel(pool);
        } else {
            if (StrUtil.isNotBlank(redisConfig.getPassword())) {
                jedisPool = new JedisPool(config, redisConfig.getHostName(), redisConfig.getPort(),
                        redisConfig.getTimeOut(),
                        redisConfig.getPassword());
            } else {
                jedisPool = new JedisPool(config, redisConfig.getHostName(), redisConfig.getPort(),
                        redisConfig.getTimeOut());
            }
            jedisClient = new JedisClientSingle(jedisPool);
        }
    }
}
