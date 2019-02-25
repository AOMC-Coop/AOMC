package com.aomc.coop.service;

import java.nio.channels.Channel;
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

import com.aomc.coop.mapper.*;
import com.aomc.coop.model.NewPwd;
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

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private ChannelMapper channelMapper;

// ***** 결국 String으로 변경함
    @Resource(name="redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    MailSend mailSend = new MailSend();

    private JavaMailSender mailSender;

    public MemberService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }



    // <1. 회원 가입>
    // 가입시 이메일 인증

    // 회원 가입

    public ResponseType register(@RequestBody User user) {

        try {

// *** pwd, confirm_pwd 가 같은지 체크하는 작업은
// *** 서버 단에도 있는 것이 가장 바람직하겠으나, 이미 클라이언트 단에서 예외처리를 하였으므로
// *** 성능 향상을 위해 주석처리 함

//            if (!user.getPwd().equals(user.getConfirm_pwd())) {
////                System.out.println(user.getPwd() + " " + user.getConfirm_pwd());
////                System.out.println("Password doesn't match!");
//                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register.getStatus());
//            }

// ***** DB 연산을 줄이기 위해 Mapper 함수 변경함
            String uid = user.getUid();
            String myUser = userMapper.checkUser(uid);

            // db상에 이미 가입되어 있는 내역이 없는 경우, 정상 절차대로 가입
            if (myUser == null) {

//                System.out.println("Register");

                HashMap<String, String> hashMap = new HashMap<>();

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

//                System.out.println("authUrl : " + authUrl);

                String newPassword = salt + user.getPwd();
                String hashPassword = (SHA256.getInstance()).encodeSHA256(newPassword);
                String nickname = user.getNickname();

                hashMap.put("uid", uid);
                hashMap.put("pwd", hashPassword);
                hashMap.put("salt", salt);
                hashMap.put("nickname", nickname);

                hashOperations.putAll(authUrl, hashMap);
                hashOperations.getOperations().expire(authUrl, 5L, TimeUnit.HOURS);

                String invite_token = user.getInvite_token();

                if(invite_token== null){
                    SendMailThread sendMailThread = new SendMailThread(mailSender, uid, authUrl);
                    sendMailThread.start();
                } else {
                    SendMailThread sendMailThread = new SendMailThread(mailSender, uid, authUrl, invite_token);
                    sendMailThread.runToInvite();
                }


                return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Register_Auth_Mail_Sent.getStatus());

            } else {
                // 이미 가입되어 있는 uid(e-mail)이라면
//                System.out.println("Already registerd ID!");
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register_Duplicate.getStatus());
            }
        }
        catch (Exception e) {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register.getStatus());
        }
    }

