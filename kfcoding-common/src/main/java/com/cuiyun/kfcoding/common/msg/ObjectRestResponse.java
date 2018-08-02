package com.cuiyun.kfcoding.common.msg;


/**
 * @program: kfcoding-cloud
 * @description: object返回结果
 * @author: maple
 * @create: 2018-08-02 17:13
 **/
public class ObjectRestResponse<T> extends BaseResponse {

    T data;
    boolean rel;

    public boolean isRel() {
        return rel;
    }

    public void setRel(boolean rel) {
        this.rel = rel;
    }


    public ObjectRestResponse rel(boolean rel) {
        this.setRel(rel);
        return this;
    }


    public ObjectRestResponse data(T data) {
        this.setData(data);
        return this;
    }
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
