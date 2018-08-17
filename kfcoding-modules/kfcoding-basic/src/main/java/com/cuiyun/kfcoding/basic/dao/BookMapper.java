package com.cuiyun.kfcoding.basic.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cuiyun.kfcoding.basic.model.Book;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author maple
 * @since 2018-08-13
 */
public interface BookMapper extends BaseMapper<Book> {

    List<Book> selectPublicList(Pagination pagination, @Param("condition") Map condition);

    Long selectPublicPageCount (@Param("condition") Map condition);
}
