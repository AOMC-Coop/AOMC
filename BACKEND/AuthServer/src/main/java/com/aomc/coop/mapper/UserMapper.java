package com.aomc.coop.mapper;

import com.aomc.coop.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

// ***** SELECT * 연산은 지양할 것! 필요한 것만 가져와서 access time을 줄이자

    // Login 용도
    @Select("SELECT idx, pwd, salt, nickname, status, image FROM users WHERE uid = #{uid}")
    User getUserWithUid(final String uid);

    // my page 보는 용도
    @Select("SELECT uid FROM users WHERE idx = #{idx}")
    String getUid(final int idx);

    // 이미 가입한 회원이 있는지 체크하는 용도
    @Select("SELECT uid FROM users WHERE uid = #{uid}")
    String checkUser(final String uid);

    // insert 성공시 1을 return, 실패시 0을 return.
    @Insert("INSERT INTO users(uid, pwd, salt, nickname, gender) VALUES(#{uid},#{pwd},#{salt},#{nickname},#{gender})")
    int insertUser(User user);

    // mysql query문으로 자동 업데이트 될 것임
//    @Update("UPDATE users SET access_date = now() WHERE uid = #{uid}")
//    int updateAccess_date(final String uid);

    @Update("UPDATE users SET nickname = #{newNickname} WHERE idx = #{idx}")
    int updateUserInfo(final String newNickname, final int idx);

    @Update("UPDATE users SET status = 0 WHERE idx = #{idx}")
    int withdrawal(final int idx);

    @Update("UPDATE users SET pwd = #{pwd}, salt = #{salt} WHERE idx = #{idx}")
    int changePwd(final String pwd, final String salt, final int idx);
}
