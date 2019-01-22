package com.aomc.coop.mapper;

import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Message;
import com.aomc.coop.model.User;
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

    @Update("UPDATE channels SET name=#{name}, update_date=now() WHERE idx = #{idx}")
    void updateChannel(final Channel channel);

    @Select("SELECT * FROM messages WHERE channel_idx = #{channelIdx}")
    List<Message> getChannelMessage(int channelIdx);

    //채널의 멤버 조회
    @Select("SELECT user_idx FROM user_has_channel WHERE channel_idx = #{channelIdx} and status = 1")
    List<Integer> getChannelUsers(int channelIdx);

    @Insert("INSERT INTO user_has_channel(channel_idx, user_idx) VALUES(#{channelIdx}, #{userIdx})")
    void inviteChannelUser(int channelIdx, int userIdx);

    @Update("UPDATE user_has_channel SET status= 0 WHERE channel_idx = #{channelIdx} and user_idx = #{userIdx}")
    void deleteChannelUser(int channelIdx, int userIdx);

    // == userMapper 로 가야함 == //

    //회원 이름으로 조회
    @Select("SELECT idx, uid, nickname, gender, status FROM users WHERE uid = #{uid}")
    User findBysUserid(final String uid);

    //회원 고유 번호로 조회
    @Select("SELECT idx, uid, nickname, gender, status FROM users WHERE idx = #{userIdx}")
    User findByUserIdx(final int userIdx);


}
