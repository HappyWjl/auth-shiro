package com.shiro.api.dao;

import com.shiro.api.model.TbRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色表dao
 *
 * Created by Happy王佳乐 on 2019/8/02.
 */
public interface TbRoleDAO {

    /**
     * 根据角色id集合查询角色信息集合
     *
     * @param roleIds 角色id集合
     * @return 角色信息集合
     */
    List<TbRole> getByIds(@Param("roleIds") List<Long> roleIds);
}
