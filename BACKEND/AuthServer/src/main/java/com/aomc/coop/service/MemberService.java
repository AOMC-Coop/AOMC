package com.aomc.coop.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.aomc.coop.dto.RegisterRequest;
import com.aomc.coop.dto.User;
import com.aomc.coop.dto.WithdrawalRequest;
import com.aomc.coop.mapper.*;
import com.aomc.coop.dto.NewPasswordRequest;
import com.aomc.coop.response.Status_3000;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import com.aomc.coop.utils.mail.MailSend;

import com.aomc.coop.utils.SHA256;

import javax.annotation.Resource;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    private final UserMapper userMapper;
    private final TeamMapper teamMapper;
    private final ChannelMapper channelMapper;
    private final MailSend mailSend;
    private final JavaMailSender mailSender;

    @Resource(name="redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    public ResponseType register(@RequestBody RegisterRequest registerRequest) throws NoSuchAlgorithmException {

        String uid = registerRequest.getUid();
        String myUser = userMapper.checkUser(uid);

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

            String newPassword = salt + registerRequest.getPwd();
            String hashPassword = (SHA256.getInstance()).encodeSHA256(newPassword);
            String nickname = registerRequest.getNickname();

            hashMap.put("uid", uid);
            hashMap.put("pwd", hashPassword);
            hashMap.put("salt", salt);
            hashMap.put("nickname", nickname);

            hashOperations.putAll(authUrl, hashMap);
            hashOperations.getOperations().expire(authUrl, 5L, TimeUnit.HOURS);

            String invite_token = registerRequest.getInvite_token();

            if(invite_token == null) {
                SendMailThread sendMailThread = new SendMailThread(mailSender, uid, authUrl);
                sendMailThread.run();
            } else {
                SendMailThread sendMailThread = new SendMailThread(mailSender, uid, authUrl, invite_token);
                sendMailThread.runToInvite();
            }
            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Register_Auth_Mail_Sent.getStatus());
        }

        return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register_Duplicate.getStatus());
    }

// *** 이메일 인증 성공시 바로 로그인 창으로 넘어가도록 구조를 바꿔보자
    public String emailAuth(final String authUrl, String invite_token) {

        Map userInfo = hashOperations.entries(authUrl);
        String uid = (String) userInfo.get("uid");

        if(userMapper.getUserWithUid(uid) != null ) {
// *** 적당한 예외처리를 찾지 못해서 일단 같은 URL로 박아둠
            return "http://localhost:9999";
//            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register_Duplicate.getStatus());
        }

        if(userInfo == null) {
// *** 적당한 예외처리를 찾지 못해서 일단 같은 URL로 박아둠
            return "http://localhost:9999";
//            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register_Timeout.getStatus());
        } else {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat( "yy-MM-dd HH:mm:ss" , Locale.KOREA );

            String pwd = (String) userInfo.get("pwd");
            String salt = (String) userInfo.get("salt");
            String nickname = (String) userInfo.get("nickname");

            // JPA로 수정해야 하는 부분 : userRequest, userMapper.insertUser(userRequest)
            User user = new User();
            user.setUid(uid);
            user.setPwd(pwd);
            user.setSalt(salt);
            user.setNickname(nickname);

            // 제대로 db에 저장이 되었다면
            if (userMapper.insertUser(user) == 1) {
//                return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Register.getStatus());
            } else {
                // 어떠한 이유로 db에 저장이 되지 못했다면
// *** 적당한 예외처리를 찾지 못해서 일단 같은 URL로 박아둠
//                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register.getStatus());
            }
            int user_idx = user.getIdx();

            // 초대로 회원가입한 유저인 경우
            if(!"invitation".equals(invite_token)){

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

    // <2. 회원 탈퇴>
    public ResponseType withdrawal(@RequestBody WithdrawalRequest withdrawalRequest, final int idx) {

        int userIdx = withdrawalRequest.getIdx();

        if(userIdx == idx) {
            userMapper.withdrawal(idx);
            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Withdrawal.getStatus());
        }

        return codeJsonParser.codeJsonParser(Status_3000.FAIL_Withdrawal.getStatus());
    }

    public ResponseType changePwd (@RequestBody NewPasswordRequest newPasswordRequest, final int idx) throws NoSuchAlgorithmException{

        if(newPasswordRequest.getIdx() != idx) {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Change_Pwd_Wrong_Idx.getStatus());
        }

        if(newPasswordRequest.getPwd() != newPasswordRequest.getConfirm_pwd()) {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Change_Pwd_Wrong_Confirm_Pwd.getStatus());
        }

        String pwd = newPasswordRequest.getPwd();

        SecureRandom secRan = SecureRandom.getInstance("SHA1PRNG");
        int numLength = 16;
        String newSalt = "";
        for (int i = 0; i < numLength; ++i) {
            newSalt += secRan.nextInt(10);
        }

        String newPassword = newSalt + pwd;
        String hashPassword = (SHA256.getInstance()).encodeSHA256(newPassword);

        if(userMapper.changePwd(hashPassword, newSalt, idx) == 1) {
            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Change_Pwd.getStatus());
        }

        return codeJsonParser.codeJsonParser(Status_3000.FAIL_Change_Pwd.getStatus());
    }


    // <4. 비밀번호 분실 후 변경>
    // 이메일 발송
    public ResponseType missingEmailAuth (@PathVariable final int idx) {

        String uid = userMapper.getUid(idx);
        SendMailThread sendMailThread = new SendMailThread(mailSender, uid, idx);
        sendMailThread.run();

        return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Register_Auth_Mail_Sent.getStatus());
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

        public SendMailThread(JavaMailSender mailSender, String uid, int idx) {
            this.mailSender = mailSender;
            this.uid = uid;
            this.idx = idx;
        }

        public SendMailThread(JavaMailSender mailSender, String uid, String authUrl, String invite_token) {
            this.mailSender = mailSender;
            this.uid = uid;
            this.authUrl = authUrl;
            this.invite_token = invite_token;
        }

        public void run() {
            mailSend.mailsend(mailSender, uid, authUrl);
        }
        public void runToInvite() { mailSend.mailsendToInvite(mailSender, uid, authUrl, invite_token); }
    }
}