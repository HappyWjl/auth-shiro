package com.shiro.api.dao;

import com.shiro.api.model.TbUserRole;

import java.util.List;

/**
 * 用户-角色关联表dao
 *
 * Created by Happy王佳乐 on 2019/8/02.
 */
public interface TbUserRoleDAO {

    /**
     * 根据用户id查询用户-角色关联信息
     *
     * @param userId 用户id
     * @return 用户-角色关联信息
     */
    List<TbUserRole> getByUserId(Long userId);
}
