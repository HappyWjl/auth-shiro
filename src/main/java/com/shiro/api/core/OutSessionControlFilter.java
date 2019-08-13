package com.shiro.api.core;

import com.shiro.api.enums.ErrorCode;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 多端互T过滤器
 *
 * Created by Happy王佳乐 on 2019/8/02.
 */
public class OutSessionControlFilter extends AccessControlFilter {

    @Value("${shiro.redis.kickOutKey}")
    private String kickOutKey;

    private String kickOutPrefix;
    private RedisTemplate redisTemplate;
    private SessionManager sessionManager;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        // 如果没有登录，不进行多出登录判断
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            return true;
        }
        Session session = subject.getSession();
        String username = (String) subject.getPrincipal();
        Serializable sessionId = session.getId();
        // 获取redis中数据
        List<Serializable> sessionIdList = redisTemplate.opsForList().range(kickOutPrefix + username, 0, -1);
        if (sessionIdList == null || sessionIdList.size() == 0) {
            sessionIdList = new ArrayList<>();
        }
        // 如果队列里没有此sessionId，且用户没有被踢出,当前session放入队列
        if (!sessionIdList.contains(sessionId) && Objects.isNull(session.getAttribute(kickOutKey))) {
            sessionIdList.add(sessionId);
            redisTemplate.opsForList().leftPush(kickOutPrefix + username, sessionId);
        }
        // 如果队列里的sessionId数大于1，开始踢人
        while (sessionIdList.size() > 1) {
            // 获取第一个sessionId（限转成LinkedList，保证顺序）
            Serializable outSessionId = sessionIdList.get(0);
            sessionIdList.remove(outSessionId);
            System.out.println("移除---sessionId: " + outSessionId);
            System.out.println("剩余---sessionId: " + sessionIdList.get(0));
            redisTemplate.opsForList().remove(kickOutPrefix + username, 1, outSessionId);
            try {
                DefaultSessionKey defaultSessionKey = new DefaultSessionKey(outSessionId);
                Session outSession = sessionManager.getSession(defaultSessionKey);
                // 设置会话的out属性表示踢出了
                if (outSession != null) {
                    outSession.setAttribute(kickOutKey, true);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        // session包含out属性，T出
        if (session.getAttribute(kickOutKey) != null) {
            try {
                subject.logout();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            saveRequest(servletRequest);
            // 返回错误码，以及错误文案
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setStatus(HttpStatus.OK.value());
            httpResponse.setContentType("application/json;charset=utf-8");
            httpResponse.getWriter().write("{\"code\":" + ErrorCode.UNAUTHENTIC.getCode() + ", \"msg\":\"" + "您已被强制下线！" + "\"}");
            return false;
        }
        return true;
    }

    public void setKickOutPrefix(String kickOutPrefix) {
        this.kickOutPrefix = kickOutPrefix;
    }
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
