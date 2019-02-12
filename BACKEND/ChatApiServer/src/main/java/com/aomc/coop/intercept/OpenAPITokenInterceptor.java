package com.aomc.coop.intercept;

import com.aomc.coop.response.Status_1000;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.JwtService;
import com.aomc.coop.service.TokenService;
import com.aomc.coop.utils.CodeJsonParser;
import com.rabbitmq.client.AMQP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class OpenAPITokenInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AMQP.Channel.Open.class);
    private static final String HEADER_AUTH = "X-Auth-Token";

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("in Interceptor");
        String tokenString = request.getHeader(HEADER_AUTH);
        logger.info(tokenString);
        if (tokenString != null) {
//            logger.info(tokenString);
            if (tokenService.isUsableToken(tokenString)) {
                return true;
            }
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
