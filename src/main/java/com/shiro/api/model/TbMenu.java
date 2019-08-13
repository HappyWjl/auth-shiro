package com.shiro.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色model
 */
@Data
public class TbMenu implements Serializable {
	/**
	 * 主键id
	 */
	private Long id;
	/**
	 * 菜单名称
	 */
	private String menuName;
	/**
	 * 菜单url
	 */
	private String menuUrl;
}
