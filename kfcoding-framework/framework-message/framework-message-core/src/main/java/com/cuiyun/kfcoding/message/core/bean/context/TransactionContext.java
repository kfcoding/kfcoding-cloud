package com.cuiyun.kfcoding.message.core.bean.context;

import lombok.Data;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-21 14:11
 **/
@Data
public class TransactionContext {

    private static final long serialVersionUID = -5289080166922118073L;

    private String transId;

    /**
     * 事务参与的角色. {@linkplain com.cuiyun.kfcoding.message.core.enums.RoleEnum}
     */
    private int role;
}
