package com.aomc.coop.interceptor;


import com.aomc.coop.service.JwtService;
import com.rabbitmq.client.AMQP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class OpenAPITokenInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AMQP.Channel.Open.class);
    private static final String HEADER_AUTH = "X-Auth-Token";

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("in interceptor");
        String tokenString = request.getHeader(HEADER_AUTH);
        logger.info(tokenString);
        if (tokenString != null) {
            if (jwtService.isUsableToken(tokenString)) {
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
