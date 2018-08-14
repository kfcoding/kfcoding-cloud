package com.cuiyun.kfcoding.basic.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.cuiyun.kfcoding.common.base.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 书籍和标签映射表
 * </p>
 *
 * @author maple
 * @since 2018-08-13
 */
@TableName("basic_book_to_book_tag")
@Data
public class BookToBookTag extends BaseModel<BookToBookTag> {

    private static final long serialVersionUID = 1L;
    @TableField("book_id")
    private String bookId;
    @TableField("tag_id")
    private String tagId;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
