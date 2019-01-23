package com.aomc.coop.controller;

import com.aomc.coop.model.Team;
import com.aomc.coop.model.User;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.TeamService;
import com.aomc.coop.utils.CodeJsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;


/**
 *
 * @brief API http://localhost:8083/api/team
 * @details Team생성, Team조회, Team수정, Team비활성화, Team에 멤버초대, Team멤버관리,  Channel조회
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
     *        @brief Post http://localhost:8083/api/team
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
     *        @brief Get http://localhost:8083/api/team/detail/{teamIdx}
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
    @GetMapping(path="/detail/{teamIdx}")
    @CrossOrigin
    public ResponseEntity readTeamDatail(@PathVariable(value = "teamIdx") final int teamIdx){
        try{
            return new ResponseEntity<>(teamService.readTeamDetail(teamIdx), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**
     *
     *        @brief Put http://localhost:8083/api/team
     *
     *        @details Team 수정
     *        test json
     *        {
     * 	        "idx": 19,
     * 	        "name":"winterdevcamp"
     *        }
     *
     *
     *        @param RequestBody final Team team
     *
     *        @return ResponseEntity<>
     *
     *        성공시
     *        {
     *           "status": 200,
     *           "message": "팀 수정 성공",
     *           "description": "Success Team Update"
     *        }
     *
     *        실패시
     *        {
     *           "status": 400,
     *           "message": "팀 수정 실패",
     *           "description": "Fail Team Update"
     *        }
     *
     *        팀정보가 없으면
     *        {
     *            "status": 400,
     *            "message": "정보 없음",
     *            "description": "Fail Read"
     *        }
     *
     *        비활성화된 팀이면
     *        {
     *             "status": 400,
     *             "message": "삭제된 팀입니다",
     *             "description": "Fail Team Deactive"
     *        }
     *
     *        @throws
     *
     */
    @PutMapping
    @CrossOrigin
    public ResponseEntity updateTeam(@RequestBody final Team team){
        try{
            return new ResponseEntity<>(teamService.updateTeam(team), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**
     *
     *        @brief Delete http://localhost:8083/api/team/{teamIdx}
     *
     *        @details Team 삭제(비활성화)
     *
     *
     *        @param PathVariable(value = "teamIdx") final int teamIdx
     *
     *        @return ResponseEntity<>
     *
     *        성공시
     *        {
     *           "status": 200,
     *           "message": "팀 삭제 성공",
     *           "description": "Success Team Delete"
     *        }
     *
     *        실패시
     *        {
     *           "status": 400,
     *           "message": "팀 삭제 실패",
     *           "description": "Fail Team Delete"
     *        }
     *
     *        팀정보가 없으면
     *        {
     *            "status": 400,
     *            "message": "정보 없음",
     *            "description": "Fail Read"
     *        }
     *
     *        비활성화된 팀이면
     *        {
     *             "status": 400,
     *             "message": "삭제된 팀입니다",
     *             "description": "Fail Team Deactive"
     *        }
     *
     *        @throws
     *
     */
    @DeleteMapping(path="/{teamIdx}")
    @CrossOrigin
    public ResponseEntity deleteTeam(@PathVariable(value = "teamIdx") final int teamIdx){
        try{
            return new ResponseEntity<>(teamService.deleteTeam(teamIdx), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**
     *
     *        @brief Get http://localhost:8083/api/team/invite/{teamIdx}&{uid}
     *
     *        @details Team의 멤버초대
     *
     *
     *        @param PathVariable(value = "teamIdx") final int teamIdx, PathVariable(value = "uid") final String uid
     *
     *        @return ResponseEntity<>
     *
     *        성공시
     *        {
     *           "status": 200,
     *           "message": "팀 초대 성공",
     *           "description": "Success Invite"
     *        }
     *
     *        실패시
     *        {
     *           "status": 400,
     *           "message": "멤버 초대 실패",
     *           "description": "Fail Team Invite"
     *        }
     *
     *        없는 User인 경우
     *        {
     *            "status": 400,
     *            "message": "정보 없음",
     *            "description": "Fail Read"
     *        }
     *
     *        @throws
     *
     */
    @GetMapping(path="/invite/{teamIdx}&{uid}")
    @CrossOrigin
    public ResponseEntity inviteTeam(@PathVariable(value = "teamIdx") final int teamIdx, @PathVariable(value = "uid") final String uid){
        try{
            return new ResponseEntity<>(teamService.inviteTeam(teamIdx, uid), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**
     *
     *        @brief Get http://localhost:8083/api/team/channel/{teamIdx}&{userIdx}
     *
     *        @details Team의 Channel 조회
     *
     *
     *        @param PathVariable(value = "teamIdx") final int teamIdx, PathVariable(value = "userIdx") final int userIdx
     *
     *        @return ResponseEntity<>
     *
     *        성공시
     *        {
     *           "status": 200,
     *           "message": "채널 조회 성공",
     *           "description": "Success Channel Read",
     *           "data": [
     *           {
     *             "idx": 7,
     *             "name": "general",
     *             "star_flag": 0,
     *             "status": 0,
     *             "teamIdx": 0
     *           },
     *           {
     *             "idx": 9,
     *             "name": "love",
     *             "star_flag": 0,
     *             "status": 0,
     *             "teamIdx": 0
     *           }
     *          ]
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
    @GetMapping(path="/channel/{teamIdx}&{userIdx}")
    @CrossOrigin
    public ResponseEntity readChannel(@PathVariable(value = "teamIdx") final int teamIdx, @PathVariable(value = "userIdx") final int userIdx){
        try{
            return new ResponseEntity<>(teamService.readChannel(teamIdx, userIdx), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**
     *
     *        @brief Put http://localhost:8083/api/team/manage/{teamIdx}&{userIdx}
     *
     *        @details Team의 멤버 비활성화
     *
     *
     *        @param PathVariable(value = "teamIdx") final int teamIdx, PathVariable(value = "userIdx") final int userIdx
     *
     *        @return ResponseEntity<>
     *
     *        성공시
     *        {
     *           "status": 200,
     *           "message": "멤버 비활성화 성공",
     *           "description": "Success Deactive User"
     *        }
     *
     *        실패시
     *        {
     *           "status": 400,
     *           "message": "멤버 비활성화 실패",
     *           "description": "Fail Deactive User"
     *        }
     *
     *        @throws
     *
     */
    @PutMapping(path="/manage/{teamIdx}&{userIdx}")
    @CrossOrigin
    public ResponseEntity deactiveUser(@PathVariable(value = "teamIdx") final int teamIdx, @PathVariable(value = "userIdx") final int userIdx){
        try{
            return new ResponseEntity<>(teamService.deactiveUser(teamIdx, userIdx), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**
     *
     *        @brief get http://localhost:8083/api/team/user/{userIdx}
     *
     *        @details User의 Team 목록 조회
     *
     *
     *        @param PathVariable(value = "userIdx") final int userIdx
     *
     *        @return ResponseEntity<>
     *
     *        성공시
     *        {
     *     "status": 200,
     *     "message": "팀 조회 성공",
     *     "description": "Success Team Read",
     *     "data": [
     *         {
     *             "idx": 15,
     *             "name": "winterdev",
     *             "status": 1
     *         },
     *         {
     *             "idx": 16,
     *             "name": "aomc",
     *             "status": 1
     *         }
     *     ]
     * }
     *
     *        실패시
     *        User정보없으면
     *        {
     *     "status": 400,
     *     "message": "사용자 정보 없음",
     *     "description": "Fail Read User"
     *   }
     *
     *   Team 정보 없으면
     *   {
     *     "status": 400,
     *     "message": "팀 정보 없음",
     *     "description": "Fail Read Team"
     *   }
     *
     *
     *        @throws
     *
     */
    @GetMapping(path="/user/{userIdx}")
    @CrossOrigin
    public ResponseEntity readTeamOfUser(@PathVariable(value = "userIdx") final int userIdx){
        try{
            return new ResponseEntity<>(teamService.readTeamOfUser(userIdx), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }


//    @GetMapping(path="/{teamIdx}")
//    @CrossOrigin
//    public ResponseEntity readUserOfTeam(@PathVariable(value = "teamIdx") final int teamIdx){
//        try{
//            return new ResponseEntity<>(teamService.readUserOfTeam(teamIdx), HttpStatus.OK);
//        }catch (Exception e){
//            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
//        }
//    }

    @GetMapping(path="/sendmail/{teamIdx}&{uid}")
    @CrossOrigin
    public ResponseEntity sendMail(@PathVariable(value = "teamIdx") final int teamIdx, @PathVariable(value = "uid") final String uid){
        try{

            return new ResponseEntity<>(teamService.sendMail(teamIdx, uid), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }




}
