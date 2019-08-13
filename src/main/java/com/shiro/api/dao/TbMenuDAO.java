package com.shiro.api.dao;

import com.shiro.api.model.TbMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单表dao
 *
 * Created by Happy王佳乐 on 2019/8/02.
 */
public interface TbMenuDAO {

    /**
     * 根据角色id集合查询角色菜单集合
     *
     * @param roleIds 角色id集合
     * @return 角色菜单集合
     */
    List<TbMenu> getByIds(@Param("roleIds") List<Long> roleIds);
}
