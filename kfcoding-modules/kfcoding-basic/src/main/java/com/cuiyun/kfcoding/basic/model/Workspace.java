package com.cuiyun.kfcoding.basic.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.cuiyun.kfcoding.common.base.model.BaseModel;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author maple
 * @since 2018-07-01
 */
@TableName("workspace_workspace")
@Data
public class Workspace extends BaseModel<Workspace> {

    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    @TableField("git_url")
    private String gitUrl;
    private String image;
    @TableField("user_id")
    private String userId;
    @TableField("container_name")
    private String containerName;
    private String release;
    private String repo;
    private String type;
    private String status;
    @TableField(exist = false)
    private Object wsaddr;

}
