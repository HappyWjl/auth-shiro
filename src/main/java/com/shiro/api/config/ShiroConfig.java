package com.shiro.api.config;

import com.shiro.api.core.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import javax.servlet.Filter;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置文件
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
@Configuration
public class ShiroConfig {

    // session缓存时间
    @Value("${shiro.redis.sessionLive}")
    private long sessionLive;
    // session前缀
    @Value("${shiro.redis.sessionPrefix}")
    private String sessionPrefix;
    // redis缓存时间
    @Value("${shiro.redis.cacheLive}")
    private long cacheLive;
    // redis缓存前缀
    @Value("${shiro.redis.cachePrefix}")
    private String cachePrefix;
    // 验证码缓存前缀
    @Value("${shiro.redis.kickoutPrefix}")
    private String kickoutPrefix;

    /**
     * 自定义redis缓存管理器
     *
     * @param redisTemplate redis模版
     * @return redis缓存管理器对象
     */
    @Bean(name = "redisCacheManager")
    public RedisCacheManager redisCacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        // 将redis缓存时间及前缀，放到配置中
        redisCacheManager.setCacheLive(cacheLive);
        redisCacheManager.setCacheKeyPrefix(cachePrefix);
        redisCacheManager.setRedisTemplate(redisTemplate);
        return redisCacheManager;
    }

    /**
     * 凭证匹配器（密码加密）
     *
     * @return 凭证匹配器对象
     */
    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 加密算法，指定MD5加密
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 加密的次数为2次，相当于 MD5(MD5())
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    /**
     * Session ID生成管理器
     *
     * @return sessionId 生成器对象
     */
    @Bean(name = "sessionIdGenerator")
    public JavaUuidSessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * 自定义RedisSessionDAO
     *
     * @param sessionIdGenerator sessionId 生成器
     * @param redisTemplate      redis模版
     * @return RedisSessionDAO 对象
     */
    @Bean(name = "redisSessionDAO")
    public RedisSessionDAO redisSessionDAO(JavaUuidSessionIdGenerator sessionIdGenerator, RedisTemplate redisTemplate) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        // 配置sessionId生成器
        redisSessionDAO.setSessionIdGenerator(sessionIdGenerator);
        // 配置session缓存时间及缓存前缀
        redisSessionDAO.setSessionLive(sessionLive);
        redisSessionDAO.setSessionKeyPrefix(sessionPrefix);
        // 配置redis模版
        redisSessionDAO.setRedisTemplate(redisTemplate);
        return redisSessionDAO;
    }

    /**
     * 自定义session管理器
     *
     * @param redisSessionDAO redisSessionDAO
     * @return session管理器 对象
     */
    @Bean(name = "sessionManager")
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        MySessionManager mySessionManager = new MySessionManager();
        // 配置 自定义的RedisSessionDAO 对象
        mySessionManager.setSessionDAO(redisSessionDAO);
        return mySessionManager;
    }

    /**
     * 自定义域
     *
     * @return 自定义域 对象
     */
    @Bean(name = "myRealm")
    public MyRealm myRealm() {
        MyRealm myRealm = new MyRealm();
        // 启用缓存，默认为false
        myRealm.setCachingEnabled(true);
        return myRealm;
    }

    /**
     * 安全管理器
     *
     * @param sessionManager    session管理器
     * @param redisCacheManager redis缓存管理器
     * @return 安全管理器
     */
    @Bean(name = "securityManager")
    public SecurityManager securityManager(SessionManager sessionManager, RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 配置自定义域
        securityManager.setRealm(myRealm());
        // 配置session管理器
        securityManager.setSessionManager(sessionManager);
        // 配置redis缓存管理器
        securityManager.setCacheManager(redisCacheManager);
        return securityManager;
    }

    /**
     * 多端互T控制过滤器
     *
     * @param sessionManager session管理器
     * @param redisTemplate  redis模版
     * @return 互T控制过滤器
     */
    @Bean(name = "outSessionControlFilter")
    public OutSessionControlFilter outSessionControlFilter(SessionManager sessionManager, RedisTemplate redisTemplate) {
        OutSessionControlFilter outSessionControlFilter = new OutSessionControlFilter();
        outSessionControlFilter.setSessionManager(sessionManager);
        outSessionControlFilter.setRedisTemplate(redisTemplate);
        outSessionControlFilter.setKickOutPrefix(kickoutPrefix);
        return outSessionControlFilter;
    }

    /**
     * shiro过滤器
     *
     * @param securityManager         安全管理器
     * @param outSessionControlFilter 多端互T控制过滤器
     * @return shiro过滤工厂Bean
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, OutSessionControlFilter outSessionControlFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filters = new HashMap(2);
        filters.put("out", outSessionControlFilter);
        shiroFilterFactoryBean.setFilters(filters);
        // 注意拦截链配置顺序，不能颠倒
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap();
        // 退出
        filterChainDefinitionMap.put("/logout", "logout");
        // 可匿名访问，无需登录，即可访问的路径
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/loginByMenu", "anon");
        filterChainDefinitionMap.put("/loginByCaptcha", "anon");
        filterChainDefinitionMap.put("/captcha", "anon");
        // 拦截所有请求
        filterChainDefinitionMap.put("/**", "out,authc");
        // 未认证 跳转未认证页面
        shiroFilterFactoryBean.setLoginUrl("/unAuthen");
        // 未授权 跳转未权限页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unAuthor");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 将springboot中的过滤器替换成shiro过滤器，不替换会报错
     *
     * @return 过滤器注册Bean
     */
    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        // 设置servlet容器来管理其生命周期，默认false，spring来管理其生命周期
        proxy.setTargetFilterLifecycle(true);
        // 配置改为shiro过滤器
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    /**
     * 默认创建代理类Bean
     * 在这里将代理改为cglib代理的方式
     *
     * @return 默认创建代理类Bean
     */
    @Bean(name = "advisorAutoProxyCreator")
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 设置成true，是以cglib动态代理生成代理类；设置成false，就是默认用JDK动态代理生成代理类
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * shiro中的权限拦截器
     *
     * @param securityManager 权限管理器
     * @return shiro中的权限拦截器
     */
    @Bean(name = "authorizationAttributeSourceAdvisor")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        // 配置权限管理器
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
