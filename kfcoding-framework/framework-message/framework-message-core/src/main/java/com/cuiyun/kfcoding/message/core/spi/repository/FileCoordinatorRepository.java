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

import cn.hutool.core.io.FileUtil;
import com.cuiyun.kfcoding.message.core.bean.adapter.CoordinatorRepositoryAdapter;
import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;
import com.cuiyun.kfcoding.message.core.config.AutoConfig;
import com.cuiyun.kfcoding.message.core.constant.CommonConstant;
import com.cuiyun.kfcoding.message.core.enums.RepositorySupportEnum;
import com.cuiyun.kfcoding.message.core.enums.StatusEnum;
import com.cuiyun.kfcoding.message.core.exception.MessageException;
import com.cuiyun.kfcoding.message.core.exception.MessageRuntimeException;
import com.cuiyun.kfcoding.message.core.serializer.ObjectSerializer;
import com.cuiyun.kfcoding.message.core.spi.CoordinatorRepository;
import com.cuiyun.kfcoding.message.core.util.RepositoryConvertUtils;
import com.cuiyun.kfcoding.message.core.util.RepositoryPathUtils;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * use file save transaction log.
 * @author maple
 */
@SuppressWarnings("unchecked")
public class FileCoordinatorRepository implements CoordinatorRepository {

    private String filePath;

    private ObjectSerializer serializer;

    private AtomicBoolean initialized = new AtomicBoolean(false);

    @Override
    public void setSerializer(final ObjectSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public int create(final Transaction transaction) {
        writeFile(transaction);
        return CommonConstant.SUCCESS;
    }

    @Override
    public int remove(final String id) {
        String fullFileName = RepositoryPathUtils.getFullFileName(filePath, id);
        File file = new File(fullFileName);
        return file.exists() && file.delete() ? CommonConstant.SUCCESS : CommonConstant.ERROR;
    }

    @Override
    public int update(final Transaction transaction) throws MessageRuntimeException {
        transaction.setLastTime(new Date());
        transaction.setVersion(transaction.getVersion() + 1);
        transaction.setRetriedCount(transaction.getRetriedCount() + 1);
        writeFile(transaction);
        return CommonConstant.SUCCESS;
    }

    @Override
    public void updateFailTransaction(final Transaction transaction) throws MessageRuntimeException {
        try {
            final String fullFileName = RepositoryPathUtils.getFullFileName(filePath, transaction.getTransId());
            transaction.setLastTime(new Date());
            FileUtil.writeBytes(RepositoryConvertUtils.convert(transaction, serializer), fullFileName);
        } catch (MessageException e) {
            throw new MessageRuntimeException("update exception！");
        }
    }

    @Override
    public void updateParticipant(final Transaction transaction) throws MessageRuntimeException {
        try {
            final String fullFileName = RepositoryPathUtils.getFullFileName(filePath, transaction.getTransId());
            final File file = new File(fullFileName);
            final CoordinatorRepositoryAdapter adapter = readAdapter(file);
            if (Objects.nonNull(adapter)) {
                adapter.setContents(serializer.serialize(transaction.getParticipants()));
            }
            FileUtil.writeBytes(serializer.serialize(adapter), fullFileName);
        } catch (Exception e) {
            throw new MessageRuntimeException("update exception！");
        }

    }

    @Override
    public int updateStatus(final String id, final Integer status) throws MessageRuntimeException {
        try {
            final String fullFileName = RepositoryPathUtils.getFullFileName(filePath, id);
            final File file = new File(fullFileName);

            final CoordinatorRepositoryAdapter adapter = readAdapter(file);
            if (Objects.nonNull(adapter)) {
                adapter.setStatus(status);
            }
            FileUtil.writeBytes(serializer.serialize(adapter), fullFileName);
            return CommonConstant.SUCCESS;
        } catch (Exception e) {
            throw new MessageRuntimeException("更新数据异常！");
        }

    }

    @Override
    public Transaction findByTransId(final String transId) {
        String fullFileName = RepositoryPathUtils.getFullFileName(filePath, transId);
        File file = new File(fullFileName);
        try {
            return readTransaction(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Transaction> listAllByDelay(final Date date) {
        final List<Transaction> transactionList = listAll();
        return transactionList.stream()
                .filter(tccTransaction -> tccTransaction.getLastTime().compareTo(date) < 0)
                .filter(transaction -> transaction.getStatus() == StatusEnum.BEGIN.getCode())
                .collect(Collectors.toList());
    }

    private List<Transaction> listAll() {
        List<Transaction> transactionRecoverList = Lists.newArrayList();
        File path = new File(filePath);
        File[] files = path.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                try {
                    Transaction transaction = readTransaction(file);
                    transactionRecoverList.add(transaction);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return transactionRecoverList;
    }

    @Override
    public void init(final String modelName, final AutoConfig autoConfig) {
        filePath = RepositoryPathUtils.buildFilePath(modelName);
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.mkdirs();
        }
    }

    @Override
    public String getScheme() {
        return RepositorySupportEnum.FILE.getSupport();
    }

    private Transaction readTransaction(final File file) throws Exception {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] content = new byte[(int) file.length()];
            fis.read(content);
            return RepositoryConvertUtils.transformBean(content, serializer);
        }
    }

    private CoordinatorRepositoryAdapter readAdapter(final File file) throws Exception {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] content = new byte[(int) file.length()];
            fis.read(content);
            return serializer.deSerialize(content, CoordinatorRepositoryAdapter.class);
        }
    }

    private void writeFile(final Transaction transaction) throws MessageRuntimeException {
        for (; ;) {
            if (makeDirIfNecessary()) {
                break;
            }
        }
        try {
            String fileName = RepositoryPathUtils.getFullFileName(filePath, transaction.getTransId());
            FileUtil.writeBytes(RepositoryConvertUtils.convert(transaction, serializer), fileName);
        } catch (Exception e) {
            throw new MessageRuntimeException("fail to write transaction to local storage", e);
        }
    }

    private boolean makeDirIfNecessary() throws MessageRuntimeException {
        if (!initialized.getAndSet(true)) {
            File rootDir = new File(filePath);
            boolean isExist = rootDir.exists();
            if (!isExist) {
                if (rootDir.mkdir()) {
                    return true;
                } else {
                    throw new MessageRuntimeException(String.format("fail to make root directory, path:%s.", filePath));
                }
            } else {
                if (rootDir.isDirectory()) {
                    return true;
                } else {
                    throw new MessageRuntimeException(String.format("the root path is not a directory, please check again, path:%s.", filePath));
                }
            }
        }
        return false;
    }
}
