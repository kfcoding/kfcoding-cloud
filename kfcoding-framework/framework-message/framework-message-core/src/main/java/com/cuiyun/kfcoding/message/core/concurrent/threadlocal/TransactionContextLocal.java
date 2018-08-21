package com.cuiyun.kfcoding.message.core.concurrent.threadlocal;

import com.cuiyun.kfcoding.message.core.bean.context.TransactionContext;

/**
 * @program: kfcoding-cloud
 * @description:
 * @author: maple
 * @create: 2018-08-21 13:59
 **/
public class TransactionContextLocal {
    private static final ThreadLocal<TransactionContext> CURRENT_LOCAL = new ThreadLocal<>();

    private static final TransactionContextLocal TRANSACTION_CONTEXT_LOCAL = new TransactionContextLocal();

    private TransactionContextLocal() {
    }

    public static TransactionContextLocal getInstance() {
        return TRANSACTION_CONTEXT_LOCAL;
    }

    public void set(final TransactionContext context) {
        CURRENT_LOCAL.set(context);
    }

    public TransactionContext get() {
        return CURRENT_LOCAL.get();
    }

    public void remove() {
        CURRENT_LOCAL.remove();
    }
}
