package com.aomc.coop.controller;

import com.aomc.coop.model.User;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.auth.Auth;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import com.aomc.coop.service.LoginLogoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*

<인증 서버 기능 리스트>
------------------------------------------
[        기능       ]    [ 상태 ]    [     남은 일들     ]
Login                   ㅣ Done ㅣ 로그인이 되었다면, '/' -> '/chat'으로 redirect할 것
Logout                  ㅣ Done ㅣ 로그아웃 후 '/chat' -> '/'으로 redirect할 것
redis에 회원정보 저장    ㅣ Done ㅣ
회원가입                 ㅣ Done ㅣ
회원탈퇴                 ㅣ Done ㅣ
Profile                 ㅣ Done ㅣ 1) vue에 nickname 띄워주기, 2) 파일 서버 & 스토리지 구축 이후 프로필 사진도 다루어야 함
Profile 프로필 수정      ㅣ Done ㅣ 파일 서버 & 스토리지 구축 이후 프로필 사진도 다루어야 함

가입 시 email 인증       ㅣ Done ㅣ 이메일 URL 클릭 시 로그인 화면으로 redirect
비밀번호 변경            ㅣ Done ㅣ

토큰 헤더                ㅣ Done ㅣ 토큰 헤더에 담아서 보내기

방 초대시 인증서버 동기화 ㅣ Done ㅣ
(비회원 유저라면, 초대 토큰을 받아서 회원가입 시키기 -> user has team에 정보 넣어주고, user has channel에도 넣어줘야 함 (redis에서 찾아서 할 것), teamservice의 356줄 flag를 1로 해서 저장 (2개 다))

비밀번호 분실 후 변경     : Milestone 2

*/

@CrossOrigin
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class LoginLogoutController {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    private LoginLogoutService loginLogoutService;

    /**
     *
     *        @brief GET http://localhost:8082/api/login/
     *        @details 유저의 로그인 요청을 처리하는 함수
     *        @param RequestBody User user
     *        @return ResponseEntity<>
     *
     *        성공시
     *
     *        {
     *              "status": 200,
     *              "message": "로그인 성공",
     *              "description": "Success Login"
     *        }
     *
     *        실패시
     *
     *        {
     *              "status": 400,
     *              "message": "로그인 실패",
     *              "description": "Fail Login"
     *        },
     *
     *

     *        @throws

     *

     */

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody User user) {
        try {
            return new ResponseEntity(loginLogoutService.loginUser(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**
     *
     *        @brief GET http://localhost:8082/api/logout/
     *        @details 유저의 로그아웃 요청을 처리하는 함수
     *        @param RequestBody UserWithToken userWithToken
     *        @return ResponseEntity<>
     *
     *        성공시
     *        {
     *              "status": 200,
     *              "message": "로그아웃 성공",
     *              "description": "Success Logout"
     *        }
     *
     *        실패시
     *        {
     *              "status": 400,
     *              "message": "로그아웃 실패",
     *              "description": "Fail Logout"
     *        }
     *
     *        @throws

     *

     */

    @PostMapping(value = "/logout")
    public ResponseEntity logout(@RequestBody User user) {
        try {
            return new ResponseEntity(loginLogoutService.logoutUser(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }
}


