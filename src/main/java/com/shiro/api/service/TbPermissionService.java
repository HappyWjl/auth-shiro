package com.shiro.api.service;

import com.shiro.api.model.TbPermission;

import java.util.List;

/**
 * TbUserService
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
public interface TbPermissionService {

    /**
     * 根据权限id集合，查询权限信息集合
     *
     * @param permissionIds 权限id集合
     * @return 权限信息集合
     */
    List<TbPermission> getByPermissionIds(List<Long> permissionIds);
}
