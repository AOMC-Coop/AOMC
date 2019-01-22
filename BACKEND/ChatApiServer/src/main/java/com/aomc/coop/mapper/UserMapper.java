package com.aomc.coop.mapper;

import com.aomc.coop.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    //회원 이름으로 조회
    @Select("SELECT idx, uid, nickname, gender, status FROM users WHERE uid = #{uid}")
    User findBysUserid(final String uid);

    //회원 고유 번호로 조회
    @Select("SELECT idx, uid, nickname, gender, status FROM users WHERE idx = #{userIdx}")
    User findByUserIdx(final int userIdx);

}
