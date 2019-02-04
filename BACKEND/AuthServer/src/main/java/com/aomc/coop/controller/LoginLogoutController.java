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
Login                ㅣ Done ㅣ http header, body 전송 남음 & 로그인이 되었다면, /login 으로 접근시 자동으로 /chat으로 redirect할 것
Logout               ㅣ Done ㅣ vue에서도 jwt 토큰을 파괴해야 함
redis에 회원정보 저장 ㅣ Done ㅣ
Register             ㅣ Done ㅣ Vue 구축되면 테스트 해볼 것
회원탈퇴              ㅣ Done ㅣ Vue 구축되면 테스트 해볼 것
Mypage               ㅣ Done ㅣ 파일 서버 & 스토리지 구축 이후, 프로필 사진도 다루어야 함
Mypage 프로필 수정    ㅣ Done ㅣ
비밀번호 찾기          ㅣ      ㅣ <-- 요놈까지 하고 vue 전부 구축
email 인증            ㅣ      ㅣ


*/

@RestController
@RequestMapping
// *** 즉, 기본 URL 요청을 받으면 /login, /logout으로 넘어오게 됨
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


