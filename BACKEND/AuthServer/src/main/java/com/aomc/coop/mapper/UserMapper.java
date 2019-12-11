package com.aomc.coop.mapper;

import com.aomc.coop.dto.UserDto;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    // Login 용도
    @Select("SELECT idx, pwd, salt, nickname, status, image FROM users WHERE uid = #{uid}")
    UserDto getUserWithUid(final String uid);

    // my page 보는 용도
    @Select("SELECT uid FROM users WHERE idx = #{idx}")
    String getUid(final int idx);

    // 이미 가입한 회원이 있는지 체크하는 용도
    @Select("SELECT uid FROM users WHERE uid = #{uid}")
    String checkUser(final String uid);

    // insert 성공시 1을 return, 실패시 0을 return.
    @Insert("INSERT INTO users(uid, pwd, salt, nickname, image) VALUES( #{uid}, #{pwd}, #{salt}, #{nickname}, #{image} )")
    @Options(useGeneratedKeys = true, keyProperty = "idx")
    int insertUser(UserDto userDto);

    @Update("UPDATE users SET nickname = #{newNickname} WHERE idx = #{idx}")
    int updateUserInfo(final String newNickname, final int idx);

    @Update("UPDATE users SET status = 0 WHERE idx = #{idx}")
    int withdrawal(final int idx);

    @Update("UPDATE users SET pwd = #{pwd}, salt = #{salt} WHERE idx = #{idx}")
    int changePwd(final String pwd, final String salt, final int idx);
}
