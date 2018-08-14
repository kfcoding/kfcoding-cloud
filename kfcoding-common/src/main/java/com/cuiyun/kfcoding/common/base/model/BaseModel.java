package com.cuiyun.kfcoding.common.base.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.Version;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: kfcoding-cloud
 * @description: model基础类
 * @author: maple
 * @create: 2018-08-02 14:03
 **/
@Data
public abstract class BaseModel<T extends BaseModel> extends Model<T> {
    @TableId(value = "id", type = IdType.UUID)
    protected String id;

    @TableField(value = "create_time")
    protected Date createTime;

    @TableField(value = "update_time", update = "now()")
    protected Date updateTime;

    @TableField(value = "delete_time")
    @JSONField(serialize = false)
    protected Date deleteTime;

    @Version
    @JSONField(serialize=false)
    protected Long version = 1L;

    @TableLogic
    @TableField("is_del")
    @JSONField(serialize=false)
    protected Integer isDel = 0;

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @JSONField(serialize = false)
    public String[] getIgnoreProperties(){
        return new String[]{"id", "version", "startTime", "updateTime", "isDel"};
    }
}
