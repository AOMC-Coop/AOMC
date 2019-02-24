package com.aomc.coop.utils.auth;

import com.aomc.coop.mapper.UserMapper;
import com.aomc.coop.model.User;
import com.aomc.coop.response.Status_3000;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.JwtService;
import com.aomc.coop.utils.CodeJsonParser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
@Aspect
public class AuthAspect {

    /**
     * 실패 시 기본 반환 Response
     */
    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();
    private final HttpServletRequest httpServletRequest;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    /**
     * Repository 의존성 주입
     */
    public AuthAspect(final HttpServletRequest httpServletRequest, final UserMapper userMapper, final JwtService jwtService) {
        this.httpServletRequest = httpServletRequest;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }


    /**
     * 토큰 유효성 검사
     * @param pjp
     * @return
     * @throws Throwable
     */

    //항상 @annotation 패키지 이름을 실제 사용할 annotation 경로로 맞춰줘야 한다.

    @Around("@annotation(com.aomc.coop.utils.auth.Auth)")
    public Object around(final ProceedingJoinPoint pjp) throws Throwable {
        final String jwt = httpServletRequest.getHeader("token");
        //토큰 존재 여부 확인
        if (jwt == null){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.UNAUTHORIZED.getStatus()), HttpStatus.UNAUTHORIZED);
        }
        //토큰 해독
        final JwtService.Token token = jwtService.decode(jwt);
        //토큰 검사
        if (token == null) {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.UNAUTHORIZED.getStatus()), HttpStatus.UNAUTHORIZED);
        } else {
            final String uid = userMapper.getUid(token.getUser_idx());
// ***** 유효 사용자 검사 -> 속도 향상을 위해 user에서 uid만 찾는 것으로 userMapper를 변경함
            if (uid == null){
                return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.UNAUTHORIZED.getStatus()), HttpStatus.UNAUTHORIZED);
            }

            return pjp.proceed(pjp.getArgs());

        }

    }
}
