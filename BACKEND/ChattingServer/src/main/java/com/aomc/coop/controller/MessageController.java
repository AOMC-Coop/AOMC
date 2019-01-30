package com.aomc.coop.controller;

import com.aomc.coop.config.RabbitMQConfig;
import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Message;
import com.aomc.coop.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class MessageController {

    @Resource(name="redisTemplate")
    private ListOperations<String, Message> listOperations;

    @Resource(name="redisTemplate")
    private HashOperations<String, Object, Object> hashOperations;


    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void onMessage(HashMap<String, Message> map) {
        System.out.println("message = " + map);

        //redis에 저장하기
        listOperations.rightPush(RedisUtil.redisKey, map.get("msg"));
        //listOperations.getOperations().expire(redis_key, 5L, TimeUnit.MINUTES);

        // test -> 조회해보기
//        List<Message> messages = (ArrayList<Message>)listOperations.leftPop(redis_key);
//        List<Message> messages = new ArrayList<>();
//        while(true) {
//            Message message = (Message)listOperations.leftPop(RedisUtil.redisKey);
//            if(message == null) {
//                break;
//            }
//            messages.add(message);
//            System.out.println("Message's content = " + message.getContent());
//        }

//        LinkedHashMap<String, Message> map2 = (LinkedHashMap<String, Message>)listOperations.leftPop(RedisUtil.redisKey);


    }
}
