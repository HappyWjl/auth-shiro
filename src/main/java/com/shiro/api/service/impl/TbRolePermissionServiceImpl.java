package com.shiro.api.service.impl;

import com.shiro.api.dao.TbRolePermissionDAO;
import com.shiro.api.model.TbRolePermission;
import com.shiro.api.service.TbRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TbUserRoleServiceImpl
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
@Service("tbRolePermissionService")
public class TbRolePermissionServiceImpl implements TbRolePermissionService {

    @Autowired
    TbRolePermissionDAO tbRolePermissionDAO;

    @Override
    public List<TbRolePermission> getByRoleId(Long roleId) {
        return tbRolePermissionDAO.getByRoleId(roleId);
    }

    @Override
    public List<TbRolePermission> getByRoleIds(List<Long> roleIds) {
        return tbRolePermissionDAO.getByRoleIds(roleIds);
    }
}
