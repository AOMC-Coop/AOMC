package com.aomc.coop.controller;

import com.aomc.coop.config.RabbitMQConfig;
import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Message;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.SimpleFormatter;

//방 아이디에 따라 메세지 나누기
@Slf4j
@RestController
public class MessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/chat")
    public Message broadcasting(Message msg) throws Exception{
        System.out.println("요청이 왔습니다" + msg);
//        System.out.println("channelIdx는 " + channelIdx);

        Map<String, Message> map = new HashMap<>();
        map.put("msg", msg);
//        map.put("channelIdx", ms);

        //큐에보냄
        if(msg != null) {
            System.out.println("rabbitMQ send");
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, map);
        }

        return  msg;
    }

    @RabbitListener(queues = RabbitMQConfig.RECEIVE_QUEUE_NAME)
    public void broadCasting(Message message) throws Exception {

        System.out.println("rabbitMQ receive = " + message);
        this.simpMessagingTemplate.convertAndSend("/topic/message", message);

    }
}

