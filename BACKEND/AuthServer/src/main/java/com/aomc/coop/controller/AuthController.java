//package com.aomc.coop.controller;
//
//import com.aomc.coop.model.User;
//import com.aomc.coop.service.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import com.aomc.coop.utils.CodeJsonParser;
//import java.security.NoSuchAlgorithmException;
//
//// ***** 얘를 login.vue에 맞게 고쳐야 함
//@RestController
//// response를 json으로 주겠다
//// Spring 4 에서 Rest API 또는 Web API를 개발하기 위해 등장한 애노테이션.
//// 이전 버전의 @Controller와 @ResponseBody를 포함합니다.
//// 컨트롤러의 메소드에서는 JSON으로 변환될 객체를 반환합니다.
//// jackson 라이브러리를 추가할 경우 객체를 JSON으로 변환하는 메시지 컨버터가 사용되도록 @EnableWebMvc에서 기본으로 설정되어 있습니다.
//// jackson 라이브러리를 추가하지 않으면 JSON메시지로 변환할 수 없어 500오류가 발생합니다.
//// 사용자가 임의의 메시지 컨버터(MessageConverter)를 사용하도록 하려면 WebMvcConfigurerAdapter의 configureMessageConverters메소드를 오버라이딩 하도록 합니다.
//@RequestMapping("/api")
//public class AuthController {
//
//    // CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();
//    // @Autowired : 주입 대상이되는 bean을 컨테이너에 찾아 주입하는 어노테이션
//
//    @Autowired private JwtService jwtService;
//}
