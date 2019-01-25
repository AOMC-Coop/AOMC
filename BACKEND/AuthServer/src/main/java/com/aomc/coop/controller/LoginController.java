package com.aomc.coop.controller;

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

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.aomc.coop.model.User;
import com.aomc.coop.service.UserService;
import com.aomc.coop.util.Http;
import com.aomc.coop.util.SHA256;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @Resource(name="redisTemplate") // @Resource : 일단 @Autowired와 비슷한 것으로 알고 있기
    private HashOperations<String, String, String> hashOperations; // HashOperations : Redis map specific operations working on a hash

    // Login a User
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity<User> loginUser(@RequestBody User user) throws UnsupportedEncodingException { // header,body(json),HTTP.status //,

        System.out.println("Login 1111");
        try {
            User myUser = userService.getUser(user.getUserId());
            if(!userService.updateAccess_date(user)) { // *** updateAccess_date(user)가 제대로 되지 않는 경우는 어떤 때일까?
                return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
            }

            String hashPassword = SHA256.getInstance().encodeSHA256(myUser.getSalt() + user.getPassword());
            if(hashPassword.equals(myUser.getPassword())) { // Http request로 들어온 user와 db상의 user가 같다면
                System.out.println("Login 2222");
                // 토큰 생성해서 -> JWT 토큰 생성으로 바꿔야 함 호환떄문에 *** 은미 코드 보자
                String token = Base64.encodeBase64String(((SHA256.getInstance().encodeSHA256(myUser.getName())).getBytes("UTF-8")));
                token = token.replace("=", "").replace('/', '_').replace('+', '-');
                System.out.println("token = " + token);

                // redis에 토큰 보내기
                String key = token;
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat( "yy-MM-dd HH:mm:ss" , Locale.KOREA );
                String time = sdf.format( new Date( timestamp.getTime( ) ) );

                System.out.println(Http.getIp() + " " + time);

                HashMap<String, String> map = new HashMap<String, String>();   // Map : An object that maps keys to values. A map cannot contain duplicate keys; each key can map to at most one value.
                //        This interface takes the place of the Dictionary class.
                // HashMap : Hash table based implementation of the Map interface.
                map.put("userId", myUser.getUserId());      // put(K key, V value)
                map.put("ip", Http.getIp());
                map.put("timeStamp", time);
                hashOperations.putAll(key, map); // *** 연구 : key는 redis token, map은 user id + ip + timeStamp
                hashOperations.getOperations().expire(token, 5L, TimeUnit.MINUTES);

                // test -> 조회해보기
                String userId = hashOperations.get(token, "userId");
                String ip = hashOperations.get(token, "ip");
                String timeStamp = hashOperations.get(token, "timeStamp");

                System.out.println(userId + " " + ip + " " + timeStamp);

                // 토큰을 client에 보내기
                myUser.setPassword(token); // *** password 대신 token을 보냄 -> 1. 원래는 JWT 토큰을 바디에 보내야 함  2. 그 다음부터는 헤더에 담아서 통신 : 은미 코드 보자

//				HttpHeaders headers = new HttpHeaders();
//				headers.add("auth_token", token);

                return new ResponseEntity<User>(myUser, HttpStatus.OK);
            }else { // Http request로 들어온 user와 db상의 user가 다르다면
                return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
            }

        } catch (Exception e) {
            return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
        }

    }

}
