package com.shiro.api.service;

import com.shiro.api.model.TbUserRole;

import java.util.List;

/**
 * TbUserService
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
public interface TbUserRoleService {

    /**
     * 根据用户id查询用户-角色关联信息
     *
     * @param userId 用户id
     * @return 用户-角色关联信息
     */
    List<TbUserRole> getByUserId(Long userId);
}
