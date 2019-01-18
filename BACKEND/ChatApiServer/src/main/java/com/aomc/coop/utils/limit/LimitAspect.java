//package com.aomc.coop.utils.limit;
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
//@Slf4j
//@Component
//@Aspect
//public class LimitAspect {
//    private final static DefaultRes DEFAULT_RES = DefaultRes.builder().status(401).message("인증 실패").build();
//    private final static ResponseEntity<DefaultRes> RES_RESPONSE_ENTITY = new ResponseEntity<>(DEFAULT_RES, HttpStatus.UNAUTHORIZED);
//
//    private final static DefaultRes FAIL_RES = DefaultRes.builder().status(401).message("권한 없음").build();
//    private final static ResponseEntity<DefaultRes> FAIL_RESPONSE_ENTITY = new ResponseEntity<>(FAIL_RES, HttpStatus.UNAUTHORIZED);
//
//    private final HttpServletRequest httpServletRequest;
//    private final UserMapper userMapper;
//    private final JwtService jwtService;
//
//    public LimitAspect(final HttpServletRequest httpServletRequest, final UserMapper userMapper, final JwtService jwtService) {
//        this.httpServletRequest = httpServletRequest;
//        this.userMapper = userMapper;
//        this.jwtService = jwtService;
//    }
//
//    @Around("@annotation(com.aomc.coop.utils.limit.Limit)")
//    public Object checkGrade(final ProceedingJoinPoint pjp) throws Throwable {
//        final String jwt = httpServletRequest.getHeader("token");
//
//        //토큰 해독
//        final JwtService.Token token = jwtService.decode(jwt);
//        //토큰 검사
//        if (token == null) {
//            System.out.println("토큰없음");
//            return RES_RESPONSE_ENTITY;
//        } else {
//            final User user = userMapper.findByUserIdx(token.getUser_idx());
//            //유효 사용자 검사
//            if (user.getUser_grade().equals("user")) {
//                System.out.println("접근할 수 없는 사용자입니다");
//                return FAIL_RESPONSE_ENTITY;
//            }
//
//            return pjp.proceed(pjp.getArgs());
//        }
//    }
//}
