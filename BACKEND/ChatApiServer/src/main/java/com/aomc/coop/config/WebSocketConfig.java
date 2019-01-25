package com.aomc.coop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

//MessageBroker에 대한 설정
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic"); //메모리기반 메세지 브로커가 해당 api를 구독하는 클라이언트에게 메세지를 전달
        config.setApplicationDestinationPrefixes("/app"); //서버에서 클라이언트로부터의 메세지를 받을 api의 prefix 설정
    }

    //클라이언트에서 웹소켓을 연결할 api설정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/chat")
                .setAllowedOrigins("http://localhost:8081")
                .withSockJS();
//        registry.addEndpoint("/chat")
////                .setAllowedOrigins("http://localhost:8081")
////                .withSockJS();
    }
}
