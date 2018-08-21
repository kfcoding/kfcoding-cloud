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
import com.alibaba.druid.pool.DruidDataSource;
import com.cuiyun.kfcoding.message.core.bean.entity.Participant;
import com.cuiyun.kfcoding.message.core.bean.entity.Transaction;
import com.cuiyun.kfcoding.message.core.config.AutoConfig;
import com.cuiyun.kfcoding.message.core.config.DbConfig;
import com.cuiyun.kfcoding.message.core.constant.CommonConstant;
import com.cuiyun.kfcoding.message.core.enums.RepositorySupportEnum;
import com.cuiyun.kfcoding.message.core.enums.StatusEnum;
import com.cuiyun.kfcoding.message.core.exception.MessageException;
import com.cuiyun.kfcoding.message.core.exception.MessageRuntimeException;
import com.cuiyun.kfcoding.message.core.helper.SqlHelper;
import com.cuiyun.kfcoding.message.core.serializer.ObjectSerializer;
import com.cuiyun.kfcoding.message.core.spi.CoordinatorRepository;
import com.cuiyun.kfcoding.message.core.util.RepositoryPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * use jdbc save transaction log.
 * @author maple
 */
@SuppressWarnings("unchecked")
public class JdbcCoordinatorRepository implements CoordinatorRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcCoordinatorRepository.class);

    private DruidDataSource dataSource;

    private String tableName;

    private ObjectSerializer serializer;

    @Override
    public void setSerializer(final ObjectSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public int create(final Transaction transaction) {
        StringBuilder sql = new StringBuilder()
                .append("insert into ")
                .append(tableName)
                .append("(trans_id,target_class,target_method,retried_count,create_time,last_time,version,status,invocation,role,error_msg)")
                .append(" values(?,?,?,?,?,?,?,?,?,?,?)");
        try {
            final byte[] serialize = serializer.serialize(transaction.getParticipants());
            return executeUpdate(sql.toString(),
                    transaction.getTransId(),
                    transaction.getTargetClass(),
                    transaction.getTargetMethod(),
                    transaction.getRetriedCount(),
                    transaction.getCreateTime(),
                    transaction.getLastTime(),
                    transaction.getVersion(),
                    transaction.getStatus(),
                    serialize,
                    transaction.getRole(),
                    transaction.getErrorMsg());
        } catch (MessageException e) {
            e.printStackTrace();
            return CommonConstant.ERROR;
        }
    }

    @Override
    public int remove(final String transId) {
        String sql = "delete from " + tableName + " where trans_id = ? ";
        return executeUpdate(sql, transId);
    }

    @Override
    public int update(final Transaction transaction) throws MessageRuntimeException {
        final Integer currentVersion = transaction.getVersion();
        transaction.setLastTime(new Date());
        transaction.setVersion(transaction.getVersion() + 1);
        String sql = "update " + tableName + " set last_time = ?,version =?,retried_count =?,invocation=?,status=?  where trans_id = ? and version=? ";
        try {
            final byte[] serialize = serializer.serialize(transaction.getParticipants());
            return executeUpdate(sql,
                    transaction.getLastTime(),
                    transaction.getVersion(),
                    transaction.getRetriedCount(),
                    serialize,
                    transaction.getStatus(),
                    transaction.getTransId(),
                    currentVersion);
        } catch (MessageException e) {
            throw new MessageRuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateFailTransaction(final Transaction transaction) throws MessageRuntimeException {
        String sql = "update " + tableName + " set  status=? ,error_msg=? ,retried_count =?,last_time = ?   where trans_id = ?  ";
        transaction.setLastTime(new Date());
        executeUpdate(sql, transaction.getStatus(),
                transaction.getErrorMsg(),
                transaction.getRetriedCount(),
                transaction.getLastTime(),
                transaction.getTransId());
    }

    @Override
    public void updateParticipant(final Transaction transaction) throws MessageRuntimeException {
        String sql = "update " + tableName + " set invocation=?  where trans_id = ?  ";
        try {
            final byte[] serialize = serializer.serialize(transaction.getParticipants());
            executeUpdate(sql, serialize, transaction.getTransId());
        } catch (MessageException e) {
            throw new MessageRuntimeException(e.getMessage());
        }
    }

    @Override
    public int updateStatus(final String id, final Integer status) throws MessageRuntimeException {
        String sql = "update " + tableName + " set status=?  where trans_id = ?  ";
        return executeUpdate(sql, status, id);
    }

    @Override
    public Transaction findByTransId(final String transId) {
        String selectSql = "select * from " + tableName + " where trans_id=?";
        List<Map<String, Object>> list = executeQuery(selectSql, transId);
        if (ArrayUtil.isNotEmpty(list)) {
            return list.stream().filter(Objects::nonNull)
                    .map(this::buildByResultMap).collect(Collectors.toList()).get(0);
        }
        return null;
    }

    @Override
    public List<Transaction> listAllByDelay(final Date date) {
        String sb = "select * from " + tableName + " where last_time < ?  and status = " + StatusEnum.BEGIN.getCode();
        List<Map<String, Object>> list = executeQuery(sb, date);
        if (ArrayUtil.isNotEmpty(list)) {
            return list.stream()
                    .filter(Objects::nonNull)
                    .map(this::buildByResultMap)
                    .collect(Collectors.toList());
        }
        return null;
    }

    private Transaction buildByResultMap(final Map<String, Object> map) {
        Transaction transaction = new Transaction();
        transaction.setTransId((String) map.get("trans_id"));
        transaction.setRetriedCount((Integer) map.get("retried_count"));
        transaction.setCreateTime((Date) map.get("create_time"));
        transaction.setLastTime((Date) map.get("last_time"));
        transaction.setVersion((Integer) map.get("version"));
        transaction.setStatus((Integer) map.get("status"));
        transaction.setRole((Integer) map.get("role"));
        byte[] bytes = (byte[]) map.get("invocation");
        try {
            final List<Participant> participants = serializer.deSerialize(bytes, CopyOnWriteArrayList.class);
            transaction.setParticipants(participants);
        } catch (MessageException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    @Override
    public void init(final String modelName, final AutoConfig autoConfig) {
        dataSource = new DruidDataSource();
        final DbConfig tccDbConfig = autoConfig.getDbConfig();
        dataSource.setUrl(tccDbConfig.getUrl());
        dataSource.setDriverClassName(tccDbConfig.getDriverClassName());
        dataSource.setUsername(tccDbConfig.getUsername());
        dataSource.setPassword(tccDbConfig.getPassword());
        dataSource.setInitialSize(tccDbConfig.getInitialSize());
        dataSource.setMaxActive(tccDbConfig.getMaxActive());
        dataSource.setMinIdle(tccDbConfig.getMinIdle());
        dataSource.setMaxWait(tccDbConfig.getMaxWait());
        dataSource.setValidationQuery(tccDbConfig.getValidationQuery());
        dataSource.setTestOnBorrow(tccDbConfig.getTestOnBorrow());
        dataSource.setTestOnReturn(tccDbConfig.getTestOnReturn());
        dataSource.setTestWhileIdle(tccDbConfig.getTestWhileIdle());
        dataSource.setPoolPreparedStatements(tccDbConfig.getPoolPreparedStatements());
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(tccDbConfig.getMaxPoolPreparedStatementPerConnectionSize());
        this.tableName = RepositoryPathUtils.buildDbTableName(modelName);
        executeUpdate(SqlHelper.buildCreateTableSql(tccDbConfig.getDriverClassName(), tableName));
    }

    @Override
    public String getScheme() {
        return RepositorySupportEnum.DB.getSupport();
    }

    private int executeUpdate(final String sql, final Object... params) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("executeUpdate-> " + e.getMessage());
        }
        return 0;
    }

    private List<Map<String, Object>> executeQuery(final String sql, final Object... params) {
        List<Map<String, Object>> list = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                int columnCount = md.getColumnCount();
                list = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> rowData = new HashMap<>(16);
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.put(md.getColumnName(i), rs.getObject(i));
                    }
                    list.add(rowData);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("executeQuery-> " + e.getMessage());
        }
        return list;
    }
}
