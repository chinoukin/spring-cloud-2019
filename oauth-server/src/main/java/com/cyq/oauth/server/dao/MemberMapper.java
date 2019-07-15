package com.cyq.oauth.server.dao;

import com.cyq.oauth.server.entity.Member;

public interface MemberMapper {

    /**
     * 根据会员名查找会员
     * @param memberName 会员名
     * @return 会员
     */
    Member findByMemberName(String memberName);
}
