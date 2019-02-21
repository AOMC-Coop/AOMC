package com.aomc.coop.mapper;

import com.aomc.coop.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.Date;

@Mapper
@Repository
// *** 제대로 working 하는지 테스트 해 볼 것
public interface UserMapper {

    // Login 용도
    @Select("SELECT * FROM users WHERE uid = #{uid}")
    User getUserWithUid(final String uid);

    // my page 보는 용도
    @Select("SELECT * FROM users WHERE idx = #{idx}")
    User getUserWithIdx(final int idx);

    // insert 성공시 1을 return, 실패시 0을 return.
    @Insert("INSERT INTO users(uid, pwd, salt, nickname, gender) VALUES(#{uid},#{pwd},#{salt},#{nickname},#{gender})")
    int insertUser(User user);

    // mysql query문으로 자동 업데이트 될 것임 vs 그런데 LoginController에서 필요함 -> 일단 사용
    @Update("UPDATE users SET access_date = now() WHERE uid = #{uid}")
    int updateAccess_date(final String uid);

    @Update("UPDATE users SET nickname = #{newNickname}, gender = #{newGender} WHERE idx = #{idx}")
    int updateUserInfo(final int idx, final String newNickname, final int newGender);

    @Update("UPDATE users SET status = 0 WHERE idx = #{idx}")
    int withdrawal(final int idx);

    @Update("UPDATE users SET pwd = #{pwd}, salt = #{salt} WHERE idx = #{idx}")
    int changePwd(final String pwd, final String salt, final int idx);
}
