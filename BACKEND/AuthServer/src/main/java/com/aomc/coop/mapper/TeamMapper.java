package com.aomc.coop.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TeamMapper {

    //team생성 - user_has_team 테이블
    @Insert("INSERT INTO user_has_team(team_idx, user_idx, invite_flag) VALUES( #{team_idx}, #{user_idx}, #{invite_flag})")
    int createUserHasTeam(final int team_idx, final int user_idx, final int invite_flag);
}
