package com.shiro.api.service.impl;

import com.shiro.api.dao.TbPermissionDAO;
import com.shiro.api.model.TbPermission;
import com.shiro.api.service.TbPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TbUserRoleServiceImpl
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
@Service("tbPermissionService")
public class TbPermissionServiceImpl implements TbPermissionService {

    @Autowired
    TbPermissionDAO tbPermissionDAO;

    @Override
    public List<TbPermission> getByPermissionIds(List<Long> permissionIds) {
        return tbPermissionDAO.getByPermissionIds(permissionIds);
    }
}
