package com.aomc.coop.service;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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


    public ResponseType register(@RequestBody User user) {

        try {

// *** DB 연산을 줄이기 위해 Mapper 함수 변경함
            String uid = user.getUid();
            String myUser = userMapper.checkUser(uid);

            // db상에 이미 가입되어 있는 내역이 없는 경우, 정상 절차대로 가입
            if (myUser == null) {

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
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register_Duplicate.getStatus());
            }
        }
        catch (Exception e) {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register.getStatus());
        }
    }

// *** 이메일 인증 성공시 바로 로그인 창으로 넘어가도록 구조를 바꿔보자
    public String emailAuth(final String authUrl, String invite_token) {

        try {

            Map userInfo = hashOperations.entries(authUrl);
            String uid = (String) userInfo.get("uid");

            if(userMapper.getUserWithUid(uid) != null ) {
// *** 적당한 예외처리를 찾지 못해서 일단 같은 URL로 박아둠
                return "http://localhost:9999";
//                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register_Duplicate.getStatus());
            }

            if(userInfo == null) {
// *** 적당한 예외처리를 찾지 못해서 일단 같은 URL로 박아둠
                return "http://localhost:9999";
//                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register_Timeout.getStatus());

            } else {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat( "yy-MM-dd HH:mm:ss" , Locale.KOREA );


                String pwd = (String) userInfo.get("pwd");
                String salt = (String) userInfo.get("salt");
                String nickname = (String) userInfo.get("nickname");
                String image = "https://avatars0.githubusercontent.com/u/28426269?s=460&v=4";

                User user = new User();
                user.setUid(uid);
                user.setPwd(pwd);
                user.setSalt(salt);
                user.setNickname(nickname);
                user.setImage(image);

                String check = invite_token;
                // 제대로 db에 저장이 되었다면
                if (userMapper.insertUser(user) == 1) {
//                    return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Register.getStatus());
                } else {
                    // 어떠한 이유로 db에 저장이 되지 못했다면
// *** 적당한 예외처리를 찾지 못해서 일단 같은 URL로 박아둠
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

// *** user has team에 정보 넣어주고, user has channel에도 넣어줘야 함 (redis에서 찾아서 할 것), teamservice의 356줄 flag를 1로 해서 저장 (2개 다))

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

// *** 이런 식으로 중복되는 생성자가 있는건 별로인가?
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


    public ResponseType changePwd (@RequestBody NewPwd newPwd, final int idx) {

        if(newPwd.getIdx() != idx){
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
                return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Change_Pwd.getStatus());
            } else {
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Change_Pwd.getStatus());
            }
        } catch (Exception e) {
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