package com.shiro.api.service.impl;

import com.shiro.api.dao.TbRoleDAO;
import com.shiro.api.model.TbRole;
import com.shiro.api.service.TbRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TbUserRoleServiceImpl
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
@Service("tbRoleService")
public class TbRoleServiceImpl implements TbRoleService {

    @Autowired
    TbRoleDAO tbRoleDAO;

    @Override
    public List<TbRole> getByIds(List<Long> roleIds) {
        return tbRoleDAO.getByIds(roleIds);
    }
}
