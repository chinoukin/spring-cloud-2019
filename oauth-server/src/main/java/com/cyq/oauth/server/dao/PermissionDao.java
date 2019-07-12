package com.cyq.oauth.server.dao;

import com.cyq.oauth.server.entity.Permission;

import java.util.List;

public interface PermissionDao {

    /**
     * 根据角色id查找权限列表
     * @param roleId 角色id
     * @return 权限列表
     */
    List<Permission> findByRoleId(Integer roleId);
}
