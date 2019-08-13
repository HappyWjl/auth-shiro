package com.shiro.api.service.impl;

import com.shiro.api.dao.TbUserDAO;
import com.shiro.api.model.TbUser;
import com.shiro.api.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TbUserServiceImpl
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
@Service("tbUserService")
public class TbUserServiceImpl implements TbUserService {

    @Autowired
    private TbUserDAO userDAO;

    @Override
    public TbUser getByUserName(String userName) {
        return userDAO.getByUserName(userName);
    }

    @Override
    public void forbiddenByUserName(String userName) {
        userDAO.forbiddenByUserName(userName);
    }
}
