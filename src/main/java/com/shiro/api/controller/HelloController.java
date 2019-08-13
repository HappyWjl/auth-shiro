package com.shiro.api.controller;

import com.shiro.api.util.Response;
import com.shiro.api.util.ResponseUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业务接口Controller
 *
 * Created by Happy王子乐 on 2019/8/02.
 */
@RestController
public class HelloController {

    /**
     * 没有角色、权限限制，只要登录即可访问
     *
     * @return Response
     */
    @RequestMapping("/hello")
    public Response hello() {
        return ResponseUtil.makeSuccess("无角色、权限限制接口，任何登录账号都可以请求该方法");
    }

    // 一些角色组合用法：
    // 属于user角色
    // @RequiresRoles("user")
    // 必须同时属于user和admin角色
    // @RequiresRoles({"user","admin"})
    // 属于user或者admin之一;修改logical为OR 即可
    // @RequiresRoles(value={"user","admin"},logical=Logical.OR)

    /**
     * 后面接口按照傻瓜场景进行权限演示：
     * 角色：zhuren（主任） 、 teacher（老师）
     * 权限：jiaoxuesheng（教学生）、shouxuefei（收学费）
     *
     * 于是：
     * 主任可以教学生（宝刀未老的主任是由老师升职上去的），可以收学费
     * 老师可以教学生，不可以收学费
     */

    /**
     * 拥有“zhuren”角色，登录后可正常访问
     *
     * @return Response
     */
    @RequiresRoles("zhuren")
    @RequestMapping("/role1")
    public Response role1() {
        return ResponseUtil.makeSuccess("zhuren角色正确，可以请求该方法");
    }

    /**
     * 拥有“teacher”角色，登录后可正常访问
     *
     * @return Response
     */
    @RequiresRoles("teacher")
    @RequestMapping("/role2")
    public Response role2() {
        return ResponseUtil.makeSuccess("teacher角色正确，可以请求该方法");
    }

    /**
     * 拥有“zhuren”角色和“teacher”角色，登录后可正常访问
     *
     * @return Response
     */
    @RequiresRoles({"teacher", "zhuren"})
    @RequestMapping("/role3")
    public Response role3() {
        return ResponseUtil.makeSuccess("zhuren角色和teacher角色都有，可以请求该方法");
    }

    /**
     * 拥有“zhuren”角色或“teacher”角色，登录后可正常访问
     *
     * @return Response
     */
    @RequiresRoles(value={"teacher", "zhuren"}, logical=Logical.OR)
    @RequestMapping("/role4")
    public Response role4() {
        return ResponseUtil.makeSuccess("有zhuren角色或者有teacher角色，可以请求该方法");
    }

    /**
     * 拥有“jiaoxuesheng”权限，登录后可正常访问
     *
     * @return Response
     */
    @RequiresPermissions("jiaoxuesheng")
    @RequestMapping("/permission1")
    public Response permission1() {
        return ResponseUtil.makeSuccess("jiaoxuesheng权限正确，可以请求该方法");
    }

    /**
     * 拥有“shoubanfei”权限，登录后可正常访问
     *
     * @return Response
     */
    @RequiresPermissions("shoubanfei")
    @RequestMapping("/permission2")
    public Response permission2() {
        return ResponseUtil.makeSuccess("shoubanfei权限正确，可以请求该方法");
    }

    /**
     * 拥有"jiaoxuesheng"权限和"shoubanfei"权限，登录后可正常访问
     *
     * @return Response
     */
    @RequiresPermissions({"shoubanfei", "jiaoxuesheng"})
    @RequestMapping("/permission3")
    public Response permission3() {
        return ResponseUtil.makeSuccess("拥有jiaoxuesheng权限和shoubanfei权限，可以请求该方法");
    }

    /**
     * 拥有"jiaoxuesheng"权限或"shoubanfei"权限，登录后可正常访问
     *
     * @return Response
     */
    @RequiresPermissions(value={"shoubanfei", "jiaoxuesheng"}, logical=Logical.OR)
    @RequestMapping("/permission4")
    public Response permission4() {
        return ResponseUtil.makeSuccess("拥有jiaoxuesheng权限或shoubanfei权限，可以请求该方法");
    }
}
