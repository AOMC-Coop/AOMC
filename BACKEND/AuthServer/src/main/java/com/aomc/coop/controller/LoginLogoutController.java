package com.aomc.coop.controller;

import com.aomc.coop.model.User;
import com.aomc.coop.model.UserWithToken;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.utils.CodeJsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import com.aomc.coop.service.LoginLogoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*

<인증 서버 기능 리스트>
------------------------------------------
[        기능       ] [ 상태 ] [     남은 일들     ]
                     ㅣ      ㅣ 토큰 헤더에 담아서 보내기 -> 대대적으로 손 봐야할 듯 : utils.auth.AuthAspect
Login                ㅣ Done ㅣ 로그인이 되었다면, '/' -> '/chat'으로 redirect할 것
Logout               ㅣ Done ㅣ 로그아웃 후 '/chat' -> '/'으로 redirect할 것
redis에 회원정보 저장 ㅣ Done ㅣ
회원가입              ㅣ Done ㅣ
회원탈퇴              ㅣ Done ㅣ
Profile              ㅣ Done ㅣ vue에 nickname 띄워주기, 파일 서버 & 스토리지 구축 이후 프로필 사진도 다루어야 함
Profile 프로필 수정   ㅣ Done ㅣ 파일 서버 & 스토리지 구축 이후 프로필 사진도 다루어야 함

비밀번호 변경         ㅣ      ㅣ
가입 시 email 인증    ㅣ      ㅣ
초대                  ㅣ      ㅣ

*/

@RestController
@RequestMapping
public class LoginLogoutController {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    @Autowired
    private LoginLogoutService loginLogoutService;

    //로그인
    @PostMapping(value = "/login")
    @CrossOrigin
    public ResponseEntity login(@RequestBody User user) { // header, body(json), HTTP.status //
        try {
            return new ResponseEntity(loginLogoutService.loginUser(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    //로그아웃
    @PostMapping(value = "/logout")
    @CrossOrigin
    public ResponseEntity logout(@RequestBody UserWithToken userWithToken) { // header, body(json), HTTP.status //
        try {
            return new ResponseEntity(loginLogoutService.logoutUser(userWithToken), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }
}


