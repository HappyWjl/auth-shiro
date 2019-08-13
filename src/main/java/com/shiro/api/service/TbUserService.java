package com.shiro.api.service;

import com.shiro.api.model.TbUser;

/**
 * TbUserService
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
public interface TbUserService {

    /**
     * 根据用户名称查询用户信息
     *
     * @param userName 用户名称
     * @return 用户对象
     */
    TbUser getByUserName(String userName);

    /**
     * 根据用户名称禁用账号
     *
     * @param userName 用户名称
     */
    void forbiddenByUserName(String userName);
}
