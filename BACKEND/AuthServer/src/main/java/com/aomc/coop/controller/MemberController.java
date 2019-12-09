package com.aomc.coop.controller;

import com.aomc.coop.dto.NewPasswordRequest;
import com.aomc.coop.dto.RegisterRequest;
import com.aomc.coop.dto.WithdrawalRequest;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.MemberService;
import com.aomc.coop.utils.CodeJsonParser;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class MemberController {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    private final MemberService memberService;

    /**
     *
     *        @brief POST http://localhost:8082/api/members
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

    @ApiOperation(value = "유저의 회원 가입 요청을 처리", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "회원 가입 성공"),
            @ApiResponse(code = 400, message = "회원 가입 실패")
    })
    @PostMapping
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest) {
        try {
            return new ResponseEntity(memberService.register(registerRequest), HttpStatus.OK);
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

    @ApiOperation(value = "유저의 회원 가입 이메일 인증을 처리", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "회원 가입 인증 메일 발송 성공")
    })
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

    @ApiOperation(value = "유저의 회원 탈퇴 요청을 처리", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "회원 탈퇴 성공"),
            @ApiResponse(code = 400, message = "회원 탈퇴 실패")
    })
    @PutMapping(path="/{idx}")
    public ResponseEntity withdrawal(@RequestBody WithdrawalRequest withdrawalRequest, @PathVariable(value = "idx") int idx) {
        return new ResponseEntity(memberService.withdrawal(withdrawalRequest, idx), HttpStatus.OK);
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

    @ApiOperation(value = "유저의 비밀번호 변경 요청을 처리", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "비밀번호 변경 성공"),
            @ApiResponse(code = 400, message = "비밀번호 변경 실패")
    })
    @PutMapping(path="/pwd/{idx}")
    public ResponseEntity changePwd(@RequestBody NewPasswordRequest newPasswordRequest, @PathVariable(value = "idx") int idx) {
        try {
            return new ResponseEntity(memberService.changePwd(newPasswordRequest, idx), HttpStatus.OK);
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

    @ApiOperation(value = "유저 비밀번호 분실 시 인증 이메일을 전송", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "비밀번호 변경 인증 메일 발송 성공")
    })
    @GetMapping(path="/missing/{idx}")
    public ResponseEntity missingEmailAuth(@PathVariable(value = "idx") int idx) {
        return new ResponseEntity(memberService.missingEmailAuth(idx), HttpStatus.OK);
    }
}
