package com.aomc.coop.mapper;

import com.aomc.coop.model.Channel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ChannelMapper {

    //channels 테이블
    @Insert("INSERT INTO channels(name, team_idx) VALUES(#{channel.name}, #{team_idx})")
    @Options(useGeneratedKeys = true, keyProperty = "channel.idx")
    int createChannel(@Param("channel") final Channel channel, @Param("team_idx") final int team_idx);

    //channels 테이블
    @Insert("INSERT INTO user_has_channel(channel_idx, user_idx) VALUES(#{channelIdx}, #{userIdx})")
    int createUserHasChannel(final int channelIdx, final int userIdx);


}
