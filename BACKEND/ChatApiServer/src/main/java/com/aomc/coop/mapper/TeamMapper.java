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
    @Insert("INSERT INTO user_has_team(team_idx, user_idx, owner_flag) VALUES( #{team_idx}, #{user_idx}, #{owner_flag})")
    void createUserHasTeam(final int team_idx, final int user_idx, final int owner_flag);


    //team수정
    @Update("UPDATE teams SET name=#{name}, update_date=now() WHERE idx = #{idx}")
    void updateTeam(final Team team);

    //team삭제?
    @Delete("DELETE FROM teams WHERE idx = #{idx}")
    void deleteTeam(@Param("idx") final int team_idx);
}
