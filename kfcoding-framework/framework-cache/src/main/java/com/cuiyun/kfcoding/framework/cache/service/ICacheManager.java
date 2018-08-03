/**
 *
 */
package com.cuiyun.kfcoding.framework.cache.service;

import com.cuiyun.kfcoding.framework.cache.entity.CacheBean;
import com.cuiyun.kfcoding.framework.cache.vo.CacheTree;

import java.util.List;


/**
 * 解决问题：
 *
 * @author bifenglin
 * @version 1.0
 * @date 2018-8-3
 * @since 1.7
 */
public interface ICacheManager {
    public void removeAll();

    public void remove(String key);

    public void remove(List<CacheBean> caches);

    public void removeByPre(String pre);

    public List<CacheTree> getAll();

    public List<CacheTree> getByPre(String pre);

    public void update(String key, int hour);

    public void update(List<CacheBean> caches, int hour);

    public String get(String key);
}
