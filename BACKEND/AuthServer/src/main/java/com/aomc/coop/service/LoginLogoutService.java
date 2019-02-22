package com.aomc.coop.service;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;

import com.aomc.coop.mapper.UserMapper;
import com.aomc.coop.model.UserWithToken;
import com.aomc.coop.response.Status_3000;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
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
//@Transactional
public class LoginLogoutService {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtService jwtService;

    @Resource(name="redisTemplate") // @Resource : 일단 @Autowired와 비슷한 것으로 알고 있기
    private HashOperations<String, String, Object> hashOperations;

    // HashOperations : Redis map specific operations working on a hash
    // HashOperations <H,HK,HV>
// *** 모든 User 정보를 넣기 위해 String -> Object로 바꾸었는데, Side effect 없겠지?
    // Code Refactoring : Generics를 사용해야 할 것 같아


    public ResponseType loginUser(@RequestBody User user) throws UnsupportedEncodingException { // header,body(json),HTTP.status //,

        try
        {
            String uid = user.getUid();
//            System.out.println("1 " + uid);
            User myUser = userMapper.getUserWithUid(uid);
//            System.out.println("2 " + myUser);

            // 해당 이메일로 가입된 유저가 없는 경우
            if(myUser == null){
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login_Wrong_ID.getStatus());
            }
// *** updateAccess_date(user)가 제대로 되지 않는 경우는 어떤 때일까?
//            if(userMapper.updateAccess_date(user.getUid()) == 0) {
//                System.out.println("updateAccess_date : Fail");
//                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login.getStatus());
//            }

            // 탈퇴한 회원일 경우
// *** 추후 Status_3000 수정하자 -> 더 자세히 경우 나누기
            if(myUser.getStatus() == 0){
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login.getStatus());
            }
            String hashPassword = SHA256.getInstance().encodeSHA256(myUser.getSalt() + user.getPwd());
//            System.out.println("hashPassword : "+hashPassword);
                // 1. Http request로 들어온 user와 db상의 user가 같다면
            if(hashPassword.equals(myUser.getPwd())) {
                // 2. JWT(JSON Web Tokens) 토큰 생성
                final JwtService.TokenRes token = new JwtService.TokenRes(jwtService.create(myUser.getIdx()), myUser.getIdx());

                // 3. redis에 토큰 보내기
                String key = token.getToken();
//                System.out.println("token : " + key);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat( "yy-MM-dd HH:mm:ss" , Locale.KOREA );
                String time = sdf.format( new Date( timestamp.getTime( ) ) );

                // Code Refactoring : 필요없는 User 정보는 redis에 저장하지 말자
                HashMap<String, Object> hashMap = new HashMap<>();
// *** 모든 User 정보를 넣기 위해 String -> Object로 바꾸었는데, Side effect 없겠지?
                hashMap.put("idx", myUser.getIdx()); // put(K key, V value)
                hashMap.put("uid", myUser.getUid());
                hashMap.put("pwd", myUser.getPwd());
                hashMap.put("salt", myUser.getSalt());
                hashMap.put("nickname", myUser.getNickname());
                hashMap.put("role", myUser.getRole());
                hashMap.put("gender", myUser.getGender());
                hashMap.put("status", myUser.getStatus());
                hashMap.put("reg_date", myUser.getReg_date());
                hashMap.put("access_date", myUser.getAccess_date());
                hashMap.put("update_date", myUser.getUpdate_date());
                hashMap.put("ip", Http.getIp());
                hashMap.put("timeStamp", time);
                hashOperations.putAll(key, hashMap);
                // hashOperations  <String,            String,     String>
                //                 <   H,                HK,         HV  >
                //                 [ key  ]            [      map       ]
                //                  token             "uid",    myUser.getUid()
                //                                    "ip",        Http.getIp()
                //                                    "timeStamp", time]
                //                 ...                ...

                // 테스트를 위해 토큰 유지 시간을 5시간으로 바꿈
                hashOperations.getOperations().expire(key, 24L, TimeUnit.HOURS);

                // <test>
                // Object userId = hashOperations.get(key, "uid");
                // Object ip = hashOperations.get(key, "ip");
                // Object timeStamp = hashOperations.get(key, "timeStamp");
                // Object role = hashOperations.get(key, "role");
                // Object access_date = hashOperations.get(key, "access_date");

                // System.out.println(userId + " " + ip + " " + timeStamp + " " + role + " " + access_date);

                // 4. 토큰을 client에 보내기
//				HttpHeaders headers = new HttpHeaders();
//				headers.add("auth_token", token);

                // 제대로 로그인이 되었다면
//                System.out.println("Successfully login!");
                UserWithToken userWithToken = new UserWithToken();
                userWithToken.setIdx(myUser.getIdx());
                userWithToken.setUid(myUser.getUid());
                userWithToken.setToken(key);
                userWithToken.setImage(myUser.getImage());
                userWithToken.setNickname(myUser.getNickname());

                return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Login.getStatus(), userWithToken); // ,로 파라미터에 token 객체 넘기기 (token String, idx)
            } else {
                // Http request로 들어온 user와 db상의 user가 다르다면
//                System.out.println("Wrong Password");
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login_Wrong_Password.getStatus());
            }
        }
        catch (Exception e) {
//            System.out.println("Catch Error");
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login.getStatus());
        }
    }

    // So, the first thing to do when logging out is
    // just to delete the token you stored on the client (e.i. browser local storage).
    // In that case, the client won’t have a token to put in the request, thus causing unauthorized response status.
    public ResponseType logoutUser(@RequestBody UserWithToken userWithToken) {

        try
        {
            String key = userWithToken.getToken();

            // redis token pop
            hashOperations.getOperations().delete(key);
//            System.out.println("Successfully logout!");
            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Logout.getStatus());
        }
        catch (Exception e) {
            System.out.println("Catch Error");
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Logout.getStatus());
        }
    }


}