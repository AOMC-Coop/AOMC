package com.aomc.coop.service;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.aomc.coop.mapper.UserMapper;
import com.aomc.coop.response.Status_3000;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.aomc.coop.model.User;
import com.aomc.coop.util.Http;
import com.aomc.coop.util.SHA256;

@Slf4j
@Service
@Transactional
public class LoginLogoutService {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    @Autowired
    private UserMapper userMapper;

    @Autowired // 이렇게 하는거 맞나?
    private JwtService jwtService;

    @Resource(name="redisTemplate") // @Resource : 일단 @Autowired와 비슷한 것으로 알고 있기
    private HashOperations<String, String, String> hashOperations; // HashOperations : Redis map specific operations working on a hash

    public ResponseType loginUser(@RequestBody User user) throws UnsupportedEncodingException { // header,body(json),HTTP.status //,

        try
        {
            User myUser = userMapper.getUser(user.getUid());

            // 해당 이메일로 가입된 유저가 없는 경우
            if(myUser == null){
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login_Wrong_ID.getStatus());
            }
// *** updateAccess_date(user)가 제대로 되지 않는 경우는 어떤 때일까?
            if(userMapper.updateAccess_date(user.getUid()) == 0) {
                System.out.println("updateAccess_date : Fail");
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login.getStatus());
            }
            String hashPassword = SHA256.getInstance().encodeSHA256(myUser.getSalt() + user.getPwd());
            System.out.println("hashPassword : "+hashPassword);
                // 1. Http request로 들어온 user와 db상의 user가 같다면
            if(hashPassword.equals(myUser.getPwd())) {
                // 2. JWT(JSON Web Tokens) 토큰 생성
//                <윤재 자체 토큰 생성 부분>
//                String token = Base64.encodeBase64String(((SHA256.getInstance().encodeSHA256(myUser.getUid())).getBytes("UTF-8")));
//                token = token.replace("=", "").replace('/', '_').replace('+', '-');
//                System.out.println("token = " + token);

                final JwtService.TokenRes token = new JwtService.TokenRes(jwtService.create(myUser.getIdx()));

                HashMap hashMap = new HashMap();
                hashMap.put("uid", myUser.getUid());
                hashMap.put("nickname", myUser.getNickname());

                hashOperations.putAll(token.getToken(), hashMap);

//                String value = (String)redisTemplate.opsForValue().get(key);
//                Assert.assertEquals("token", value);
//                values.leftPush("key:auth", tokenDto.getToken());

                // 3. redis에 토큰 보내기
                String key = token.getToken();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat( "yy-MM-dd HH:mm:ss" , Locale.KOREA );
                String time = sdf.format( new Date( timestamp.getTime( ) ) );
                System.out.println(Http.getIp() + " " + time);

                HashMap<String, String> map = new HashMap<String, String>();
                // Map : An object that maps keys to values. A map cannot contain duplicate keys; each key can map to at most one value.
                // This interface takes the place of the Dictionary class.
                // HashMap : Hash table based implementation of the Map interface.
                map.put("userId", myUser.getUid());      // put(K key, V value)
                map.put("ip", Http.getIp());
                map.put("timeStamp", time);
                hashOperations.putAll(key, map);
                // key는 redis token, map은 <user id, ip, time>
                //            hashOperations<String, String, String>
                hashOperations.getOperations().expire(token.getToken(), 5L, TimeUnit.MINUTES);

                // test -> 조회해보기
                // String userId = hashOperations.get(token, "userId");
                // String ip = hashOperations.get(token, "ip");
                // String timeStamp = hashOperations.get(token, "timeStamp");

                // System.out.println(userId + " " + ip + " " + timeStamp);

                // 4. 토큰을 client에 보내기
                // myUser.setPwd(token.getToken()); -> 이 부분은 return 파라미터에 token을 담은 것으로 대체
// ***** password 대신 token을 보냄 -> 1. 원래는 JWT 토큰을 바디에 보내야 함  2. 그 다음부터는 헤더에 담아서 통신 -> 다른 service들에 영향 : 은미 코드 보자

//				HttpHeaders headers = new HttpHeaders();
//				headers.add("auth_token", token);
                // 제대로 로그인이 되었다면
                return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Login.getStatus(), token.getToken()); // ,로 파라미터에 token값 넘기기 (String으로)
            } else {
                // Http request로 들어온 user와 db상의 user가 다르다면
                System.out.println("Wrong Password");
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login_Wrong_Password.getStatus());
            }
        }
        catch (Exception e) {
            System.out.println("Catch Error");
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Login.getStatus());
        }
    }

//    public ResponseType logoutUser(@RequestBody User user) {
//
//        try
//        {
//
//        }
//        catch (Exception e) {
//
//        }
//    }
}