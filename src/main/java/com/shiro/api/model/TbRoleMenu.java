package com.shiro.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色-菜单 关联model
 */
@Data
public class TbRoleMenu implements Serializable {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 角色id
	 */
	private Long roleId;
	/**
	 * 菜单id
	 */
	private Long menuId;
}
