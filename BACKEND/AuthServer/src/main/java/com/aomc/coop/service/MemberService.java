package com.aomc.coop.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.aomc.coop.mapper.UserMapper;
import com.aomc.coop.model.UserWithToken;
import com.aomc.coop.response.Status_3000;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.aomc.coop.model.User;
import com.aomc.coop.util.SHA256;

@Slf4j
@Service
@Transactional
public class MemberService {

    // 접근 진행 순서 : Controller -> Service -> Mapper
    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    @Autowired
    private UserMapper userMapper;

    // 회원 가입
// *** 여기선 try...catch를 안 썼다. 그래도 되는걸까?
    public ResponseType register(@RequestBody User user) throws NoSuchAlgorithmException {

        String uid = user.getUid();
        User myUser = userMapper.getUserWithUid(uid);
        // db상에 이미 가입되어 있는 내역이 없는 경우, 정상 절차대로 가입
        if(myUser == null) {
            // 1. (@RequestBody User user)가 Http request를 자바 객체 User로 변환시켜줌
            System.out.println("Register");

            // 2. random 문자열을 통해 salt 생성
            SecureRandom secRan = SecureRandom.getInstance("SHA1PRNG"); //random 문자 길이를 담은 객체
            int numLength = 16;
            String salt = "";
            for (int i = 0; i < numLength; ++i) {
                salt += secRan.nextInt(10);
            }
            System.out.println("salt : "+ salt);

            // 3. salt를 통해 hash된 password를 구하기
            String newPassword = salt + user.getPwd();
            String hashPassword = (SHA256.getInstance()).encodeSHA256(newPassword);

            // 4. salt와 hash된 password를 user 데이터에 저장, user role까지 설정
            user.setSalt(salt);
            user.setPwd(hashPassword);
            if(user.getUid().equals("admin")) { user.setRole("admin_role"); }
            else { user.setRole("user_role"); }

            // 5. 유저가 탈퇴하면 0으로 설정하기 때문에 필요
            user.setStatus(1);

            // 6. 정보 입력을 끝내고 user를 db에 INSERT
            if (userMapper.insertUser(user) == 1) {
                System.out.println("Successfully Registered");
            // 7. Status_3000 response를 줌
            // 제대로 db에 저장이 되었다면
                return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Register.getStatus());
// 이 return 값은, ex) Chat.vue -> created().get.then.(response)의 response로 감 // 토큰도 ResponseEntity에 담아서 보낼것
            } else {
            // 어떠한 이유로 db에 저장이 되지 못했다면
                return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register.getStatus());
            }
        } else {
        // 이미 가입되어 있는 uid(e-mail)이라면
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Register_Duplicate.getStatus());
        }
    }

    // 회원 탈퇴
    public ResponseType withdrawal(@RequestBody UserWithToken userWithToken, int idx){

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