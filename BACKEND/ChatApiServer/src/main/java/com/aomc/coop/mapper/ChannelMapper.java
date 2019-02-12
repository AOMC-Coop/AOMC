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
    @Options(useGeneratedKeys = true, keyProperty = "idx")
    int createUserHasChannel(final int channelIdx, final int userIdx);

    @Update("UPDATE channels SET name=#{name}, update_date=now() WHERE idx = #{idx}")
    void updateChannel(final Channel channel);

    //채널의 메세지조회
    @Select("SELECT m.message_idx, content, channel_idx, u.nickname, u.idx as user_idx, DATE_FORMAT(send_date, '%W, %M %D') as send_date, DATE_FORMAT(send_date, '%l:%i %p') as send_time FROM messages m, users u WHERE m.user_idx=u.idx AND channel_idx = #{channelIdx} ORDER BY m.message_idx desc LIMIT #{start}, 10")
    List<Message> getChannelMessage(int channelIdx, int start);

    //채널의 멤버 조회
    @Select("SELECT u.uid, u.nickname FROM user_has_channel has, users u WHERE has.user_idx=u.idx AND has.status=1 AND has.channel_idx=#{channelIdx}")
    List<User> getChannelUsers(int channelIdx);

    @Insert("INSERT INTO user_has_channel(channel_idx, user_idx) VALUES(#{channelIdx}, #{userIdx})")
    void inviteChannelUser(int channelIdx, int userIdx);

    @Update("UPDATE user_has_channel SET status= 0 WHERE channel_idx = #{channelIdx} and user_idx = #{userIdx}")
    void deleteChannelUser(int channelIdx, int userIdx);

    //유저의 channel 조회
    @Select("SELECT DISTINCT c.idx, c.name, has.star_flag, has.status FROM channels c, user_has_channel has WHERE c.idx = has.channel_idx AND has.status=1 AND team_idx=#{teamIdx} AND user_idx=#{userIdx}")
    List<Channel> readChannelOfUser(final int teamIdx, final int userIdx);

    //팀의 channel 조회
    @Select("SELECT DISTINCT c.idx, c.name, uhc.star_flag, uhc.status FROM channels c, user_has_channel uhc WHERE c.idx = uhc.channel_idx AND team_idx=#{teamIdx}")
    List<Channel> readChannelOfTeam(final int teamIdx);

    //팀에 있는 멤버 비활성화
    @Update("UPDATE user_has_channel SET status=0 WHERE channel_idx = #{channelIdx} AND user_idx = #{userIdx}")
    int deactiveUserOfChannel(final int channelIdx, final int userIdx);

    @Select("SELECT status FROM user_has_channel WHERE channel_idx = #{channelIdx} and user_idx = #{userIdx}")
    int findChannelStatus(int channelIdx, int userIdx);

    @Update("UPDATE user_has_channel SET status=#{status} WHERE channel_idx = #{channelIdx} AND user_idx = #{userIdx}")
    void updateChannelStatus(int status, int channelIdx, int userIdx);

    //채널 star_flag 수정
    @Update("UPDATE user_has_channel SET star_flag=#{starFlag} WHERE channel_idx = #{channelIdx} AND user_idx = #{userIdx}")
    void updateUserHasChannelStar(int channelIdx, int userIdx, int starFlag);
}
