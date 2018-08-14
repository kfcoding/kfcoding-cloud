package com.cuiyun.kfcoding.basic.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.cuiyun.kfcoding.basic.enums.BookStatusEnum;
import com.cuiyun.kfcoding.common.base.model.BaseModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: kfcoding-cloud
 * @description: book实体类
 * @author: maple
 * @create: 2018-08-13 15:12
 **/
@Data
@TableName("basic_book")
public class Book extends BaseModel<Book>{
    private static final long serialVersionUID = 1L;
    private String title;
    private String author;
    private Integer type;
    private String brief;
    @TableField("user_id")
    private String userId;
    private String level;
    @TableField("surface_image")
    private String surfaceImage;
    @TableField("surface_background")
    private String surfaceBackground;
    private BookStatusEnum status = BookStatusEnum.PRIVATE;
    private Integer priority;
    @TableField(exist=false)
    private List<BookTag> bookTags = new ArrayList<>();
}
