package com.aomc.coop.mapper;

import com.aomc.coop.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.Date;

@Mapper
@Repository
// *** 제대로 working 하는지 테스트 해 볼 것
public interface UserMapper {

    // my page 보는 용도
    @Select("SELECT * FROM users WHERE uid = #{uid}")
    User getUser(final String uid);

    // insert 성공시 1을 return, 실패시 0을 return.
    @Insert("INSERT INTO users(uid, pwd, salt, nickname, gender) VALUES(#{uid},#{pwd},#{salt},#{nickname},#{gender})")
    int insertUser(User user);

    // mysql query문으로 자동 업데이트 될 것임 vs 그런데 LoginController에서 필요함 -> 일단 사용
    @Update("UPDATE users set access_date = now() WHERE uid = #{uid}")
    int updateAccess_date(final String userId);
}
