//package com.aomc.coop.utils.auth;
//
//import com.aomc.coop.service.JwtService;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//
//
//@Slf4j
//@Component
//@Aspect
//public class AuthAspect {
//
//    private final static String AUTHORIZATION = "Authorization";
//
//    /**
//     * 실패 시 기본 반환 Response
//     */
////    private final static DefaultRes DEFAULT_RES = DefaultRes.builder().status(401).message("인증 실패").build();
////    private final static ResponseEntity<DefaultRes> RES_RESPONSE_ENTITY = new ResponseEntity<>(DEFAULT_RES, HttpStatus.UNAUTHORIZED);
//    private final HttpServletRequest httpServletRequest;
//    private final UserMapper userMapper;
//    private final JwtService jwtService;
//
//    public AuthAspect(final HttpServletRequest httpServletRequest, final UserMapper userMapper, final JwtService jwtService) {
//        this.httpServletRequest = httpServletRequest;
//        this.userMapper = userMapper;
//        this.jwtService = jwtService;
//    }
//
//
//    //@annotation 패키지 이름을 실제 사용할 annotation 경로로 맞춰줘야 함.
//    @Around("@annotation(com.aomc.coop.utils.auth.Auth)")
//    public Object auth(final ProceedingJoinPoint pjp) throws Throwable {
//        final String jwt = httpServletRequest.getHeader("token");
//        //토큰 존재 여부 확인
//        if (jwt == null){
//            System.out.println("jwt없음");
//            return RES_RESPONSE_ENTITY;
//        }
//        //토큰 해독
//        final JwtService.Token token = jwtService.decode(jwt);
//        //토큰 검사
//        if (token == null) {
//            System.out.println("토큰없음");
//            return RES_RESPONSE_ENTITY;
//        } else {
//            final User user = userMapper.findByUserIdx(token.getUser_idx());
//            //유효 사용자 검사
//            if (user == null){
//                System.out.println("사용자없음");
//                return RES_RESPONSE_ENTITY;
//            }
//
//            return pjp.proceed(pjp.getArgs());
//
//        }
//
//    }
//}