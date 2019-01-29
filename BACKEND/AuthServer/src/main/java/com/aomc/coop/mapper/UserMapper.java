package com.aomc.coop.mapper;

import com.aomc.coop.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    // my page 보는 용도
    @Select("SELECT * FROM users WHERE userId = #{userId}")
    User getUser(final String userId);

    // insert 성공시 1을 return, 실패시 0을 return 한다고 예상함. 은미는 return type을 int로 사용하였는데, boolean이 작동한다면 boolean으로 통일할 것
    @Insert("INSERT INTO users(name, userId, password, email, salt, role, status) VALUES(#{name},#{userId},#{password},#{email},#{salt},#{role},#{status})")
    boolean insertUser(User user);
}
