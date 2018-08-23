package com.cuiyun.kfcoding.basic.biz;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cuiyun.kfcoding.basic.dao.BookMapper;
import com.cuiyun.kfcoding.basic.enums.BookStatusEnum;
import com.cuiyun.kfcoding.basic.exception.BizExceptionEnum;
import com.cuiyun.kfcoding.basic.model.Book;
import com.cuiyun.kfcoding.basic.model.BookTag;
import com.cuiyun.kfcoding.basic.model.BookToBookTag;
import com.cuiyun.kfcoding.common.base.biz.BaseBiz;
import com.cuiyun.kfcoding.common.exception.KfCodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: kfcoding-cloud
 * @description: 书籍服务层
 * @author: maple
 * @create: 2018-08-13 15:35
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class BookBiz extends BaseBiz<BookMapper, Book>{

    @Autowired
    BookToBookTagBiz bookToBookTagBiz;

    @Override
    public boolean insert(Book entity) {
        entity.setUserId(getCurrentUserId());
        entity.setAuthor(getCurrentUserName());
        List<BookTag> tags = entity.getBookTags();
        baseMapper.insert(entity);
        List<BookToBookTag> bookToBookTags = setBookToBookTag(tags, entity.getId());
        return bookToBookTagBiz.insertBatch(bookToBookTags);
    }

    @Override
    public Page<Book> selectPage(Page<Book> page) {
        Map condition = null;
        if (page.getCondition() != null) {
            condition= page.getCondition();
            if (!condition.containsKey("status")){
                condition.put("status", BookStatusEnum.PUBLIC.getValue());
            }
        } else {
            condition = new HashMap();
            condition.put("status", BookStatusEnum.PUBLIC.getValue());
        }

        page.setTotal(baseMapper.selectPublicPageCount(condition));
        page.setRecords(baseMapper.selectPublicList(page, condition));
        return page;
    }

    private List<BookToBookTag> setBookToBookTag(List<BookTag> tags, String bookId){
        List<BookToBookTag> bookToBookTags = new ArrayList<>();
        BookToBookTag bookToBookTag;
        for (BookTag tag: tags) {
            bookToBookTag = new BookToBookTag();
            bookToBookTag.setBookId(bookId);
            bookToBookTag.setTagId(tag.getId());
            bookToBookTags.add(bookToBookTag);
        }
        return bookToBookTags;
    }

    @Override
    public boolean updateById(Book entity) {
        // 删除这个课程和tag的对应关系
        EntityWrapper ew = new EntityWrapper<>();
        ew.eq("kongfu_id", entity.getId());
        bookToBookTagBiz.delete(ew);

        List<BookTag> tags = entity.getBookTags();
        Book targetBook = baseMapper.selectById(entity.getId());
        BeanUtil.copyProperties(entity, targetBook, entity.getIgnoreProperties());
        if (baseMapper.updateById(targetBook) == 0) {
            throw new KfCodingException(BizExceptionEnum.BOOK_UPDATE);
        }
        List<BookToBookTag> bookToBookTags = setBookToBookTag(tags, entity.getId());
        return bookToBookTagBiz.insertBatch(bookToBookTags);
    }

    /**
     *  根据用户Id获取当前书籍
     */
    public List<Book> getBooksByUserId(String userId){
        return this.baseMapper.selectList(new EntityWrapper<Book>().eq("user_id", userId));
    }

}
