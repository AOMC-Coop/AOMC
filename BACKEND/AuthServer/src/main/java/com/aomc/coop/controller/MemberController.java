package com.aomc.coop.controller;

import com.aomc.coop.model.NewPwd;
import com.aomc.coop.model.User;
import com.aomc.coop.model.UserWithToken;
import com.aomc.coop.model.NewPwd;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.MemberService;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/members")
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

    // 회원 가입 시 이메일 인증
// ***** URL Redirection 때문에 return type을 String으로 바꾸다 보니, 예외처리 제대로 안되고 있음
    @GetMapping(path="/{authUrl}/{invite_token}")
    @CrossOrigin
    public ResponseEntity emailAuth(RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response,
                                    @PathVariable(value = "authUrl") String authUrl, @PathVariable(value = "invite_token") String invite_token) {
        try {
            String redirectAddress = memberService.emailAuth(authUrl, invite_token);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(redirectAddress));
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).headers(headers).build();
//            return new ResponseEntity(memberService.emailAuth(authUrl, invite_token), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    // 회원 탈퇴
//    @Auth
    @PutMapping(path="/{idx}")
    @CrossOrigin
    public ResponseEntity withdrawal(@RequestBody UserWithToken userWithToken, @PathVariable(value = "idx") int idx) {
        try {
            return new ResponseEntity(memberService.withdrawal(userWithToken, idx), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    // 비밀번호 변경
//    @Auth
    @PutMapping(path="/pwd/{idx}")
    @CrossOrigin
    public ResponseEntity changePwd(@RequestBody NewPwd newPwd, @PathVariable(value = "idx") int idx) {
        try {
            return new ResponseEntity(memberService.changePwd(newPwd, idx), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    // 비밀번호 분실 시 인증용 이메일 전송
//    @Auth
    @GetMapping(path="/missing/{idx}")
    @CrossOrigin
    public ResponseEntity missingEmailAuth(@PathVariable(value = "idx") int idx) {
        try {
            return new ResponseEntity(memberService.missingEmailAuth(idx), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }


//    // 비밀번호 분실 후 변경
//    @Auth
//    @PutMapping(path="/missing/{idx}")
//    @CrossOrigin
//    public ResponseEntity changeMissingPwd(@RequestBody NewPwd newPwd, @PathVariable(value = "idx") int idx) {
//        try {
//            return new ResponseEntity(memberService.changePwd(newPwd, idx), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
//        }
//    }

}
