package com.aomc.coop.controller;

import com.aomc.coop.config.RabbitMQConfig;
import com.aomc.coop.model.Message;
import com.aomc.coop.util.RedisUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Resource(name = "redisTemplate")
    private ListOperations<String, Message> listOperations;

    @Resource(name = "redisTemplate")
    private HashOperations<String, Integer, Integer> hashOperations_channelIdx; //String : key, Integer : channelIdx, Integer : lastMessageIdx

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(AMQP.Channel.Open.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void onMessage(HashMap<String, Message> map) {
        logger.debug("message = " + map.get("msg"));
        int channelIdx = map.get("msg").getChannel_idx();

        //redis에 저장하기
        Map<Integer, Integer> channelInfo_map = new HashMap<>();

        Map<Integer, Integer> map2 = hashOperations_channelIdx.entries(RedisUtil.ChannelInfoKey);
        ObjectMapper mapper = new ObjectMapper();
        Map<Integer, Integer> get_map = mapper.convertValue(map2, new TypeReference<HashMap<Integer, Integer>>() {
        });
        int lastIdx = 0;
        try{
            lastIdx = get_map.get(channelIdx);
            lastIdx += 1;
        }catch (NullPointerException e) {
            lastIdx = 0;
        }

        map.get("msg").setMessage_idx(lastIdx);

        channelInfo_map.put(map.get("msg").getChannel_idx(), lastIdx);

        hashOperations_channelIdx.putAll(RedisUtil.ChannelInfoKey, channelInfo_map);

        listOperations.rightPush(RedisUtil.redisKey + map.get("msg").getChannel_idx(), map.get("msg"));

        //receive 큐에보냄
        if (map.get("msg") != null) {
            rabbitTemplate.convertAndSend(RabbitMQConfig.RECEIVE_QUEUE_NAME, map.get("msg"));
            logger.debug("receive_rabbitMQ send" + map.get("msg"));
        }


    }
}
