//package com.aomc.coop.interceptor;
//
//import com.aomc.coop.service.TokenService;
//import com.rabbitmq.client.AMQP;
//import lombok.AllArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class OpenAPITokenInterceptor implements HandlerInterceptor {
//    private static final Logger logger = LoggerFactory.getLogger(AMQP.Channel.Open.class);
//    private static final String HEADER_AUTH = "X-Auth-Token";
//
//    private final TokenService tokenService;
//
//    public OpenAPITokenInterceptor(TokenService tokenService) {
//        this.tokenService = tokenService;
//    }
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        logger.debug("in interceptor");
//        String tokenString = request.getHeader(HEADER_AUTH);
////        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
////                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
////        logger.info(tokenString);
//        if (tokenString != null) {
////            logger.info(tokenString);
//            if (tokenService.isUsableToken(tokenString)) {
//                return true;
//            }
//        }
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//        return false;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//
//    }
//}
