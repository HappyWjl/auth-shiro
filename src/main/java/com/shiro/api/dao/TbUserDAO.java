package com.shiro.api.dao;

import com.shiro.api.model.TbUser;

/**
 * 用户表dao
 *
 * Created by Happy王佳乐 on 2019/8/02.
 */
public interface TbUserDAO {

    /**
     * 根据用户名称，查询用户信息
     *
     * @param userName 用户名称
     * @return 用户信息
     */
    TbUser getByUserName(String userName);

    /**
     * 根据用户名称，禁用账号
     *
     * @param userName 用户名称
     */
    void forbiddenByUserName(String userName);
}
