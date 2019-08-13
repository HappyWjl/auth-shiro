package com.shiro.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色model
 */
@Data
public class TbRole implements Serializable {
	/**
	 * 主键id
	 */
	private Long id;
	/**
	 * 角色名称
	 */
	private String roleName;
}
