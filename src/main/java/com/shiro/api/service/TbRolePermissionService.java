package com.shiro.api.service;

import com.shiro.api.model.TbRolePermission;

import java.util.List;

/**
 * TbUserService
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
public interface TbRolePermissionService {

    /**
     * 根据角色id查询角色-权限关联信息
     *
     * @param roleId 角色id
     * @return 角色-权限关联信息
     */
    List<TbRolePermission> getByRoleId(Long roleId);

    /**
     * 根据角色id集合查询角色-权限关联信息
     *
     * @param roleIds 角色id集合
     * @return 角色-权限关联信息
     */
    List<TbRolePermission> getByRoleIds(List<Long> roleIds);
}
