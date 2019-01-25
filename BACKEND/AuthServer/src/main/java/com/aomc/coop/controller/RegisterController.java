package com.aomc.coop.controller;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aomc.coop.model.User;
import com.aomc.coop.service.UserService;
import com.aomc.coop.util.SHA256;

// 보는 순서
// 1. controller : 1) register, 2) login, // 3) admin
// 2. service
// 3. dao
// 그 다음부터는 내 마음대로 보기

@RestController // response를 json으로 주겠다
@RequestMapping("/api")
public class RegisterController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @CrossOrigin // 웹 서버 도메인간 액세스 제어 기능을 제공, 도메인간 데이터 전송을 가능하게 함
    public ResponseEntity<Void> insertUser(@RequestBody User user) throws NoSuchAlgorithmException { // ResponseEntity : Response body와 함께 Http status를 전송하기 위해 사용
        // cf) @ResponseBody : 자바 객체를 Http response body로 변환
        // @RequestBody : Http request body를 java 객체로 변환
        // Annotation : 소스코드에 메타 데이터를 삽입하는 것, 비즈니스 로직과 별도로 시스템 설정과 관련된 사항들을 @에게 위임할 수 있음
        // 개발자는 비즈니스 로직 구현에 집중 가능
        // NoSuchAlgorithmException : This exception is thrown when a particular cryptographic algorithm is requested but is not available in the environment
        // *** <Void>는 이해 안 감
        System.out.println("Register");

        SecureRandom secRan = SecureRandom.getInstance("SHA1PRNG"); //random 문자 길이를 담은 객체
        int numLength = 16;
        String salt = "";
        for (int i = 0; i < numLength; ++i) {
            salt += secRan.nextInt(10);
        }
        System.out.println(salt);

        String newPassword = salt + user.getPassword();
        String hashPassword = (SHA256.getInstance()).encodeSHA256(newPassword);

        user.setSalt(salt);
        user.setPassword(hashPassword);
        if(user.getUserId().equals("admin")) {
            user.setRole("admin_role");
        }else {
            user.setRole("user_role");
        }
        user.setStatus("able");

        if (userService.insertUser(user)) {                              // 1. db에 INSERT 후
            System.out.println("Successfully Registered");
            return new ResponseEntity<Void>(HttpStatus.OK);              // 2. Http Response를 줌 -> *** 이 return 값이 어디로 가는지 확인할 것
        } else {
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);

        }
    }
}
