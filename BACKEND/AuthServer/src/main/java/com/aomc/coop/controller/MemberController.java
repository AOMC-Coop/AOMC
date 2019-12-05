package com.aomc.coop.controller;

import com.aomc.coop.dto.NewPwd;
import com.aomc.coop.dto.User;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.MemberService;
import com.aomc.coop.utils.CodeJsonParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.security.NoSuchAlgorithmException;

@CrossOrigin
@RestController
@RequestMapping("/api/members")
public class MemberController {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    private final MemberService memberService;

    public MemberController (MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     *
     *        @brief GET http://localhost:8082/api/members
     *        @details 유저의 회원 가입 요청을 처리하는 함수
     *        @param @RequestBody User user
     *        @return ResponseEntity<>
     *
     *        성공시
     *
     *        {
     *              "status": 200,
     *              "message": "회원 가입 성공",
     *              "description": "Success Register"
     *        }
     *
     *        실패시
     *
     *        {
     *              "status": 400,
     *              "message": "회원 가입 실패",
     *              "description": "Fail Register"
     *        }
     *

     *        @throws

     *

     */

    @PostMapping
    public ResponseEntity register(@RequestBody User user) {
        try {
            return new ResponseEntity(memberService.register(user), HttpStatus.OK);
        }
        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**
     *
     *        @brief GET http://localhost:8082/api/members/eauth
     *        @details 유저의 회원 가입 이메일 인증을 처리하는 함수
     *        @PathVariable(value = "authUrl") String authUrl, @PathVariable(value = "invite_token") String invite_token
     *        @return ResponseEntity<>
     *
     *        성공시
     *
     *        {
     *              "status": 200,
     *              "message": "회원 가입 인증 메일 발송 성공",
     *              "description": "Success Auth Mail Sent"
     *        }
     *
     *        실패시
     *
     *        @throws

     *

     */

    @GetMapping(path="/eauth/{authUrl}/{invite_token}")
    public ResponseEntity emailAuth(RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response,
                                    @PathVariable(value = "authUrl") String authUrl, @PathVariable(value = "invite_token") String invite_token) {
        String redirectAddress = memberService.emailAuth(authUrl, invite_token);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectAddress));
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).headers(headers).build();
//       return new ResponseEntity(memberService.emailAuth(authUrl, invite_token), HttpStatus.OK);
    }

    /**
     *
     *        @brief GET http://localhost:8082/api/members/{idx}
     *        @details 유저의 회원 탈퇴 요청을 처리하는 함수
     *        @param @RequestBody UserWithToken userWithToken, @PathVariable(value = "idx") int idx
     *        @return ResponseEntity<>
     *
     *        성공시
     *
     *        {
     *              "status": 200,
     *              "message": "회원 탈퇴 성공",
     *              "description": "Success Withdrawal"
     *        }
     *
     *        실패시
     *
     *        {
     *              "status": 400,
     *              "message": "회원 탈퇴 실패",
     *              "description": "Fail Withdrawal"
     *        }
     *
     *        @throws
     *

     */

    @PutMapping(path="/{idx}")
    public ResponseEntity withdrawal(@RequestBody User user, @PathVariable(value = "idx") int idx) {
        return new ResponseEntity(memberService.withdrawal(user, idx), HttpStatus.OK);
    }

    /**
     *
     *        @brief GET http://localhost:8082/api/pwd/{idx}
     *        @details 유저의 비밀번호 변경 요청을 처리하는 함수
     *        @param @RequestBody NewPwd newPwd, @PathVariable(value = "idx") int idx
     *        @return ResponseEntity<>
     *
     *        성공시
     *
     *        {
     *              "status": 200,
     *              "message": "비밀번호 변경 성공",
     *              "description": "Success Change Password"
     *        }
     *
     *        실패시
     *
     *        {
     *              "status": 400,
     *              "message": "비밀번호 변경 실패",
     *              "description": "Fail Change Password"
     *        }
     *
     *        @throws

     *

     */

    @PutMapping(path="/pwd/{idx}")
    public ResponseEntity changePwd(@RequestBody NewPwd newPwd, @PathVariable(value = "idx") int idx) {
        try {
            return new ResponseEntity(memberService.changePwd(newPwd, idx), HttpStatus.OK);
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**
     *
     *        @brief GET http://localhost:8082/api/missing/{idx}
     *        @details 유저 비밀번호 분실 시 인증 이메일을 전송하는 함수
     *        @param @PathVariable(value = "idx") int idx
     *        @return ResponseEntity<>
     *
     *        성공시
     *
     *        {
     *              "status": 200,
     *              "message": "비밀번호 변경 인증 메일 발송 성공",
     *              "description": "Success Missing Password Mail Auth"
     *        }
     *
     *        실패시
     *
     *
     *        @throws

     *

     */

    // 비밀번호 분실 시 인증용 이메일 전송
    @GetMapping(path="/missing/{idx}")
    public ResponseEntity missingEmailAuth(@PathVariable(value = "idx") int idx) {
        return new ResponseEntity(memberService.missingEmailAuth(idx), HttpStatus.OK);
    }
}
