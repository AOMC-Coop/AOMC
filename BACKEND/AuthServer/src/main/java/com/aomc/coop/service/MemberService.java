package com.aomc.coop.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.aomc.coop.mapper.UserMapper;
import com.aomc.coop.model.UserWithToken;
import com.aomc.coop.response.Status_3000;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import com.aomc.coop.utils.mail.MailSend;

import com.aomc.coop.model.User;
import com.aomc.coop.utils.SHA256;

import javax.annotation.Resource;

@Slf4j
@Service
@Transactional
public class MemberService {

    // 접근 진행 순서 : Controller -> Service -> Mapper
    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    @Autowired
    private UserMapper userMapper;

    @Resource(name="redisTemplate")
    private HashOperations<String, String, Object> hashOperations;

    MailSend mailSend = new MailSend();

    private JavaMailSender mailSender;

    public MemberService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }



    // <1. 회원 가입>
    // 가입시 이메일 인증


    // 회원 가입

// ***** 여기선 try ~ catch를 안 썼다. 쓰는 걸로 변경할 것
    public ResponseType register(@RequestBody User user) {

        try {

            if (!user.getPwd().equals(user.getConfirm_pwd())) {
                System.out.println(user.getPwd() + " " + user.getConfirm_pwd());
                System.out.println("Password doesn't match!");
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register.getStatus());
            }

            String uid = user.getUid();
            User myUser = userMapper.getUserWithUid(uid);

            // db상에 이미 가입되어 있는 내역이 없는 경우, 정상 절차대로 가입
            if (myUser == null) {

                System.out.println("Register");

                HashMap<String, Object> hashMap = new HashMap<>();

                SecureRandom secRan = SecureRandom.getInstance("SHA1PRNG");
                int numLength = 16;
                String salt = "";
                for (int i = 0; i < numLength; ++i) {
                    salt += secRan.nextInt(10);
                }

                String authUrl = "";
                for (int i = 0; i < numLength; ++i) {
                    authUrl += secRan.nextInt(10);
                }

                System.out.println("authUrl : " + authUrl);

                String newPassword = salt + user.getPwd();
                String hashPassword = (SHA256.getInstance()).encodeSHA256(newPassword);
                String nickname = user.getNickname();
                int gender = user.getGender();

                hashMap.put("uid", uid);
                hashMap.put("pwd", hashPassword);
                hashMap.put("salt", salt);
                hashMap.put("nickname", nickname);
                hashMap.put("gender", gender);

                hashOperations.putAll(authUrl, hashMap);
                hashOperations.getOperations().expire(authUrl, 3L, TimeUnit.MINUTES);

                SendMailThread sendMailThread = new SendMailThread(mailSender, uid, authUrl);
                sendMailThread.run();

                return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Register_Auth_Mail_Sent.getStatus());

            } else {
                // 이미 가입되어 있는 uid(e-mail)이라면
                System.out.println("Already registerd ID!");
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register_Duplicate.getStatus());
            }
        }
        catch (Exception e) {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register.getStatus());
        }
    }

// ***** 이메일 인증 성공시 바로 로그인 창으로 넘어가도록 구조를 바꿔보자
    public ResponseType emailAuth(@PathVariable(value = "authUrl") final String authUrl) {

        try {
            Map userInfo = hashOperations.entries(authUrl);
            String uid = (String) userInfo.get("uid");

            if(userMapper.getUserWithUid(uid) != null ) {
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register_Duplicate.getStatus());
            }

            if(userInfo == null) {
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register_Timeout.getStatus());

            } else {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat( "yy-MM-dd HH:mm:ss" , Locale.KOREA );


                String pwd = (String) userInfo.get("pwd");
                String salt = (String) userInfo.get("salt");
                String role = "user_role";
                String nickname = (String) userInfo.get("nickname");
// ***** gender : Object -> int 캐스팅 에러 발생. 일단은 gender를 1로 강제 할당. 추후 필히 변경
                // int gender = ((Integer) userInfo.get("gender")).intValue();
                // Date reg_date = new Date( timestamp.getTime());
                // Date access_date = (Date) userInfo.get(("access_date"));

                User user = new User();
                user.setUid(uid);
                user.setPwd(pwd);
                user.setSalt(salt);
                user.setRole(role);
                user.setNickname(nickname);
                user.setGender(1);
                // user.setReg_date(reg_date);
                // user.setAccess_date(access_date);
                user.setStatus(1);

                // 제대로 db에 저장이 되었다면
                if (userMapper.insertUser(user) == 1) {
                    System.out.println("Successfully Registered");
                    return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Register.getStatus());
                } else {
                    // 어떠한 이유로 db에 저장이 되지 못했다면
                    System.out.println("Fail to store user in db!");
                    return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register.getStatus());
                }

            }

        }
        catch (Exception e) {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register.getStatus());
        }

    }

    // <2. 회원 탈퇴>
    public ResponseType withdrawal(@RequestBody UserWithToken userWithToken, final int idx) {

        int userIdx = userWithToken.getIdx();

        System.out.println("userIdx : " + userIdx +"  idx : " + idx);
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

    // 메일보내기
    class SendMailThread extends Thread {

        JavaMailSender mailSender;
        String uid;
        String authUrl;

        public SendMailThread(JavaMailSender mailSender, String uid, String authUrl) {
            this.mailSender = mailSender;
            this.uid = uid;
            this.authUrl = authUrl;
        }

        public void run() {
            mailSend.mailsend(mailSender, uid, authUrl);
        }
    }

    // <3. 비밀번호 변경>
//    public ResponseType changePwd (@RequestBody User user, final int idx) {
//        String pwd = user.getPwd();
//
//
//
//    }


    // <4. 비밀번호 분실 후 변경>

    // Forgot password?
    // 이메일 발송
    // 이메일 버튼을 통해 비밀번호 수정 창으로 이동





    // 비밀번호 변경
//    public ResponseType registerAuthorization(@RequestBody User user) {
//
//        String authCode = authCode();
//        SendMailThread sendMailThread = new SendMailThread(mailSender, user.getUid(), null, authCode);
//        // 여기선 token이 쓰이지 않으므로, null을 입력. MailSend 클래스는 비밀번호 변경에도 사용될 것이므로, String token을 아규먼트로 가지고 있긴 해야 함.
//        sendMailThread.start();
//
//    }

    // 6자리 인증 코드 생성
//    public String authCode() {
//
//        Random random = new Random(System.currentTimeMillis());
//        int certNumLength = 6;
//
//        int range = (int)Math.pow(10,certNumLength);
//        int trim = (int)Math.pow(10, certNumLength-1);
//        int result = random.nextInt(range)+trim;
//
//        if(result>range){
//            result = result - trim;
//        }
//
//        return String.valueOf(result);
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