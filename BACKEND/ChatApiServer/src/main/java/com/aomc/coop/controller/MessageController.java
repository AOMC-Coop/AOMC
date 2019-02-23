package com.aomc.coop.controller;

import com.aomc.coop.config.RabbitMQConfig;
import com.aomc.coop.model.ChannelInvite;
import com.aomc.coop.model.Message;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.ChannelService;
import com.aomc.coop.service.MessageService;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.rabbitMQ.RabbitMQUtil;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

//방 아이디에 따라 메세지 나누기
@Slf4j
@RestController
public class MessageController {

    @Autowired
    private RabbitMQUtil rabbitMQUtil;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    private static final Logger logger = LoggerFactory.getLogger(ChannelService.class);


    @MessageMapping("/chat")
    public Message broadcasting(Message msg) throws Exception{
        return rabbitMQUtil.sendRabbitMQ(msg);
    }

    @MessageMapping("/channelInvite")
    public ChannelInvite broadcasting(ChannelInvite invite) throws Exception{

        return rabbitMQUtil.channel_sendRabbitMQ(invite);

//        logger.info("channelInvite => " + invite);
//
//        //list만큼 돌기
//        for(int i=0; i<invite.getToInvite().size(); i++) {
//            this.simpMessagingTemplate.convertAndSend("/topic/chatInvite/" + invite.getToInvite().get(i).getIdx(), invite);
//            logger.info("sendTo => " + invite.getToInvite().get(i).getIdx());
//        }
//
//        return invite;
    }

    @RabbitListener(queues = RabbitMQConfig.RECEIVE_QUEUE_NAME)
    public void broadCasting(Message message) throws Exception {
        rabbitMQUtil.receiveRabbitMQ(message);
    }

    @RabbitListener(queues = RabbitMQConfig.CHANNEL_TOPIC_QUEUE_NAME)
    public void broadCasting(ChannelInvite invite) throws Exception {
        rabbitMQUtil.channel_receiveRabbitMQ(invite);
    }

    /**
     *
     *        @brief GET http://localhost:8083/api/channel/message?channelIdx=21&start=0&messageLastIdx=0
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
        System.out.println("MessageController");
        if(channelIdx >= 0) {
            return new ResponseEntity<>(messageService.getChannelMessage(channelIdx, start, messageLastIdx), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }
}

