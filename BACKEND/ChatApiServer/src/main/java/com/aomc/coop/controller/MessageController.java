package com.aomc.coop.controller;

import com.aomc.coop.config.RabbitMQConfig;
import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Message;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.MessageService;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.rabbitMQ.RabbitMQUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

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
    private RabbitMQUtil rabbitMQUtil;

    @Autowired
    private MessageService messageService;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();


    @MessageMapping("/chat")
    public Message broadcasting(Message msg) throws Exception{
        System.out.println("MessageController - " + msg);
        return rabbitMQUtil.sendRabbitMQ(msg);
    }

    @RabbitListener(queues = RabbitMQConfig.RECEIVE_QUEUE_NAME)
    public void broadCasting(Message message) throws Exception {
        rabbitMQUtil.receiveRabbitMQ(message);
    }

    /**

     *

     *        @brief GET http://localhost:8083/api/channel/message?channelIdx=21

     *        @details 채널의 메세지를 가져오는 함수

     *        @param RequestParam("channelIdx") final int channelIdx

     *        @return ResponseEntity<>

     *        성공시
     *
     *        {
     *           "status": 200,
     *           "message": "메세지 조회 성공",
     *           "description": "Success Message Get"
     *           "data": [
     *              {
     *             "idx": 14,
     *             "content": "hello"
     *              },
     *              {
     *             "idx": 15,
     *             "content": "hello2"
     *              }
     *           ]
     *        }

     *        실패시

     *        {
     *           "status": 400,
     *           "message": "메세지 조회 실패",
     *           "description": "Fail Message Get"
     *        }

     *

     *        @throws

     *

     */
    @GetMapping
    @RequestMapping("/api/channel/message")
    public ResponseEntity getChannelMessage(@RequestParam("channelIdx") final int channelIdx, @RequestParam("start") final int start, @RequestParam("messageLastIdx") final int messageLastIdx){
        if(channelIdx >= 0) {
            return new ResponseEntity<>(messageService.getChannelMessage(channelIdx, start, messageLastIdx), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }
}

