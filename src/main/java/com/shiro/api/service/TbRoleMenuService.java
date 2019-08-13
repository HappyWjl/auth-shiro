package com.shiro.api.service;

import com.shiro.api.model.TbRoleMenu;

import java.util.List;

/**
 * TbRoleMenuService
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
public interface TbRoleMenuService {

    /**
     * 根据角色id集合查询角色-菜单关联信息
     *
     * @param roleIds 角色id集合
     * @return 角色-菜单关联信息
     */
    List<TbRoleMenu> getByRoleIds(List<Long> roleIds);
}
