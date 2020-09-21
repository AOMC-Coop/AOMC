package com.aomc.coop.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.aomc.coop.domain.*;
import com.aomc.coop.dto.RegisterRequest;
import com.aomc.coop.dto.WithdrawalRequest;
import com.aomc.coop.mapper.*;
import com.aomc.coop.dto.NewPasswordRequest;
import com.aomc.coop.repository.*;
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

    private final UserRepository userRepository;
    private final UserHasTeamRepository userHasTeamRepository;
    private final UserHasChannelRepository userHasChannelRepository;
    private final TeamRepository teamRepository;
    private final ChannelRepository channelRepository;

    private final MailSend mailSend;
    private final JavaMailSender mailSender;

    @Resource(name="redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    public ResponseType register(@RequestBody RegisterRequest registerRequest) throws NoSuchAlgorithmException {

        String uid = registerRequest.getUid();

        User myUser = userRepository.findByUid(uid);

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

    public String emailAuth(final String authUrl, String invite_token) {

        Map userInfo = hashOperations.entries(authUrl);
        String uid = (String) userInfo.get("uid");

        if(userRepository.findByUid(uid) != null ) {
            return "http://localhost:9999";
        }

        if(userInfo == null) {
            return "http://localhost:9999";
        } else {

            String pwd = (String) userInfo.get("pwd");
            String salt = (String) userInfo.get("salt");
            String nickname = (String) userInfo.get("nickname");
            String imageUrl = "https://avatars0.githubusercontent.com/u/28426269?s=460&v=4";

            User user = userRepository.save(User.builder()
                                            .uid(uid)
                                            .pwd(pwd)
                                            .salt(salt)
                                            .nickname(nickname)
                                            .role(0)
                                            .status(1)
                                            .image(imageUrl)
                                            .build());

            // 초대로 회원가입한 유저인 경우
            if(!"invitation".equals(invite_token)){

                Map invitedUserInfo = hashOperations.entries(invite_token);

                Integer team_idx = (Integer) invitedUserInfo.get("teamIdx");
                Team team = teamRepository.findById(team_idx).get();

                Integer channel_idx = (Integer) invitedUserInfo.get("channelIdx");
                Channel channel = channelRepository.findById(channel_idx).get();

                userHasTeamRepository.save(UserHasTeam.builder()
                                                    .team(team)
                                                    .user(user)
                                                    .owner_flag(0)
                                                    .status(1)
                                                    .invite_flag(1)
                                                    .build());

                userHasChannelRepository.save(UserHasChannel.builder()
                                                            .channel(channel)
                                                            .user(user)
                                                            .star_flag(0)
                                                            .status(1)
                                                            .build());
            }
            return "http://localhost:9999";
        }
    }

    // <2. 회원 탈퇴>
    public ResponseType withdrawal(@RequestBody WithdrawalRequest withdrawalRequest, final int idx) {

        int userIdx = withdrawalRequest.getIdx();

        if(userIdx == idx) {
            User user = userRepository.findById(idx).get();
            user.withdrawalMyUser(withdrawalRequest);

            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Withdrawal.getStatus());
        }

        return codeJsonParser.codeJsonParser(Status_3000.FAIL_Withdrawal.getStatus());
    }

    public ResponseType changePwd (@RequestBody NewPasswordRequest newPasswordRequest, final int idx) throws NoSuchAlgorithmException{

        if(newPasswordRequest.getIdx() != idx) {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Change_Pwd_Wrong_Idx.getStatus());
        }

        if(!newPasswordRequest.getPwd().equals(newPasswordRequest.getConfirm_pwd())) {
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

        User user = userRepository.findById(idx).get();
        user.changePassword(hashPassword, newSalt);

        return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Change_Pwd.getStatus());
    }


    // <4. 비밀번호 분실 후 변경>
    // 이메일 발송
    public ResponseType missingEmailAuth (@PathVariable final int idx) {

        User user = userRepository.findById(idx).get();
        String uid = user.getUid();
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
