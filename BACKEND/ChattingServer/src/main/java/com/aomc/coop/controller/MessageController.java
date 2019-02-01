package com.aomc.coop.controller;

import com.aomc.coop.config.RabbitMQConfig;
import com.aomc.coop.model.Message;
import com.aomc.coop.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class MessageController {

    @Resource(name="redisTemplate")
    private ListOperations<String, Message> listOperations;

    @Resource(name="redisTemplate")
    private HashOperations<String, Integer, Integer> hashOperations_channelIdx; //String : key, Integer : channelIdx, Integer : lastMessageIdx

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void onMessage(HashMap<String, Message> map) {
        System.out.println("message = " + map);

        //redis에 저장하기
        Map<Integer, Integer> channelInfo_map = new HashMap<>();
        channelInfo_map.put(map.get("msg").getChannel_idx(), 170);
        hashOperations_channelIdx.putAll(RedisUtil.ChannelInfoKey, channelInfo_map);

        listOperations.rightPush(RedisUtil.redisKey + ":" + map.get("msg").getChannel_idx(), map.get("msg"));

        //receive 큐에보냄
        if(map.get("msg") != null) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.RECEIVE_QUEUE_NAME, map.get("msg"));
            System.out.println("receive_rabbitMQ send");
        }


    }
}
