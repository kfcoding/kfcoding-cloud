package com.cuiyun.kfcoding.common.msg;

import com.baomidou.mybatisplus.plugins.Page;

/**
 * @program: kfcoding-cloud
 * @description: table类型返回结果
 * @author: maple
 * @create: 2018-08-02 17:13
 **/
public class TableResultResponse<T> extends BaseResponse  {

    Page<T> data;

//    public TableResultResponse(long total, List<T> rows) {
//        this.data = new Page<T>(total, rows);
//    }

    public TableResultResponse() {
        this.data = new Page<>();
    }

    public TableResultResponse(Page<T> data) {
        this.data = data;
    }

//    TableResultResponse<T> total(int total) {
//        this.data.setTotal(total);
//        return this;
//    }

//    TableResultResponse<T> total(List<T> rows) {
//        this.data.setRows(rows);
//        return this;
//    }

    public Page<T> getData() {
        return data;
    }

    public void setData(Page<T> data) {
        this.data = data;
    }
}
