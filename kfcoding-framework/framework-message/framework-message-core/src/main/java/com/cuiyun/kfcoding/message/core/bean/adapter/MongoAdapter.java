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

package com.cuiyun.kfcoding.message.core.bean.adapter;

import cn.hutool.core.lang.ObjectId;
import lombok.Data;

import java.io.Serializable;

/**
 * MongoAdapter.
 * @author maple
 */
@Data
public class MongoAdapter extends CoordinatorRepositoryAdapter implements Serializable {

    private static final long serialVersionUID = 7920817865031921102L;

    private ObjectId id;


}
