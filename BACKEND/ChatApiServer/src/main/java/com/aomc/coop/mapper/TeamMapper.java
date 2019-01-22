package com.aomc.coop.mapper;

import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Team;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface TeamMapper {

    //team생성 - teams 테이블
    @Insert("INSERT INTO teams(name) VALUES(#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "idx")
    void createTeam(final Team team);

    //team생성 - user_has_team 테이블
    @Insert("INSERT INTO user_has_team(team_idx, user_idx, owner_flag) VALUES( #{teamIdx}, #{userIdx}, #{ownerFlag})")
    void createUserHasTeam(final int teamIdx, final int userIdx, final int ownerFlag);

    //team생성 - channels 테이블
    @Insert("INSERT INTO channels(name, team_idx, user_idx) VALUES(#{channel.name}, #{teamIdx}, #{userIdx})")
    @Options(useGeneratedKeys = true, keyProperty = "channel.idx")
    int createChannel(@Param("channel") final Channel channel, @Param("teamIdx") final int teamIdx, @Param("userIdx") final int userIdx);

    //team수정
    @Update("UPDATE teams SET name=#{name}, update_date=now() WHERE idx = #{idx}")
    void updateTeam(final Team team);

    //team비활성화
    @Update("UPDATE teams SET status=0, update_date=now() WHERE idx = #{idx}")
    int deleteTeam(@Param("idx") final int teamIdx);
}
