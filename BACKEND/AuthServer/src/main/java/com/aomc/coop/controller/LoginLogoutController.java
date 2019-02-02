package com.aomc.coop.controller;

import com.aomc.coop.model.User;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.utils.CodeJsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import com.aomc.coop.service.LoginLogoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
//    @PostMapping(value = "/logout")
//    @CrossOrigin
//    public ResponseEntity logout(@RequestBody User user) { // header, body(json), HTTP.status //
//        try {
//            return new ResponseEntity(loginLogoutService.logoutUser(user), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
//        }
//    }
}


