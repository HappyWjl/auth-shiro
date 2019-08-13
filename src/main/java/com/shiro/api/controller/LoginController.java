package com.shiro.api.controller;

import com.shiro.api.enums.ErrorCode;
import com.shiro.api.model.*;
import com.shiro.api.service.*;
import com.shiro.api.util.Response;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.shiro.api.util.ResponseUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务接口Controller
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
@RestController
public class LoginController {

    @Autowired
    DefaultKaptcha producer;

    @Autowired
    private LoginService loginService;

    /**
     * 账号、密码登录
     *
     * @param tbUser 用户对象
     * @return Response
     */
    @PostMapping(value = "/login")
    public Response login(TbUser tbUser) {
        // 校验账号密码是否传入
        if (StringUtils.isEmpty(tbUser.getUserName()) || StringUtils.isEmpty(tbUser.getPassword())) {
            return ResponseUtil.makeError(ErrorCode.PARAM_ERROR);
        }
        Subject subject = SecurityUtils.getSubject();
        // 生成token
        UsernamePasswordToken token = new UsernamePasswordToken(tbUser.getUserName(), tbUser.getPassword());
        try {
            subject.login(token);
            return ResponseUtil.makeSuccess(subject.getSession().getId());
        } catch (Exception e) {
            return ResponseUtil.makeError(ErrorCode.LOGIN_ERROR);
        }
    }

    /**
     * 账号、密码登录，带菜单信息
     *
     * @param tbUser 用户对象
     * @return Response
     */
    @PostMapping(value = "/loginByMenu")
    public Response loginByMenu(TbUser tbUser) {
        // 校验账号密码是否传入
        if (StringUtils.isEmpty(tbUser.getUserName()) || StringUtils.isEmpty(tbUser.getPassword())) {
            return ResponseUtil.makeError(ErrorCode.PARAM_ERROR);
        }
        Subject subject = SecurityUtils.getSubject();
        // 生成token
        UsernamePasswordToken token = new UsernamePasswordToken(tbUser.getUserName(), tbUser.getPassword());
        try {
            subject.login(token);
            // 根据用户名称，查询菜单权限
            List<TbMenu> menuList = loginService.getMenuByUserName(tbUser.getUserName());
            // 封装结果
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("token", subject.getSession().getId());
            resultMap.put("menuList", menuList);
            return ResponseUtil.makeSuccess(resultMap);
        } catch (Exception e) {
            return ResponseUtil.makeError(ErrorCode.LOGIN_ERROR);
        }
    }

    /**
     * 账号、密码登录，带验证码
     *
     * @param tbUser  用户对象
     * @param cToken  验证码对应的token
     * @param textStr 用户输入的验证码
     * @return Response
     */
    @PostMapping(value = "/loginByCaptcha")
    public Response loginByCaptcha(TbUser tbUser, String cToken, String textStr) {
        // 校验账号、密码、验证码等是否传入
        if (StringUtils.isEmpty(tbUser.getUserName()) || StringUtils.isEmpty(tbUser.getPassword()) ||
            StringUtils.isEmpty(cToken) || StringUtils.isEmpty(textStr)) {
            return ResponseUtil.makeError(ErrorCode.PARAM_ERROR);
        }
        Subject subject = SecurityUtils.getSubject();
        // 生成token
        UsernamePasswordToken token = new UsernamePasswordToken(tbUser.getUserName(), tbUser.getPassword());
        // 校验验证码
        boolean flag = loginService.checkCodeToken(cToken, textStr);
        if(!flag) {
            return ResponseUtil.makeError(ErrorCode.CAPTCHA_CHECK_ERROR);
        }
        try {
            subject.login(token);
            return ResponseUtil.makeSuccess(subject.getSession().getId());
        } catch (Exception e) {
            return ResponseUtil.makeError(ErrorCode.LOGIN_ERROR);
        }
    }

    /**
     * 当前用户退出登录
     *
     * @return Response
     */
    @PostMapping("/logout")
    public Response logout() {
        // 从缓存中删除缓存
        loginService.removeSessionBySessionId(SecurityUtils.getSubject().getSession().getId().toString());
        SecurityUtils.getSubject().logout();
        return ResponseUtil.makeSuccess(ErrorCode.LAY_OUT_SUCCESS);
    }

    /**
     * 生成验证码
     *
     * @return Response
     */
    @PostMapping("/captcha")
    public Response captcha() {
        try {
            return ResponseUtil.makeSuccess(loginService.generateVerificationCode());
        } catch (Exception e) {
            return ResponseUtil.makeError(ErrorCode.CAPTCHA_ERROR);
        }
    }

    /**
     * 获取在线用户
     *
     * @return Response
     */
    @PostMapping("/listOnLine")
    public Response listOnLine() {
        return ResponseUtil.makeSuccess(loginService.listOnLineUser());
    }

    /**
     * 踢出用户
     *
     * @param userName
     * @return
     */
    @RequestMapping("/kickOutUser")
    @ResponseBody
    public Response kickOutUser(String userName) {
        return ResponseUtil.makeSuccess(loginService.forbiddenByUserName(userName));
    }

    /**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     *
     * @return
     */
    @RequestMapping(value = "/unAuthen")
    public Response unAuthen() {
        return ResponseUtil.makeError(ErrorCode.UNAUTHENTIC);
    }

    /**
     * 未授权
     *
     * @return
     */
    @RequestMapping(value = "/unAuthor")
    public Response unAuthor() {
        return ResponseUtil.makeError(ErrorCode.UNAUTHORIZED);
    }
}
