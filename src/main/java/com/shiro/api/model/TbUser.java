package com.shiro.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户model
 */
@Data
public class TbUser implements Serializable {

	/**
	 * 用户id
	 */
	private Long id;
	/**
	 * 用户昵称
	 */
	private String userName;
	/**
	 * 用户密码
	 */
	private String password;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 是否禁用
	 */
	private Integer forbidden;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
}
