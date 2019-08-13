package com.shiro.api.service.impl;

import com.shiro.api.dao.TbMenuDAO;
import com.shiro.api.model.TbMenu;
import com.shiro.api.service.TbMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TbMenuServiceImpl
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
@Service("tbMenuService")
public class TbMenuServiceImpl implements TbMenuService {

    @Autowired
    TbMenuDAO tbMenuDAO;

    @Override
    public List<TbMenu> getByIds(List<Long> roleIds) {
        return tbMenuDAO.getByIds(roleIds);
    }
}
