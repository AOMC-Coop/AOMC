package com.aomc.coop.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import static com.auth0.jwt.JWT.require;

@Slf4j
@Service
public class JwtService {

    @Value("${JWT.ISSUER}")
    private String ISSUER;

    @Value("${JWT.SECRET}")
    private String SECRET;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> values;

    public boolean isUsableToken(String token) {

        String idx = (String) values.get(token, "idx");

        if(idx == null) return false;
        else return true;
    }

    /**
     * 토큰 생성
     *
     * @param idx 토큰에 담길 로그인한 사용자의 회원 고유 idx
     * @return 토큰
     */

    public String create(final int idx) {
        try {
            JWTCreator.Builder b = JWT.create();
            b.withIssuer(ISSUER);
            b.withClaim("idx", idx);
            b.withExpiresAt(expireAt());
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
            final JWTVerifier jwtVerifier = require(Algorithm.HMAC256(SECRET)).withIssuer(ISSUER).build();
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
