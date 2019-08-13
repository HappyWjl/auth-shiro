package com.shiro.api.service.impl;

import com.shiro.api.dao.TbUserRoleDAO;
import com.shiro.api.model.TbUserRole;
import com.shiro.api.service.TbUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TbUserRoleServiceImpl
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
@Service("tbUserRoleService")
public class TbUserRoleServiceImpl implements TbUserRoleService {

    @Autowired
    TbUserRoleDAO tbUserRoleDAO;

    @Override
    public List<TbUserRole> getByUserId(Long userId) {
        return tbUserRoleDAO.getByUserId(userId);
    }
}
