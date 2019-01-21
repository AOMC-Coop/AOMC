package com.aomc.coop.mapper;

import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Team;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface TeamMapper {

    //Auto Increment 값을 받아오고 싶으면 리턴 타입을 int로(Auto Increment 컬럼 타입)
    @Insert("INSERT INTO teams(name) VALUES(#{team.name})")
    @Options(useGeneratedKeys = true, keyColumn = "team.idx")
    int createTeam(@Param("team") final Team team);

    @Insert("INSERT INTO user_has_team(team_idx, user_idx) VALUES( #{team_idx}, #{user_idx})")
    void createUserHasTeam(@Param("team_idx") final int team_idx, @Param("idx") final int user_idx);

    @Insert("INSERT INTO channels(name, team_idx, user_idx) VALUES(#{channel.name}, #{team_idx}, #{user_idx})")
    @Options(useGeneratedKeys = true, keyColumn = "channel.idx")
    int createChannel(@Param("channel") final Channel channel, @Param("team_idx") final int team_idx, @Param("user_idx") final int user_idx);
}
