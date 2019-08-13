package com.shiro.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 账号-角色 关联model
 */
@Data
public class TbUserRole implements Serializable {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 角色id
	 */
	private Long roleId;
}
