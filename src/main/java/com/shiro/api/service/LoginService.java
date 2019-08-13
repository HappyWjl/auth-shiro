package com.shiro.api.service;

import com.shiro.api.model.TbMenu;
import com.shiro.api.model.TbUser;
import org.apache.shiro.authz.SimpleAuthorizationInfo;

import java.util.List;
import java.util.Map;

/**
 * TbUserService
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
public interface LoginService {

    /**
     * 根据用户名称查询用户信息
     *
     * @param userName 用户名称
     * @return 用户对象
     */
    TbUser getUserByUserName(String userName);

    /**
     * 根据用户名称查询授权信息
     *
     * @param userName 用户名称
     * @return 授权信息
     */
    SimpleAuthorizationInfo getRolesAndPermissionsByUserName(String userName);

    /**
     * 校验token
     *
     * @param sToken
     * @param textStr
     * @return
     */
    boolean checkCodeToken(String sToken, String textStr);

    /**
     *
     * @return
     * @throws Exception
     */
    Map<String, Object> generateVerificationCode() throws Exception;

    /**
     *
     * @return
     */
    List<TbUser> listOnLineUser();

    /**
     *
     * @return
     */
    boolean removeSessionBySessionId(String sessionId);

    /**
     *
     * @return
     */
    boolean forbiddenByUserName(String userName);

    /**
     * 根据用户名称，查询菜单权限
     *
     * @param userName 用户名称
     * @return 菜单权限集合
     */
    List<TbMenu> getMenuByUserName(String userName);
}
