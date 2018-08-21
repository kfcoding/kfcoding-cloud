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
import cn.hutool.core.util.StrUtil;
import com.cuiyun.kfcoding.message.core.bean.adapter.CoordinatorRepositoryAdapter;
import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;
import com.cuiyun.kfcoding.message.core.config.AutoConfig;
import com.cuiyun.kfcoding.message.core.config.ZookeeperConfig;
import com.cuiyun.kfcoding.message.core.constant.CommonConstant;
import com.cuiyun.kfcoding.message.core.enums.RepositorySupportEnum;
import com.cuiyun.kfcoding.message.core.enums.StatusEnum;
import com.cuiyun.kfcoding.message.core.serializer.ObjectSerializer;
import com.cuiyun.kfcoding.message.core.spi.CoordinatorRepository;
import com.cuiyun.kfcoding.message.core.util.RepositoryConvertUtils;
import com.cuiyun.kfcoding.message.core.util.RepositoryPathUtils;
import com.google.common.collect.Lists;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * use zookeeper save transaction log.
 *
 * @author maple
 */
public class ZookeeperCoordinatorRepository implements CoordinatorRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperCoordinatorRepository.class);

    private static volatile ZooKeeper zooKeeper;

    private static final CountDownLatch LATCH = new CountDownLatch(1);

    private ObjectSerializer objectSerializer;

    private String rootPathPrefix = "/myth";

    @Override
    public int create(final Transaction transaction) {
        try {
            zooKeeper.create(buildRootPath(transaction.getTransId()),
                    RepositoryConvertUtils.convert(transaction, objectSerializer),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            return CommonConstant.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonConstant.ERROR;
        }
    }

    @Override
    public int remove(final String transId) {
        try {
            zooKeeper.delete(buildRootPath(transId), -1);
            return CommonConstant.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonConstant.ERROR;
        }
    }

    @Override
    public int update(final Transaction transaction) throws RuntimeException {
        try {
            transaction.setLastTime(new Date());
            transaction.setVersion(transaction.getVersion() + 1);
            zooKeeper.setData(buildRootPath(transaction.getTransId()),
                    RepositoryConvertUtils.convert(transaction, objectSerializer), -1);
            return CommonConstant.SUCCESS;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateFailTransaction(final Transaction transaction) throws RuntimeException {
        update(transaction);
    }

    @Override
    public void updateParticipant(final Transaction transaction) throws RuntimeException {
        final String path = RepositoryPathUtils.buildZookeeperRootPath(rootPathPrefix, transaction.getTransId());
        try {
            byte[] content = zooKeeper.getData(path, false, new Stat());
            if (content != null) {
                final CoordinatorRepositoryAdapter adapter =
                        objectSerializer.deSerialize(content, CoordinatorRepositoryAdapter.class);
                adapter.setContents(objectSerializer.serialize(transaction.getParticipants()));
                zooKeeper.create(path, objectSerializer.serialize(adapter),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateStatus(final String id, final Integer status) throws RuntimeException {
        final String path = RepositoryPathUtils.buildZookeeperRootPath(rootPathPrefix, id);
        try {
            byte[] content = zooKeeper.getData(path, false, new Stat());
            if (content != null) {
                final CoordinatorRepositoryAdapter adapter =
                        objectSerializer.deSerialize(content, CoordinatorRepositoryAdapter.class);
                adapter.setStatus(status);
                zooKeeper.create(path,
                        objectSerializer.serialize(adapter),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return CommonConstant.SUCCESS;
    }

    @Override
    public Transaction findByTransId(final String transId) {
        try {
            Stat stat = new Stat();
            byte[] content = zooKeeper.getData(buildRootPath(transId), false, stat);
            return RepositoryConvertUtils.transformBean(content, objectSerializer);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
        List<Transaction> transactionRecovers = Lists.newArrayList();
        List<String> zNodePaths;
        try {
            zNodePaths = zooKeeper.getChildren(rootPathPrefix, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (ArrayUtil.isNotEmpty(zNodePaths)) {
            transactionRecovers = zNodePaths.stream()
                    .filter(StrUtil::isNotBlank)
                    .map(zNodePath -> {
                        try {
                            byte[] content = zooKeeper.getData(buildRootPath(zNodePath), false, new Stat());
                            return RepositoryConvertUtils.transformBean(content, objectSerializer);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList());
        }
        return transactionRecovers;
    }

    @Override
    public void init(final String modelName, final AutoConfig autoConfig) {
        rootPathPrefix = RepositoryPathUtils.buildZookeeperPathPrefix(modelName);
        connect(autoConfig.getZookeeperConfig());
    }

    private void connect(final ZookeeperConfig config) {
        try {
            zooKeeper = new ZooKeeper(config.getHost(), config.getSessionTimeOut(), watchedEvent -> {
                if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    // 放开闸门, wait在connect方法上的线程将被唤醒
                    LATCH.countDown();
                }
            });
            LATCH.await();
            Stat stat = zooKeeper.exists(rootPathPrefix, false);
            if (stat == null) {
                zooKeeper.create(rootPathPrefix, rootPathPrefix.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            LOGGER.error("zookeeper init error please check you config!:{}", e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getScheme() {
        return RepositorySupportEnum.ZOOKEEPER.getSupport();
    }

    @Override
    public void setSerializer(final ObjectSerializer objectSerializer) {
        this.objectSerializer = objectSerializer;
    }

    private String buildRootPath(final String id) {
        return RepositoryPathUtils.buildZookeeperRootPath(rootPathPrefix, id);
    }
}
