package com.aomc.coop.controller;

import com.aomc.coop.model.Team;
import com.aomc.coop.model.User;
import com.aomc.coop.response.Status_5000;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.TeamService;
import com.aomc.coop.utils.CodeJsonParser;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.portable.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;


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
     * 		        {"uid":"dmsal7325@naver.com"},
     * 		        {"uid":"Starever222@gmail.com"},
     * 		        {"uid":"yunjea031296@gmail.com"}
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
     *     "status": 200,
     *     "message": "팀 조회 성공",
     *     "description": "Success Team Read",
     *     "data": {
     *         "idx": 15,
     *         "name": "winterdev",
     *         "status": 1
     *     }
     * }
     *
     *        실패시
     *        {
     *     "status": 400,
     *     "message": "팀 정보 없음",
     *     "description": "Fail Read Team"
     *   }
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
     *       {
     *     "status": 400,
     *     "message": "팀 정보 없음",
     *     "description": "Fail Read Team"
     *   }
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
     *           "status": 400,
     *           "message": "팀 정보 없음",
     *           "description": "Fail Read Team"
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
            {
                "status": 200,
                "message": "채널 조회 성공",
                "description": "Success Channel Read",
                "data": [
                    {
                        "idx": 7,
                        "name": "general"
                    },
                    {
                        "idx": 9,
                        "name": "love"
                    }
                ]
            }
     *
     *        실패시
     *       {
     *        "status": 400,
     *        "message": "채널 정보 없음",
     *        "description": "Fail Read Channel"
     *       }
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
     *          "status": 200,
     *          "message": "팀 조회 성공",
     *          "description": "Success Team Read",
     *          "data": [
     *          {
     *             "idx": 15,
     *             "name": "winterdev",
     *             "status": 1
     *          },
     *          {
     *             "idx": 16,
     *             "name": "aomc",
     *             "status": 1
     *          }
     *         ]
     *       }
     *
     *        실패시
     *        User정보없으면
     *        {
     *           "status": 400,
     *           "message": "사용자 정보 없음",
     *           "description": "Fail Read User"
     *         }
     *
     *      Team 정보 없으면
     *      {
     *          "status": 400,
     *          "message": "팀 정보 없음",
     *          "description": "Fail Read Team"
     *      }
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


    /**
     *
     *        @brief get http://localhost:8083/api/team/{teamIdx}
     *
     *        @details Team의 User 목록 조회
     *
     *
     *        @param PathVariable(value = "teamIdx") final int teamIdx
     *
     *        @return ResponseEntity<>
     *
     *        성공시
     *        {
     *         "status": 200,
     *             "message": "팀 조회 성공",
     *             "description": "Success Team Read",
     *             "data": [
     *         {
     *             "idx": 4,
     *                 "uid": "dmsal7325@naver.com",
     *                 "nickname": "mooming"
     *         },
     *         {
     *             "idx": 5,
     *                 "uid": "yunjae",
     *                 "nickname": "yunyun"
     *         },
     *         {
     *             "idx": 6,
     *                 "uid": "garamda",
     *                 "nickname": "catman"
     *         },
     *         {
     *             "idx": 7,
     *                 "uid": "love",
     *                 "nickname": "love"
     *         }
     *     ]
     *     }
     *
     *        실패시
     *        User정보없으면
     *        {
     *           "status": 400,
     *           "message": "사용자 정보 없음",
     *           "description": "Fail Read User"
     *          }
     *
     *       Team 정보 없으면
     *      {
     *          "status": 400,
     *          "message": "팀 정보 없음",
     *          "description": "Fail Read Team"
     *      }
     *      오류나면
     *      {
     *          "status": 500,
     *          "message": "서버 오류",
     *          "description": "Internal Server Error"
     *      }
     *
     *        @throws
     *
     */
    @GetMapping(path="/{teamIdx}")
    @CrossOrigin
    public ResponseEntity readUserOfTeam(@PathVariable(value = "teamIdx") final int teamIdx){
        try{
            return new ResponseEntity<>(teamService.readUserOfTeam(teamIdx), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**
     *
     *        @brief Get http://localhost:8083/api/team/invite
     *
     *        @details Team의 멤버초대(메일전송)
     *
     *
     *        @param RequestBody final Team team
     *        test json
     *        {
     * 	        "idx": 46,
     * 	        "users":[
     * 		        {"uid":"dmsal7325@naver.com"},
     * 		        {"uid":"yunjea0312@naver.com"}
     * 	        ],
     * 	        "channels":[
     * 		        {"idx":39}
     * 	        ]
     *       }
     *
     *        @return ResponseEntity<>
     *
     *        성공시
     *        {
     *          "status": 200,
     *          "message": "팀 초대 성공",
     *          "description": "Success Invite",
     *          "data": [
     *               {
     *                  "uid": "yunjea0312@naver.com"
     *              }
     *          ],
     *          "plusData": [
     *              {
     *                  "uid": "dmsal7325@naver.com"
     *              }
     *          ]
     *      }
     *
     *        실패시
     *        {
     *           "status": 400,
     *           "message": "멤버 초대 실패",
     *           "description": "Fail Team Invite"
     *        }
     *
     *
     *        @throws
     *
     */
    @PostMapping(path="/invite")
    @CrossOrigin
    public ResponseEntity inviteTeam(@RequestBody final Team team){
        try{

            return new ResponseEntity<>(teamService.inviteTeam(team), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**
     *
     *        @brief Get http://localhost:8083/api/team/accept/{token}
     *
     *        @details 초대수락
     *
     *
     *        @param PathVariable(value = "token") final String token
     *
     *        @return ResponseEntity<>
     *
     *        성공시
     *        {
     *          "status": 200,
     *          "message": "초대 승낙 성공",
     *          "description": "Success Accept Invitation"
     *        }
     *
     *        실패시
     *        {
     *          "status": 400,
     *          "message": "인증 키가 올바르지 않습니다",
     *          "description": "Incorrect Authkey"
     *        }
     *
     *        회원가입이 필요한 초대회원일때
     *        {
     *           "status": 400,
     *          "message": "회원가입이 필요합니다",
     *          "description": "Please Signup",
     *          "data": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiJ5dW5qZWEwMzEyQG5hdmVyLmNvbSIsInRlYW1JZHgiOjQ3LCJpc3MiOiJtb29taW5nIiwiZXhwIjoxNTQ4MzA3NDI4fQ.VsEd1X_5vpcwgoLR2RBUmsBKaiFWDfC7XOaGds0pwZc"
     *        }
     *
     *
     *        @throws
     *
     */
//    @GetMapping(path="/accept/{token}")
//    @CrossOrigin
//    public ResponseEntity<Void> acceptInvite(@PathVariable(value = "token") final String token){
//        response.sendRedirect("some-url");
//        try{
//             TeamService.acceptInvite(token);

//            return "redirect:/localhost:9999/chat";

//        }catch (Exception e){
//            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
//        }

//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create("https://www.naver.com/"));

//        return new ResponseEntity<Void>(headers, HttpStatus.MOVED_PERMANENTLY);





//    @RequestMapping(value = "/{path:[^\\.]*}")
//    public String redirect() {
//        return "forward:/";
//    }

        @GetMapping(path="/accept/{token}")
        @CrossOrigin
        ResponseEntity<Void> acceptInvite(RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "token") final String token) throws IOException {

//            try{
                String redirectAddress = teamService.acceptInvite(token);
//                redirectAttributes.addFlashAttribute("token", token);
//                response.setHeader("token", token);
//                response.sendRedirect(redirectAddress);



//            http.request("http://appServer:5001/?key=value", { "Authorization": token }).on("response", (response) => response.pipe(res));



//                response.containsHeader(token);
//                response.getWriter().append(token);

//                String token2 = response.getHeader("token");
//                System.out.println(token2);




            HttpHeaders headers = new HttpHeaders();

            System.out.println("지나감");
            System.out.println(token);
            headers.setLocation(URI.create(redirectAddress));
            headers.set("token", token);
//            headers.setLocation(URI.create("https://www.naver.com/"));
            return new ResponseEntity<Void>(headers, HttpStatus.MOVED_PERMANENTLY);



//            return "forward:"+redirectAddress;




//                return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, redirectAddress).build();

//            }catch (Exception e){
//                System.out.println("오류");
//            }


//        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "http://localhost:9999/chat").build();
    }





}
