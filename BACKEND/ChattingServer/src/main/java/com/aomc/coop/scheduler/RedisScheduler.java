package com.aomc.coop.scheduler;

import com.aomc.coop.mapper.MessageMapper;
import com.aomc.coop.model.Message;
import com.aomc.coop.util.RedisUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Component
public class RedisScheduler {

    @Resource(name = "redisTemplate")
    private ListOperations<String, Message> listOperations;

    @Resource(name = "redisTemplate")
    private HashOperations<String, Integer, Integer> hashOperations_channelIdx; //String : key, Integer : channelIdx, Integer : lastMessageIdx


    @Autowired
    private MessageMapper messageMapper;

    // 애플리케이션 시작 후 30초 후에 첫 실행, 그 후 매 30초마다 주기적으로 실행한다.
    //@Scheduled(initialDelay = 30000, fixedDelay = 30000)
    @Transactional
    public void redisChatToMySQL() {

        //1. redis에 저장되어 있는 message들을 가져오기
        //2. 가져온 message들을 redis에서 삭제한 후 db에 저장
        Map<Integer, Integer> map = hashOperations_channelIdx.entries(RedisUtil.ChannelInfoKey);
        ObjectMapper mapper = new ObjectMapper();
        HashMap<Integer, Integer> map3 = mapper.convertValue(map, new TypeReference<HashMap<Integer, Integer>>() {
        });

        Set set = map3.keySet();

        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {

            int key = (int) iterator.next();

            System.out.println("hashMap Key : " + key);

            while (true) {
                Message message = listOperations.leftPop(RedisUtil.redisKey + key);

                if (message == null) {
                    break;
                }
                //db에 저장
                messageMapper.createMessage(message, message.getChannel_idx(), message.getUser_idx());
                System.out.println("///// " + message.getContent());


            }

        }

    }
}
