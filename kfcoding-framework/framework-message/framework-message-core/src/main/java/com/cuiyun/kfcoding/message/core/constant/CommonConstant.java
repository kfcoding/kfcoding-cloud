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

package com.cuiyun.kfcoding.message.core.constant;

/**
 * CommonConstant.
 * @author maple
 */
public interface CommonConstant {

    String DB_MYSQL = "mysql";

    String DB_SQLSERVER = "sqlserver";

    String DB_ORACLE = "oracle";

    String PATH_SUFFIX = "/kfcoding";

    String DB_SUFFIX = "kfcoding";

    String RECOVER_REDIS_KEY_PRE = "kfcoding:transaction:%s";

    String KFCODING_TRANSACTION_CONTEXT = "KFCODING_TRANSACTION_CONTEXT";

    String TOPIC_TAG_SEPARATOR = ",";

    int SUCCESS = 1;

    int ERROR = 0;

}
