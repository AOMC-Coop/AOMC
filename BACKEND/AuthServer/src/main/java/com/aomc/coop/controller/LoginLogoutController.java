package com.aomc.coop.controller;

import com.aomc.coop.dto.LoginRequest;
import com.aomc.coop.service.LoginLogoutService;
import com.aomc.coop.utils.ResponseType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/*

<인증 서버 기능 리스트>
------------------------------------------
[        기능       ]    [ 상태 ]    [     남은 일들     ]
Login                   ㅣ Done ㅣ 로그인이 되었다면, '/' -> '/chat'으로 redirect할 것
Logout                  ㅣ Done ㅣ 로그아웃 후 '/chat' -> '/'으로 redirect할 것
redis에 회원정보 저장    ㅣ Done ㅣ
회원가입                 ㅣ Done ㅣ
회원탈퇴                 ㅣ Done ㅣ
Profile                 ㅣ Done ㅣ
Profile 프로필 수정      ㅣ Done ㅣ

가입 시 email 인증       ㅣ Done ㅣ 이메일 URL 클릭 시 로그인 화면으로 redirect
비밀번호 변경            ㅣ Done ㅣ

토큰 헤더                ㅣ Done ㅣ

방 초대시 인증서버 동기화 ㅣ Done ㅣ

*/

@CrossOrigin
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginLogoutController {

    private final LoginLogoutService loginLogoutService;

    /**
     *
     *        @brief GET http://localhost:8082/api/login/
     *        @details 유저의 로그인 요청을 처리하는 함수
     *        @param @RequestBody User user
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

    @ApiOperation(value = "유저의 로그인 요청을 처리", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "로그인 성공"),
            @ApiResponse(code = 400, message = "로그인 실패\n로그인 실패 : 가입된 아이디가 없음\n로그인 실패 : 잘못된 비밀번호\n로그인 실패 : 탈퇴한 유저")
    })
    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        ResponseType responseType = loginLogoutService.loginUser(loginRequest);
        HttpHeaders httpHeaders = (HttpHeaders)responseType.getPlusData();
        responseType.setPlusData(null);
        return new ResponseEntity(responseType, httpHeaders, HttpStatus.OK);
    }

    /**
     *
     *        @brief GET http://localhost:8082/api/logout/
     *        @details 유저의 로그아웃 요청을 처리하는 함수
     *        @param @RequestBody UserWithToken userWithToken
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

    @ApiOperation(value = "유저의 로그아웃 요청을 처리", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "로그아웃 성공"),
            @ApiResponse(code = 400, message = "로그아웃 실패")
    })
    @PostMapping(value = "/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        String token = request.getHeader("X-Auth-Token");
        return new ResponseEntity(loginLogoutService.logoutUser(token), HttpStatus.OK);
    }
}


