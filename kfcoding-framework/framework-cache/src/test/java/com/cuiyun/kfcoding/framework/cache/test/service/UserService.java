package com.cuiyun.kfcoding.framework.cache.test.service;


import com.cuiyun.kfcoding.framework.cache.test.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by bifenglin.
 */
public interface UserService {
    public User get(String account);

    public List<User> getLlist();

    public Set<User> getSet();

    public Map<String, User> getMap();

    public void save(User user);

    public User get(int age);
}
