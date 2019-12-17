package com.aomc.coop.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;

import com.aomc.coop.domain.User;
import com.aomc.coop.dto.LoginRequest;
import com.aomc.coop.dto.LoginResponse;
import com.aomc.coop.mapper.UserMapper;
import com.aomc.coop.repository.UserRepository;
import com.aomc.coop.response.Status_3000;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.aomc.coop.utils.Http;
import com.aomc.coop.utils.SHA256;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginLogoutService {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Resource(name="redisTemplate")
    private HashOperations<String, String, Object> hashOperations;

    public ResponseType loginUser(@RequestBody LoginRequest loginRequest) {

        String uid = loginRequest.getUid();
        // findByUid 함수에 Optional<User>를 적용해보자.
        User myUser = userRepository.findByUid(uid);
        // User myUser = userMapper.getUserWithUid(uid);

        if(myUser == null) {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login_Wrong_ID.getStatus());
        }

        if(myUser.getStatus() == 0) {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login_Withdrawal.getStatus());
        }
        String hashPassword = SHA256.getInstance().encodeSHA256(myUser.getSalt() + loginRequest.getPwd());

        if(hashPassword.equals(myUser.getPwd())) {
            final JwtService.TokenRes token = new JwtService.TokenRes(jwtService.create(myUser.getIdx()), myUser.getIdx());

            String key = token.getToken();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat( "yy-MM-dd HH:mm:ss" , Locale.KOREA );
            String time = sdf.format( new Date( timestamp.getTime( ) ) );

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("idx", myUser.getIdx());
            hashMap.put("uid", uid);
            hashMap.put("nickname", myUser.getNickname());
            hashMap.put("ip", Http.getIp());
            hashMap.put("timeStamp", time);
            hashMap.put("image_url", myUser.getImage());
            hashOperations.putAll(key, hashMap);

            hashOperations.getOperations().expire(key, 30L, TimeUnit.MINUTES);

            HttpHeaders headers = new HttpHeaders();
            headers.add("auth_token", key);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setIdx(myUser.getIdx());
            loginResponse.setUid(uid);
            loginResponse.setImage(myUser.getImage());
            loginResponse.setNickname(myUser.getNickname());

            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Login.getStatus(), loginResponse, headers);
        }
        return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login_Wrong_Password.getStatus());
    }

    public ResponseType logoutUser(String token) {
        Boolean res = hashOperations.getOperations().delete(token);
        if(res == true){
            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Logout.getStatus());
        }
        return codeJsonParser.codeJsonParser(Status_3000.FAIL_Logout.getStatus());
    }
}