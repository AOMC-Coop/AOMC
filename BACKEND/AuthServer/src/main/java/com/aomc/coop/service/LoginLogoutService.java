package com.aomc.coop.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;

import com.aomc.coop.mapper.UserMapper;
import com.aomc.coop.response.Status_3000;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.aomc.coop.model.User;
import com.aomc.coop.utils.Http;
import com.aomc.coop.utils.SHA256;

@Slf4j
@Service
@Transactional
public class LoginLogoutService {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtService jwtService;

    @Resource(name="redisTemplate")
    private HashOperations<String, String, Object> hashOperations;

    public ResponseType loginUser(@RequestBody User user) {

        try
        {
            String uid = user.getUid();
            User myUser = userMapper.getUserWithUid(uid);

            // 해당 이메일로 가입된 유저가 없는 경우
            if(myUser == null){
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login_Wrong_ID.getStatus());
            }

            // 탈퇴한 회원일 경우
            if(myUser.getStatus() == 0){
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login_Withdrawal.getStatus());
            }
            String hashPassword = SHA256.getInstance().encodeSHA256(myUser.getSalt() + user.getPwd());

            // 1. Http request로 들어온 user와 db상의 user가 같다면
            if(hashPassword.equals(myUser.getPwd())) {
                final JwtService.TokenRes token = new JwtService.TokenRes(jwtService.create(myUser.getIdx()), myUser.getIdx());

                String key = token.getToken();

                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat( "yy-MM-dd HH:mm:ss" , Locale.KOREA );
                String time = sdf.format( new Date( timestamp.getTime( ) ) );

                HashMap<String, Object> hashMap = new HashMap<>();

                hashMap.put("idx", myUser.getIdx()); // put(K key, V value)
                hashMap.put("uid", uid);
                hashMap.put("nickname", myUser.getNickname());
                hashMap.put("ip", Http.getIp());
                hashMap.put("timeStamp", time);
                hashOperations.putAll(key, hashMap);

                // *** 30분이 지나면 "다시 로그인 하셔야 합니다."와 같은 메시지가 팝업되며 로그인 페이지로 이동시킬 것 -> vue.js에서 해야 함
                hashOperations.getOperations().expire(key, 30L, TimeUnit.MINUTES);

                // *** 토큰을 header에 실어서 client에 보내기
				// HttpHeaders headers = new HttpHeaders();
				// headers.add("auth_token", token);

                // 제대로 로그인이 되었다면
                User responseUser = new User();
                responseUser.setIdx(myUser.getIdx());
                responseUser.setUid(uid);
                responseUser.setImage(myUser.getImage());
                responseUser.setNickname(myUser.getNickname());

                return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Login.getStatus(), responseUser);
            } else {
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login_Wrong_Password.getStatus());
            }
        }
        catch (Exception e) {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login.getStatus());
        }
    }

    public ResponseType logoutUser(@RequestBody User user) {

        try
        {
            // *** String token;
            hashOperations.getOperations().delete(token);
            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Logout.getStatus());
        }
        catch (Exception e) {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Logout.getStatus());
        }
    }

}