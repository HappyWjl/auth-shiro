package com.shiro.api.dao;

import com.shiro.api.model.TbPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限表dao
 *
 * Created by Happy王佳乐 on 2019/8/02.
 */
public interface TbPermissionDAO {

    /**
     * 根据权限id集合，查询权限信息集合
     *
     * @param permissionIds 权限id集合
     * @return 权限信息集合
     */
    List<TbPermission> getByPermissionIds(@Param("permissionIds") List<Long> permissionIds);
}
