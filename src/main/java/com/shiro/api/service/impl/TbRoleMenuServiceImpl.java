package com.shiro.api.service.impl;

import com.shiro.api.dao.TbRoleMenuDAO;
import com.shiro.api.model.TbRoleMenu;
import com.shiro.api.service.TbRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TbRoleMenuServiceImpl
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
@Service("tbRoleMenuService")
public class TbRoleMenuServiceImpl implements TbRoleMenuService {

    @Autowired
    TbRoleMenuDAO tbRoleMenuDAO;

    @Override
    public List<TbRoleMenu> getByRoleIds(List<Long> roleIds) {
        return tbRoleMenuDAO.getByRoleIds(roleIds);
    }
}
