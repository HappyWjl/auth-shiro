package com.shiro.api.service;

import com.shiro.api.model.TbMenu;

import java.util.List;

/**
 * TbMenuService
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
public interface TbMenuService {

    /**
     * 根据角色id集合查询角色集合
     *
     * @param roleIds 角色id集合
     * @return 菜单集合
     */
    List<TbMenu> getByIds(List<Long> roleIds);
}
