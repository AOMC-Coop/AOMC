package com.aomc.coop.mapper;

import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Team;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
@Repository
public interface TeamMapper {

    //team생성 - teams 테이블
    @Insert("INSERT INTO teams(name) VALUES(#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "idx")
    void createTeam(final Team team);

    //team생성 - user_has_team 테이블
    @Insert("INSERT INTO user_has_team(team_idx, user_idx, owner_flag) VALUES( #{teamIdx}, #{userIdx}, #{ownerFlag})")
    int createUserHasTeam(final int teamIdx, final int userIdx, final int ownerFlag);

    //team상세조회
    @Select("SELECT idx, name, status FROM teams WHERE idx = #{idx}")
    Team readTeamDetail(@Param("idx") final int teamIdx);

    //유저가 가진 팀 조회
    @Select("SELECT t.idx, t.name, t.status FROM teams t, user_has_team u WHERE t.idx = u.team_idx AND user_idx = #{idx}")
    List<Team> readTeamOfUser(@Param("idx") final int userIdx);

    //team수정
    @Update("UPDATE teams SET name=#{name}, update_date=now() WHERE idx = #{idx}")
    void updateTeam(final Team team);

    //team비활성화(삭제와 같은 기능)
    @Update("UPDATE teams SET status=0, update_date=now() WHERE idx = #{idx}")
    int deleteTeam(@Param("idx") final int teamIdx);

    //팀에 있는 멤버 비활성화
    @Update("UPDATE user_has_team SET status=0 WHERE team_idx = #{teamIdx} AND user_idx = #{userIdx}")
    int deactiveUserOfTeam(final int teamIdx, final int userIdx);


    //이메일인증 - user_has_team 테이블
    @Insert("INSERT INTO user_has_team(authkey) VALUES( #{authkey}) WHERE team_idx = #{teamIdx} AND user_idx = #{userIdx}")
    int sendAuthEmail(final String authkey, final int teamIdx, final int userIdx);

    //이메일 인증시
    @Update("UPDATE user_has_team SET certification_flag=1 WHERE team_idx = #{teamIdx} AND user_idx = #{userIdx}")
    int updateAuthFlag(final int teamIdx, final int userIdx);

}
