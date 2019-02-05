package com.aomc.coop.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import com.aomc.coop.mapper.UserMapper;
import com.aomc.coop.model.UserWithToken;
import com.aomc.coop.response.Status_3000;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import com.aomc.coop.utils.mail.MailSend;

import com.aomc.coop.model.User;
import com.aomc.coop.utils.SHA256;

@Slf4j
@Service
@Transactional
public class MemberService {

    // 접근 진행 순서 : Controller -> Service -> Mapper
    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    @Autowired
    private UserMapper userMapper;

    MailSend mailSend = new MailSend();
    private JavaMailSender mailSender;

    // 가입시 이메일 인증
//    public ResponseType RegisterAuthorization(final User user) {
//
//        String authCode = authCode();
//        SendMailThread sendMailThread = new SendMailThread(mailSender, user.getUid(), null, authCode);
//        // 여기선 token이 쓰이지 않으므로, null을 입력. MailSend 클래스는 비밀번호 변경에도 사용될 것이므로, String token을 아규먼트로 가지고 있긴 해야 함.
//        sendMailThread.start();
//    }


    // 회원 가입

// ***** 여기선 try ~ catch를 안 썼다. 쓰는 걸로 변경할 것
    public ResponseType register(@RequestBody User user) throws NoSuchAlgorithmException {

        String uid = user.getUid();
        User myUser = userMapper.getUserWithUid(uid);
        // db상에 이미 가입되어 있는 내역이 없는 경우, 정상 절차대로 가입
        if(myUser == null) {

            System.out.println("Register");


            SecureRandom secRan = SecureRandom.getInstance("SHA1PRNG");
            int numLength = 16;
            String salt = "";
            for (int i = 0; i < numLength; ++i) {
                salt += secRan.nextInt(10);
            }
            System.out.println("salt : "+ salt);


            String newPassword = salt + user.getPwd();
            String hashPassword = (SHA256.getInstance()).encodeSHA256(newPassword);


            user.setSalt(salt);
            user.setPwd(hashPassword);
            if(user.getUid().equals("admin")) { user.setRole("admin_role"); }
            else { user.setRole("user_role"); }


            user.setStatus(1);

            // 제대로 db에 저장이 되었다면
            if (userMapper.insertUser(user) == 1) {
                System.out.println("Successfully Registered");

                return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Register.getStatus());
// 이 return 값은, ex) Chat.vue -> created().get.then.(response)의 response로 감 // 토큰도 ResponseEntity에 담아서 보낼것
            } else {
            // 어떠한 이유로 db에 저장이 되지 못했다면
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register.getStatus());
            }
        } else {
        // 이미 가입되어 있는 uid(e-mail)이라면
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register_Duplicate.getStatus());
        }
    }

    // 회원 탈퇴
    public ResponseType withdrawal(@RequestBody UserWithToken userWithToken, int idx){

        int userIdx = userWithToken.getIdx();

        if(userIdx == idx)
        {
            try {
                userMapper.withdrawal(idx);
                return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Withdrawal.getStatus());
            } catch (Exception e) {
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Withdrawal.getStatus());
            }
        } else {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Withdrawal.getStatus());
        }


    }



    // 6자리 인증 코드 생성
    public String authCode() {

        Random random = new Random(System.currentTimeMillis());
        int certNumLength = 6;

        int range = (int)Math.pow(10,certNumLength);
        int trim = (int)Math.pow(10, certNumLength-1);
        int result = random.nextInt(range)+trim;

        if(result>range){
            result = result - trim;
        }

        return String.valueOf(result);
    }

    // 메일보내기
    class SendMailThread extends Thread {

        JavaMailSender mailSender;
        String uid;
        String token;
        String authCode;

        public SendMailThread(JavaMailSender mailSender, String uid, String token, String authCode) {
            this.mailSender = mailSender;
            this.uid = uid;
            this.token = token;
            this.authCode = authCode;
        }

        public void run() {
            mailSend.mailsend(mailSender, uid, token, authCode);
        }
    }



    // 비밀번호 변경



//    //초대 승낙
//    public ResponseType acceptInvite(final String token) {
//
//        String string_teamIdx = (String) values.get(token, "teamIdx");
//        String string_userIdx = (String) values.get(token, "userIdx");
//
//        int teamIdx = Integer.parseInt(string_teamIdx);
//        int userIdx = Integer.parseInt(string_userIdx);
//
//        if (teamIdx == -1) {
//            return codeJsonParser.codeJsonParser(Status_5000.FAIL_INCORRECT_AUTHKEY.getStatus());
//        }
//        if (userIdx == 0) {
//            return codeJsonParser.codeJsonParser(Status_5000.PLEASE_SIGNUP.getStatus(), token);
//        }
//        teamMapper.updateAuthFlag(teamIdx, userIdx);
//        return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_ACCEPT_INVITE.getStatus());
//
//    }

}

// ResponseEntity : Response body와 함께 Http status를 전송하기 위해 사용
// cf) @ResponseBody : 자바 객체를 Http response body로 변환
// @RequestBody : Http request body를 java 객체로 변환
// NoSuchAlgorithmException : This exception is thrown when a particular cryptographic algorithm is requested but is not available in the environment
// <Generic>
// 클래스 내부에서 사용할 데이터 타입을 외부에서 지정하는 기법
// 컴파일 단계에서 오류가 검출된다.
// 중복의 제거와 타입 안전성을 동시에 추구할 수 있게 되었다.

// setter로 설정되지 않은 이외의 User 클래스 데이터들 -> (@RequestBody User user)를 통해 프론트에서 입력한 정보들이 자동으로 넘어와서 저장됨