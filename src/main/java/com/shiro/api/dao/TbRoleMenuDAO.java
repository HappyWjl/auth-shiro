package com.shiro.api.dao;

import com.shiro.api.model.TbRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色-菜单关联表dao
 *
 * Created by Happy王佳乐 on 2019/8/02.
 */
public interface TbRoleMenuDAO {

    /**
     * 根据角色id集合查询角色-菜单关联信息
     *
     * @param roleIds 角色id集合
     * @return 角色-菜单关联信息
     */
    List<TbRoleMenu> getByRoleIds(@Param("roleIds") List<Long> roleIds);
}
