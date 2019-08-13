package com.shiro.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色-权限 关联model
 */
@Data
public class TbRolePermission implements Serializable {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 角色id
	 */
	private Long roleId;
	/**
	 * 权限id
	 */
	private Long permissionId;
}