// ***** 이메일 인증 성공시 바로 로그인 창으로 넘어가도록 구조를 바꿔보자
    public String emailAuth(final String authUrl, String invite_token) {

        try {

            Map userInfo = hashOperations.entries(authUrl);
            String uid = (String) userInfo.get("uid");

            if(userMapper.getUserWithUid(uid) != null ) {
// ***** 적당한 예외처리를 찾지 못해서 일단 같은 URL로 박아둠
                return "http://localhost:9999";
//                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register_Duplicate.getStatus());
            }

            if(userInfo == null) {
// ***** 적당한 예외처리를 찾지 못해서 일단 같은 URL로 박아둠
                return "http://localhost:9999";
//                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register_Timeout.getStatus());

            } else {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat( "yy-MM-dd HH:mm:ss" , Locale.KOREA );


                String pwd = (String) userInfo.get("pwd");
                String salt = (String) userInfo.get("salt");
                String role = "user_role";
                String nickname = (String) userInfo.get("nickname");
                String image = "https://avatars0.githubusercontent.com/u/28426269?s=460&v=4";

                // Date reg_date = new Date( timestamp.getTime());
                // Date access_date = (Date) userInfo.get(("access_date"));

                User user = new User();
                user.setUid(uid);
                user.setPwd(pwd);
                user.setSalt(salt);
                // user.setRole(role);
                user.setNickname(nickname);
                user.setImage(image);
                // user.setReg_date(reg_date);
                // user.setAccess_date(access_date);
                // user.setStatus(1);

                String check = invite_token;
                // 제대로 db에 저장이 되었다면
                if (userMapper.insertUser(user) == 1) {

                    System.out.println("Successfully Registered");
//                    return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Register.getStatus());
                } else {
                    // 어떠한 이유로 db에 저장이 되지 못했다면
                    System.out.println("Fail to store user in db!");
// ***** 적당한 예외처리를 찾지 못해서 일단 같은 URL로 박아둠
//                    return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register.getStatus());
                }
                int user_idx = user.getIdx();

                // 초대로 회원가입한 유저인 경우
                if(!"invitation".equals(check)){

                    Map invitedUserInfo = hashOperations.entries(invite_token);

                    String team_idx = (String) invitedUserInfo.get("teamIdx");
                    int teamIdx = Integer.parseInt(team_idx);

                    int invite_flag = 1;

                    String channel_idx = (String) invitedUserInfo.get("channelIdx");
                    int channelIdx = Integer.parseInt(channel_idx);

// ***** user has team에 정보 넣어주고, user has channel에도 넣어줘야 함 (redis에서 찾아서 할 것), teamservice의 356줄 flag를 1로 해서 저장 (2개 다))

                    teamMapper.createUserHasTeam(teamIdx, user_idx, invite_flag);
                    channelMapper.createUserHasChannel(channelIdx, user_idx);
                }

                return "http://localhost:9999";


            }

        }
        catch (Exception e) {
// ***** 적당한 예외처리를 찾지 못해서 일단 같은 URL로 박아둠
            System.out.println(e);
            return "http://localhost:9999";
//            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register.getStatus());
        }

    }

    // <2. 회원 탈퇴>
    public ResponseType withdrawal(@RequestBody UserWithToken userWithToken, final int idx) {

        int userIdx = userWithToken.getIdx();

//        System.out.println("userIdx : " + userIdx +"  idx : " + idx);
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
        String invite_token;
        int idx;

        public SendMailThread(JavaMailSender mailSender, String uid, String authUrl) {
            this.mailSender = mailSender;
            this.uid = uid;
            this.authUrl = authUrl;
        }

        public SendMailThread(JavaMailSender mailSender, String uid, String authUrl, String invite_token) {
            this.mailSender = mailSender;
            this.uid = uid;
            this.authUrl = authUrl;
            this.invite_token = invite_token;
        }

// ***** 이런 식으로 중복되는 생성자가 있는건 별로인가?
        public SendMailThread(JavaMailSender mailSender, String uid, int idx) {
            this.mailSender = mailSender;
            this.uid = uid;
            this.idx = idx;
        }

        public void run() {
            mailSend.mailsend(mailSender, uid, authUrl);
        }
        public void runToInvite() { mailSend.mailsendToInvite(mailSender, uid, authUrl, invite_token); }
    }

    // <3. 비밀번호 변경>
    public ResponseType changePwd (@RequestBody NewPwd newPwd, final int idx) {

        if(newPwd.getIdx() != idx){
//            System.out.println("idx don't match");
            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Login.getStatus());
        }
        String pwd = newPwd.getPwd();

        try {

            SecureRandom secRan = SecureRandom.getInstance("SHA1PRNG");
            int numLength = 16;
            String newSalt = "";
            for (int i = 0; i < numLength; ++i) {
                newSalt += secRan.nextInt(10);
            }

            String newPassword = newSalt + pwd;
            String hashPassword = (SHA256.getInstance()).encodeSHA256(newPassword);

            if(userMapper.changePwd(hashPassword, newSalt, idx) == 1){
//                System.out.println("password change success");
                return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Change_Pwd.getStatus());
            } else {
//                System.out.println("password change fail");
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Change_Pwd.getStatus());
            }
        } catch (Exception e) {
//            System.out.println("password change fail");
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Change_Pwd.getStatus());
        }
    }


    // <4. 비밀번호 분실 후 변경>
    // 이메일 발송
    public ResponseType missingEmailAuth (@PathVariable final int idx) {

        try {

// *****     if (idx != 헤더 토큰의 idx) {
// *****          return codeJsonParser.codeJsonParser(Status_3000.FAIL_Missing_Pwd_Email_Auth.getStatus());
// *****     }

            String uid = userMapper.getUid(idx);

            SendMailThread sendMailThread = new SendMailThread(mailSender, uid, idx);
            sendMailThread.run();
// ***** mailsend, mailSender, SendMailThread, JavaMailSender 진짜 헷갈린다. 시간나면 한 번 정리할 것
// ***** 우선 sendMailThread.start() 대신, 편의상 sendMailThread.run()을 이용

            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Register_Auth_Mail_Sent.getStatus());
        }
        catch (Exception e) {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register.getStatus());
        }
    }


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