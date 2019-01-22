package com.aomc.coop.mapper;

import com.aomc.coop.model.Channel;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    //channel 조회
    @Select("SELECT c.idx, c.name, uhc.star_flag, uhc.status FROM channels c, user_has_channel uhc WHERE c.idx = uhc.channel_idx AND team_idx=#{teamIdx} AND user_idx=#{userIdx}")
    List<Channel> readChannel(final int teamIdx, final int userIdx);

    //팀에 있는 멤버 비활성화
    @Update("UPDATE user_has_channel SET status=0 WHERE channel_idx = #{channelIdx} AND user_idx = #{userIdx}")
    int deactiveUserOfChannel(final int channelIdx, final int userIdx);
}
