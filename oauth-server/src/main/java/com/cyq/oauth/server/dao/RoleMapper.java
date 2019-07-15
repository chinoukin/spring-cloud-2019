package com.cyq.oauth.server.dao;

import com.cyq.oauth.server.entity.Role;

import java.util.List;

public interface RoleMapper {
    /**
     * 根据用户id查找角色列表
     * @param memberId 用户id
     * @return 角色列表
     */
    List<Role> findByMemberId(Integer memberId);
}
