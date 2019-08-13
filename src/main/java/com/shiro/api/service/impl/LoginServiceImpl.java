package com.shiro.api.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.shiro.api.core.RedisSessionDAO;
import com.shiro.api.model.*;
import com.shiro.api.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * TbUserServiceImpl
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Value("${shiro.redis.sessionPrefix}")
    private String sessionPrefix;
    @Value("${shiro.redis.verificationCodeTime}")
    private Long verificationCodeTime;
    @Value("${shiro.redis.kickOutKey}")
    private String kickOutKey;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    DefaultKaptcha producer;

    @Autowired
    private TbUserService tbUserService;

    @Autowired
    private TbRoleService tbRoleService;

    @Autowired
    private TbUserRoleService tbUserRoleService;

    @Autowired
    private TbRolePermissionService tbRolePermissionService;

    @Autowired
    private TbPermissionService tbPermissionService;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private RedisSessionDAO redisSessionDAO;

    @Autowired
    private TbRoleMenuService tbRoleMenuService;

    @Autowired
    private TbMenuService tbMenuService;

    @Override
    public TbUser getUserByUserName(String userName) {
        return tbUserService.getByUserName(userName);
    }

    @Override
    public SimpleAuthorizationInfo getRolesAndPermissionsByUserName(String userName) {
        // 查询用户信息
        TbUser tbUser = tbUserService.getByUserName(userName);
        if (Objects.isNull(tbUser)) {
            return new SimpleAuthorizationInfo();
        }
        // 查询用户-角色关联信息
        List<TbUserRole> userRoleList = tbUserRoleService.getByUserId(tbUser.getId());
        if (CollectionUtils.isEmpty(userRoleList)) {
            return new SimpleAuthorizationInfo();
        }
        List<Long> roleIds = userRoleList.stream().map(TbUserRole::getRoleId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roleIds)) {
            return new SimpleAuthorizationInfo();
        }
        // 根据角色id集合，查询角色信息
        List<TbRole> roleList = tbRoleService.getByIds(roleIds);
        if (CollectionUtils.isEmpty(roleList)) {
            return new SimpleAuthorizationInfo();
        }
        Set<String> roleNameSet = roleList.stream().map(TbRole::getRoleName).collect(Collectors.toSet());
        // 根据角色id集合，查询角色-权限关联数据
        List<TbRolePermission> rolePermissionList = tbRolePermissionService.getByRoleIds(roleIds);
        if (CollectionUtils.isEmpty(rolePermissionList)) {
            return new SimpleAuthorizationInfo();
        }
        List<Long> permissionIds = rolePermissionList.stream().map(TbRolePermission::getPermissionId).collect(Collectors.toList());
        // 查询权限数据集合
        List<TbPermission> permissionList = tbPermissionService.getByPermissionIds(permissionIds);
        if (CollectionUtils.isEmpty(permissionList)) {
            return new SimpleAuthorizationInfo();
        }
        Set<String> permissionNameSet = permissionList.stream().map(TbPermission::getPermissionName).collect(Collectors.toSet());
        // 配置角色、权限
        SimpleAuthorizationInfo auth = new SimpleAuthorizationInfo();
        auth.setRoles(roleNameSet);
        auth.setStringPermissions(permissionNameSet);
        return auth;
    }

    @Override
    public boolean checkCodeToken(String sToken, String textStr) {
        Object value = redisTemplate.opsForValue().get(sToken);
        return textStr.equals(value);
    }

    @Override
    public Map<String, Object> generateVerificationCode() throws Exception {
        Map<String, Object> map = new HashMap<>();
        // 生成文字验证码
        String text = producer.createText();
        System.out.println("图片验证码为：" + text);
        // 生成图片验证码
        BufferedImage image = producer.createImage(text);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        // 转成Base64位字符串
        map.put("img", Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        // 生成验证码对应的token  以token为key  验证码为value存在redis中
        String codeToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(codeToken, text, verificationCodeTime, TimeUnit.MINUTES);
        map.put("cToken", codeToken);
        return map;
    }

    @Override
    public List<TbUser> listOnLineUser() {
        Set setSessionIds = redisTemplate.keys(sessionPrefix + "*");
        List list = new ArrayList<TbUser>(setSessionIds.size());
        Iterator<String> iter = setSessionIds.iterator();
        while (iter.hasNext()) {
            String temp = iter.next();
            SimpleSession session = (SimpleSession) redisTemplate.opsForValue().get(temp);
            System.out.println("用户信息：" + session.getAttribute("currentUser"));
            list.add(session.getAttribute("currentUser"));
        }
        return list;
    }

    @Override
    public boolean removeSessionBySessionId(String sessionId) {
        String key = sessionPrefix + sessionId;
        return redisTemplate.delete(key);
    }

    @Override
    public boolean forbiddenByUserName(String userName) {
        List<Session> outCacheSessions = getSessionByUsername(userName);
        for (Session outCacheSession : outCacheSessions) {
            DefaultSessionKey defaultSessionKey = new DefaultSessionKey(outCacheSession.getId());
            Session outSession = sessionManager.getSession(defaultSessionKey);
            // 设置会话的out属性表示踢出了
            if (outSession != null) {
                outSession.setAttribute(kickOutKey, true);
                // 除了强制下线外，将账号设为禁用，禁止再次登录
                tbUserService.forbiddenByUserName(userName);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public List<TbMenu> getMenuByUserName(String userName) {
        List<TbMenu> menuList = new ArrayList<>();
        // 根据用户名称，查询用户信息
        TbUser tbUser = tbUserService.getByUserName(userName);
        if (Objects.isNull(tbUser)) {
            return menuList;
        }
        // 根据用户id查询角色
        List<TbUserRole> tbUserRoleList = tbUserRoleService.getByUserId(tbUser.getId());
        List<Long> roleIds = tbUserRoleList.stream().map(TbUserRole::getRoleId).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(roleIds)) {
            List<TbRoleMenu> tbMenuList = tbRoleMenuService.getByRoleIds(roleIds);
            List<Long> menuIds = tbMenuList.stream().map(TbRoleMenu::getMenuId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(menuIds)) {
                menuList = tbMenuService.getByIds(menuIds);
            }
        }
        return menuList;
    }

    /**
     * 根据用户名，查询session集合
     *
     * @param username 用户名
     * @return session集合
     */
    public List<Session> getSessionByUsername(String username){
        Collection<Session> allSessions = redisSessionDAO.getActiveSessions();
        List<Session> sessions = new ArrayList<>();
        for(Session session : allSessions){
            if(null != session && String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)).equals(username)){
                sessions.add(session);
            }
        }
        return sessions;
    }
}
