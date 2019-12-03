package com.aomc.coop.mapper;

import com.aomc.coop.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    @Update("UPDATE users SET image = #{image} WHERE idx = #{idx}")
    int updateUserImage(final int idx, String image);

}
