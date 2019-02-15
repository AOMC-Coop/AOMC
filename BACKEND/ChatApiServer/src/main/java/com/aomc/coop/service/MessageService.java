package com.aomc.coop.service;

import com.aomc.coop.mapper.MessageMapper;
import com.aomc.coop.mapper.UserMapper;
import com.aomc.coop.model.Message;
import com.aomc.coop.response.Status_1000;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import com.aomc.coop.utils.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtService jwtService;

    @Resource(name = "redisTemplate")
    private ListOperations<String, Message> listOperations;

    @Autowired
    private RedisTemplate redisTemplate;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    public ResponseType getChannelMessage(int channelIdx, int start, int messageLastIdx) {
        System.out.println("start = " + start + " lastIdx = " + messageLastIdx + " channelIdx = " + channelIdx);

        if (channelIdx >= 0) {
            //redis에서 메세지 가져오기
            listOperations = redisTemplate.opsForList();
            List<Message> redis_messageList = listOperations.range(RedisUtil.redisKey + channelIdx, 0, -1);
            if (redis_messageList.size() > 0) {
                if(start == -1) {
                    start = -3;
                    System.out.println("-1-getChannelMessage - Redis");
                    redis_messageList.clear();
                    return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Get_Message.getStatus(), redis_messageList, start);
                }
                else if(redis_messageList.size() == 1 && messageLastIdx == 0 && redis_messageList.get(0).getMessage_idx() == 0) { //join #general 이 redis에 유일하게 있는 경우
                    start = -2;
                    System.out.println("0-getChannelMessage - Redis");
                    return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Get_Message.getStatus(), redis_messageList, start);
                }
                else if (messageLastIdx == 0) { // redis 일 때
                    start = -1;
                    Collections.reverse(redis_messageList);
                    System.out.println("1-getChannelMessage - Redis");
                    return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Get_Message.getStatus(), redis_messageList, start);
                } else if (messageLastIdx <= redis_messageList.get(redis_messageList.size() - 1).getMessage_idx()) { //redis이지만 이미 다 가져온 경우
                    //mysql에서 가져오기
                    System.out.println("//////-getChannelMessage - MySQL");
                    List<Message> messages = messageMapper.getChannelMessage(channelIdx, start);
//            System.out.println(messages);

                    if (messages.size() == 0) {
                        return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Get_Message.getStatus(), messages, start);
                    }
                    System.out.println("2-getChannelMessage - MySQL");
                    return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Get_Message.getStatus(), messages, start);
                }
//                else if (messageLastIdx < redis_messageList.get(redis_messageList.size() - 1).getMessage_idx()) { //redis인 경우 - 부분만 가져왔을 경우
//                    start = -1;
//                    List<Message> subMessageList = redis_messageList.subList(messageLastIdx + 1, redis_messageList.size() - 1);
//                    System.out.println("3-getChannelMessage - Redis - subMessageList");
//                    return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Get_Message.getStatus(), subMessageList, start);
//                }
                else {
                    return codeJsonParser.codeJsonParser(Status_1000.No_Message.getStatus(), start);
                }


            } else {
                //mysql에서 메세지 가져오기
                System.out.println("||||||||||-getChannelMessage - MySQL");
                List<Message> messages = messageMapper.getChannelMessage(channelIdx, start);
//            System.out.println(messages);

                if (messages.size() == 0) {
                    return codeJsonParser.codeJsonParser(Status_1000.No_Message.getStatus(), start);
                }

                System.out.println("4-getChannelMessage - MySQL");
                return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Get_Message.getStatus(), messages, start);
            }

        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_Get_Message.getStatus(), start);
        }
    }
}
