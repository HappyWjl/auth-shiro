package com.shiro.api.service;

import com.shiro.api.model.TbRole;

import java.util.List;

/**
 * TbUserService
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
public interface TbRoleService {

    /**
     * 根据角色id集合查询角色集合
     *
     * @param roleIds 角色id集合
     * @return 角色集合
     */
    List<TbRole> getByIds(List<Long> roleIds);
}
