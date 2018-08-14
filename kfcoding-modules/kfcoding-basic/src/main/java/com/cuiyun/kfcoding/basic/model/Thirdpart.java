package com.cuiyun.kfcoding.basic.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cuiyun.kfcoding.api.vo.authority.enums.AuthTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author maple123
 * @since 2018-05-19
 */
@Data
@TableName("basic_thirdpart")
public class Thirdpart extends Model<Thirdpart> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.UUID)
    private String id;
    @TableField("user_id")
    private String userId;
    @JSONField(serialzeFeatures= SerializerFeature.WriteEnumUsingToString)
    @TableField("auth_type")
    private AuthTypeEnum authType;
    @TableField("thirdpart_id")
    private String thirdpartId;
    @TableField("gists_url")
    private String gistsUrl;
    @TableField("repos_url")
    private String reposUrl;
    @TableField("following_url")
    private String followingUrl;
    private String bio;
    @TableField("created_at")
    private Date createdAt;
    private String login;
    private String type;
    private String blog;
    @TableField("subscriptions_url")
    private String subscriptionsUrl;
    @TableField("updated_at")
    private Date updatedAt;
    @TableField("site_admin")
    private String siteAdmin;
    private String company;
    @TableField("public_repos")
    private String publicRepos;
    @TableField("gravatar_id")
    private String gravatarId;
    private String email;
    @TableField("organizations_url")
    private String organizationsUrl;
    private String hireable;
    @TableField("starred_url")
    private String starredUrl;
    @TableField("followers_url")
    private String followersUrl;
    @TableField("public_gists")
    private Integer publicGists;
    private String url;
    @TableField("access_token")
    private String accessToken;
    @TableField("received_events_url")
    private String receivedEventsUrl;
    private Integer followers;
    @TableField("avatar_url")
    private String avatarUrl;
    @TableField("events_url")
    private String eventsUrl;
    @TableField("html_url")
    private String htmlUrl;
    private Integer following;
    private String name;
    private String location;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
