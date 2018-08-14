package com.cuiyun.kfcoding.basic.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.cuiyun.kfcoding.common.base.model.BaseModel;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author maple123
 * @since 2018-06-07
 */
@TableName("book_tag")
@Data
public class BookTag extends BaseModel<BookTag> {

    private static final long serialVersionUID = 1L;
    private String name;
}
