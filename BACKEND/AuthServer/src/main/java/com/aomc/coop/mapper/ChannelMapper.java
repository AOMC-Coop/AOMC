package com.aomc.coop.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ChannelMapper {

    //channels 테이블
    @Insert("INSERT INTO user_has_channel(channel_idx, user_idx) VALUES(#{channel_idx}, #{user_idx})")
    @Options(useGeneratedKeys = true, keyProperty = "idx")
    int createUserHasChannel(final int channel_idx, final int user_idx);
}
