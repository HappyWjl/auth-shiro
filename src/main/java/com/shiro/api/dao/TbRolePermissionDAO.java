package com.shiro.api.dao;

import com.shiro.api.model.TbRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色-权限关联表dao
 *
 * Created by Happy王佳乐 on 2019/8/02.
 */
public interface TbRolePermissionDAO {

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
    List<TbRolePermission> getByRoleIds(@Param("roleIds") List<Long> roleIds);
}
