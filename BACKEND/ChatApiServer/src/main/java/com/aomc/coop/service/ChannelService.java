package com.aomc.coop.service;

import com.aomc.coop.mapper.ChannelMapper;
import com.aomc.coop.mapper.MessageMapper;
import com.aomc.coop.mapper.UserMapper;
import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Message;
import com.aomc.coop.model.User;
import com.aomc.coop.response.Status_1000;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.date.DateFormatCustom;
import com.aomc.coop.utils.rabbitMQ.RabbitMQUtil;
import com.aomc.coop.utils.redis.RedisUtil;
import com.aomc.coop.utils.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@Transactional
public class ChannelService {

    @Autowired
    private ChannelMapper channelMapper;

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

    @Autowired
    private RabbitMQUtil rabbitMQUtil;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    public ResponseType createChannel(Channel channel) {

        if (channel == null) {
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_CREATE_Channel.getStatus());
        }

        //message Date Format
        Date date = new Date();
        DateFormatCustom dataFormatCustom = new DateFormatCustom();
        String sendDate = dataFormatCustom.sendDateFormat(date);
        String sendTime = dataFormatCustom.sendTimeFormat(date);
        String sendDBDate = dataFormatCustom.sendDBDateFormat(date);

        //message info setting
        Message message = new Message();
        message.setContent("joined #" + channel.getName());
        message.setNickname(channel.getUsers().get(0).getNickname());
        message.setMessage_idx(0);
        message.setUser_idx(channel.getUsers().get(0).getIdx());
        message.setSend_date(sendDate);
        message.setSend_time(sendTime);
        message.setSend_db_date(sendDBDate);

        List<Message> messages = new ArrayList<>();
        messages.add(message);
        channel.setMessages(messages);
        int channelIdx = channelMapper.createChannel(channel, channel.getTeamIdx());

        //
        message.setChannel_idx(channel.getIdx());
        rabbitMQUtil.sendRabbitMQ(message);
        //
        for (int i = 0; i < channel.getUsers().size(); i++) {
//            System.out.println("채널 생성 함수의 user index = " + channel.getUsers().get(i).getIdx());
            channelMapper.createUserHasChannel(channel.getIdx(), channel.getUsers().get(i).getIdx());

        }
        //int idx2 = channelMapper.createUserHasChannel(channel.getIdx(), channel.getUsers().get(0).getIdx());

        if (channel.getIdx() >= 0) {
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_CREATE_Channel.getStatus(), channel.getIdx());
        } else {
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_CREATE_Channel.getStatus());
        }

    }

    public ResponseType updateChannel(Channel channel) {
        if (channel != null) {
            channelMapper.updateChannel(channel);
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Update_Channel.getStatus());
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_Update_Channel.getStatus());
        }
    }

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
                    List<Message> messages = channelMapper.getChannelMessage(channelIdx, start);
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
                List<Message> messages = channelMapper.getChannelMessage(channelIdx, start);
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


    public ResponseType getChannelUsers(int channelIdx, int teamIdx) {
        if (channelIdx >= 0) {
            List<User> users = channelMapper.getChannelUsers(channelIdx, teamIdx);
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Get_Channel_Users.getStatus(), users);
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_Get_Channel_Users.getStatus());
        }
    }

    //user의 idx만 받음
    public ResponseType inviteChannelUser(Channel channel) {
        if (channel!=null) {
            List<User> inviteUsers = channel.getUsers();

            for(User inviteUser : inviteUsers){
                try {
                    int status = channelMapper.findChannelStatus(channel.getIdx(), inviteUser.getIdx());

                    if (status == 0) {
                        System.out.println("해당 채널을 나간 사용자 입니다.");
                        channelMapper.updateChannelStatus(1, channel.getIdx(), inviteUser.getIdx());
//                      return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Invite_Channel_User.getStatus());
                    } else if (status == 1) {
                        System.out.println("이미 채널에 있는 사용자입니다.");
//                      return codeJsonParser.codeJsonParser(Status_1000.Channel_Already_Has_User.getStatus());
                    }
                } catch (Exception e) {
                    //user_has_channel에 없으면 insert
                    channelMapper.inviteChannelUser(channel.getIdx(), inviteUser.getIdx());
                }

            }
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Invite_Channel_User.getStatus());
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_Invite_Channel_User.getStatus());
        }
    }



            //user_has_channel에 해당 채널에 유저가 있는지 판단
//            try {
//                int idx = channelMapper.findByChannelIdxAndUserIdx(channelIdx, userIdx);
//                System.out.println("idx = " + idx);
//                //status를 확인
//                int status = channelMapper.findByStatusFromIdx(idx);
//                //status가 0이면 1로 바꿔줌
//                if (status == 0) {
//                    channelMapper.updateChannelStatus(1, channelIdx, userIdx);
//                    return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Invite_Channel_User.getStatus());
//                }
//                //status가 1이면 이미 있는 사용자라고 말해줌
//                else if (status == 1) {
//                    return codeJsonParser.codeJsonParser(Status_1000.Channel_Already_Has_User.getStatus());
//                }
//
//                return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Invite_Channel_User.getStatus());





    public ResponseType deleteChannelUser(int channelIdx, int userIdx) {
        if (channelIdx >= 0 || userIdx >= 0) {
            channelMapper.deleteChannelUser(channelIdx, userIdx);
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Delete_Channel_User.getStatus());
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_Delete_Channel_User.getStatus());
        }
    }

    public ResponseType updateUserHasChannelStar(int channelIdx, int userIdx, int starFlag) {
        if (channelIdx >= 0 || userIdx >= 0) {
            channelMapper.updateUserHasChannelStar(channelIdx, userIdx, starFlag);
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_UdpateStar_Channel_User.getStatus());
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_UdpateStar_Channel_User.getStatus());
        }
    }
}
