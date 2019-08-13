package com.shiro.api.core;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * 自定义session管理
 *
 * Created by Happy王佳乐 on 2019/8/02.
 */
public class MySessionManager extends DefaultWebSessionManager {

    // 前端请求头传这个参数，用于获取SessionId
    private static final String AUTHORIZATION = "Authorization";

    public MySessionManager() {
        super();
    }

    /**
     * 获取sessionId
     *
     * @param request  请求request
     * @param response 返回response
     * @return SessionId
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        // 从请求头中获取SessionId
        String id = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
        // 如果请求头中有 Authorization 则其值为sessionId
        if (!StringUtils.isEmpty(id)) {
            // 参考DefaultWebSessionManager源码中getSessionId方法
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        } else {
            // 否则按默认规则（DefaultWebSessionManager）从cookie取sessionId
            return super.getSessionId(request, response);
        }
    }
}
