package com.aomc.coop.controller;

import com.aomc.coop.model.Team;
import com.aomc.coop.model.User;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.TeamService;
import com.aomc.coop.utils.CodeJsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 *
 * @brief API http://localhost:8083/api/team
 * @details Team생성, Team조회, Team수정, Team비활성화, Team멤버관리, Team에 멤버초대, Channel조회
 * @author LeeEunmi
 * @date 2019-01-23
 * @version 1.0.0
 *
 */



@Slf4j
@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;
    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    public TeamController(final TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     *
     *        @brief POST http://localhost:8083/api/team
     *
     *        @details Team 생성
     *        test json
     *        {
     * 	        "name":"aomc",
     * 	        "users":[
     * 		        {"idx":6},
     * 		        {"idx":4},
     * 		        {"idx":5}
     * 	        ]
     *        }
     *
     *        @param RequestBody final Team team
     *
     *        @return ResponseEntity<>
     *
     *        성공시
     *        {
     *           "status": 200,
     *           "message": "팀 생성 성공",
     *           "description": "Success Channel Create"
     *        }
     *
     *        실패시
     *        {
     *           "status": 400,
     *           "message": "팀 생성 실패",
     *           "description": "Fail Team Create"
     *        }
     *
     *        @throws
     *
     */
    @PostMapping
    @CrossOrigin
    public ResponseEntity makeTeam(@RequestBody final Team team){
        try{
            return new ResponseEntity<>(teamService.createTeam(team), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**
     *
     *        @brief Get http://localhost:8083/api/team/{teamIdx}
     *
     *        @details Team 조회
     *
     *
     *        @param PathVariable(value = "teamIdx") final int teamIdx
     *
     *        @return ResponseEntity<>
     *
     *        성공시
     *        {
     *           "status": 200,
     *           "message": "팀 조회 성공",
     *           "description": "Success Channel Create",
     *           "data": {
     *              "idx": 15,
     *              "name": "aomc",
     *              "status": 1,
     *              "owner": 0
     *            }
     *        }
     *
     *        실패시
     *        {
     *           "status": 400,
     *           "message": "정보 없음",
     *           "description": "Fail Read"
     *        }
     *
     *        @throws
     *
     */
    @GetMapping(path="/{teamIdx}")
    @CrossOrigin
    public ResponseEntity makeTeam(@PathVariable(value = "teamIdx") final int teamIdx){
        try{
            return new ResponseEntity<>(teamService.readTeam(teamIdx), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    //팀수정
    @PutMapping
    @CrossOrigin
    public ResponseEntity updateTeam(@RequestBody final Team team){
        try{
            return new ResponseEntity<>(teamService.updateTeam(team), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    //팀비활성화
    @DeleteMapping(path="/{teamIdx}")
    @CrossOrigin
    public ResponseEntity deleteTeam(@PathVariable(value = "teamIdx") final int teamIdx){
        try{
            return new ResponseEntity<>(teamService.deleteTeam(teamIdx), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    //팀원초대
    @GetMapping(path="/invite/{teamIdx}&{uid}")
    @CrossOrigin
    public ResponseEntity inviteTeam(@PathVariable(value = "teamIdx") final int teamIdx, @PathVariable(value = "uid") final String uid){
        try{
            return new ResponseEntity<>(teamService.inviteTeam(teamIdx, uid), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    //채널조회
    @GetMapping(path="/channel/{teamIdx}&{userIdx}")
    @CrossOrigin
    public ResponseEntity readChannel(@PathVariable(value = "teamIdx") final int teamIdx, @PathVariable(value = "userIdx") final int userIdx){
        try{
            return new ResponseEntity<>(teamService.readChannel(teamIdx, userIdx), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    //ownwe만 팀에있는 멤버 비활성화
    @PutMapping(path="/manage/{teamIdx}&{userIdx}")
    @CrossOrigin
    public ResponseEntity deactiveUser(@PathVariable(value = "teamIdx") final int teamIdx, @PathVariable(value = "userIdx") final int userIdx){
        try{
            return new ResponseEntity<>(teamService.deactiveUser(teamIdx, userIdx), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

}
