package com.aomc.coop.controller;

import com.aomc.coop.model.User;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.MemberService;
import com.aomc.coop.utils.CodeJsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();
    @Autowired
    private MemberService memberService;

    // 회원 가입
    @PostMapping
    @CrossOrigin
    public ResponseEntity register(@RequestBody User user) { // header, body(json), HTTP.status //
        try {
            return new ResponseEntity(memberService.register(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    // 회원 탈퇴
//    @PutMapping
//    @CrossOrigin
//    public ResponseEntity withdrawal(@RequestBody User user) { // header, body(json), HTTP.status //
//        try {
//            return new ResponseEntity(memberService.withdrawal(user), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
//        }
//    }

}
