package com.aomc.coop.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.util.Calendar;
import java.util.Date;
import static com.auth0.jwt.JWT.require;

@Slf4j
// The Simple Logging Facade for Java (SLF4J) serves as a simple facade or abstraction for various logging frameworks
// (e.g. java.utils.logging, logback, log4j) allowing the end user to plug in the desired logging framework at deployment time.
@Service
public class JwtService {

    @Value("${JWT.ISSUER}") // JWT.ISSUER에 입력된 값을 가져오는 것
    private String ISSUER;

    @Value("${JWT.SECRET}") // JWT.SECRET에 입력된 값을 가져오는 것
    private String SECRET;

    /**
     * 토큰 생성
     *
     * @param idx 토큰에 담길 로그인한 사용자의 회원 고유 idx
     * @return 토큰
     */
/*
    1. Header
        - Two parts: 1) the type of the token(JWT) 2) the signing algorithm being used, such as HMAC SHA256 or RSA.
    2. Payload
        - Contains the claims. Claims are statements about an entity (typically, the user) and additional data.
    3. Signature
        - The signature is used to verify the message wasn't changed along the way.

    Therefore, a JWT typically looks like the following.
    xxxxx.yyyyy.zzzzz
*/

    public String create(final int idx) {
        try {
            //토큰 생성 빌더 객체 생성
            JWTCreator.Builder b = JWT.create();
            //토큰 생성자 명시
            b.withIssuer(ISSUER);
            //토큰 payload 작성, key - value 형식, 객체도 가능
            b.withClaim("idx", idx);
            //만료날짜지정, 1달로
            b.withExpiresAt(expireAt());
            //토큰 해싱해서 반환
            return b.sign(Algorithm.HMAC256(SECRET));
        } catch (JWTCreationException JwtCreationException) {
            log.info(JwtCreationException.getMessage());
        }
        return null;
    }


    private Date expireAt() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        //한달은 24*31
        cal.add(Calendar.HOUR, 744);
        return  cal.getTime();
    }

    /**
     * 토큰 해독
     *
     * @param token 토큰
     * @return 로그인한 사용자의 회원 고유 idx
     */

    public Token decode(final String token) {

        try {
            //토큰 해독 객체 생성
            final JWTVerifier jwtVerifier = require(Algorithm.HMAC256(SECRET)).withIssuer(ISSUER).build();
            //토큰 검증
            DecodedJWT decodedJWT = jwtVerifier.verify(token);

            //토큰 payload 반환, 정상적인 토큰이라면 토큰 주인(사용자) 고유 ID, 아니라면 -1
            return new Token(decodedJWT.getClaim("idx").asLong().intValue());
        } catch (JWTVerificationException jve) {
            log.error(jve.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new Token();
    }

// *** Token과 TokenRes의 차이는 뭘까? 일단 TokenRes에 모두 담아 사용하였다.
    public static class Token {
        //토큰에 담길 정보 필드
        //초기값을 -1로 설정함으로써 로그인 실패시 -1 반환
        private int idx = -1;
        public Token() { }
        public Token(final int idx) { this.idx = idx; }
        public int getUser_idx() { return idx; }
    }

    //반환될 토큰 Res
    public static class TokenRes {
        //실제 토큰
        private String token;
        private int idx = -1;
        public TokenRes() { }
        public TokenRes(final String token, final int idx) {
            this.token = token;
            this.idx = idx;
        }
        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
        public int getIdx() {
            return idx;
        }
        public void setIdx(int idx) {
            this.idx = idx;
        }
    }
}
