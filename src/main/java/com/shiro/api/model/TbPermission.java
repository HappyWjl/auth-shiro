package com.shiro.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 授权model
 */
@Data
public class TbPermission implements Serializable {
	/**
	 * 授权id
	 */
	private Long id;
	/**
	 * 授权名称
	 */
	private String permissionName;
}
