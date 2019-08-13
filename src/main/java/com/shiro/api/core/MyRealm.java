package com.shiro.api.core;

import com.shiro.api.model.TbUser;
import com.shiro.api.service.LoginService;
import com.shiro.api.service.TbUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * 自定义域
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private TbUserService userService;

    @Autowired
    private LoginService loginService;

    /**
     * 认证，出现异常会被ControllerExceptionHandler捕获
     *
     * @param token 用户token
     * @return 认证信息
     * @throws AuthenticationException 认证异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = (String) token.getPrincipal();
        TbUser user = userService.getByUserName(userName);
        if (Objects.isNull(user)) {
            throw new UnknownAccountException("该用户名称不存在！");
        } else if (Objects.isNull(user.getForbidden()) || user.getForbidden().equals(1)) {
            throw new UnknownAccountException("该用户已经被锁定！");
        } else {
            String password = new String((char[]) token.getCredentials());
            // 校验传入的密码，是否等于数据库中的密码
            if (user.getPassword().equals(password)) {
                AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                        user.getUserName(), user.getPassword(), user.getUserName());
                // 将user对象放到session属性中
                SecurityUtils.getSubject().getSession().setAttribute("currentUser", user);
                return authenticationInfo;
            } else {
                throw new IncorrectCredentialsException("密码错误！");
            }
        }
    }

    /**
     * 授权，出现异常会被ControllerExceptionHandler捕获
     *
     * @param principals shiro框架中用户信息对象
     * @return 授权信息
     * @throws AuthorizationException 授权异常
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userName = (String) principals.getPrimaryPrincipal();
        // 根据用户名授予相应的权限，用户名需唯一
        return loginService.getRolesAndPermissionsByUserName(userName);
    }
}
