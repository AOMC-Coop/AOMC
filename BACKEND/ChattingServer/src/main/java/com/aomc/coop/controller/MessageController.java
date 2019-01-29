package com.aomc.coop.controller;

import com.aomc.coop.config.RabbitMQConfig;
import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
@Slf4j
public class MessageController {

    @Resource(name="redisTemplate")
    private ListOperations<Object, Object> listOperations;

    @Resource(name="redisTemplate")
    private HashOperations<String, Object, Object> hashOperations;


    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void onMessage(HashMap<String, Object> map) {
        System.out.println("message = " + map);

        //redis에 저장하기
        listOperations.rightPush(map.get("channelIdx").toString(), map.get("msg"));


    }
}
