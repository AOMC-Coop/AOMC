package com.aomc.coop.service;

import com.aomc.coop.mapper.ChannelMapper;
import com.aomc.coop.mapper.MessageMapper;
import com.aomc.coop.mapper.UserMapper;
import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Message;
import com.aomc.coop.model.User;
import com.aomc.coop.response.Status_1000;
import com.aomc.coop.utils.CodeJsonParser;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Resource(name="redisTemplate")
    private ListOperations<String, Message> listOperations;

    @Autowired
    private RedisTemplate redisTemplate;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    public ResponseType createChannel(Channel channel) {

        if (channel == null) {
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_CREATE_Channel.getStatus());
        }
        Message message = new Message();
        message.setContent("joined #" + channel.getName());
        message.setNickname(channel.getUsers().get(0).getNickname());
        List<Message> messages = new ArrayList<>();
        messages.add(message);

        channel.setMessages(messages);

        channelMapper.createChannel(channel, channel.getTeamIdx());
        messageMapper.createMessage(message, channel.getIdx(), channel.getUsers().get(0).getIdx());
        for(int i=0; i<channel.getUsers().size(); i++) {
            System.out.println("채널 생성 함수의 user index = " + channel.getUsers().get(i).getIdx());

            channelMapper.createUserHasChannel(channel.getIdx(), channel.getUsers().get(i).getIdx());

        }
        //int idx2 = channelMapper.createUserHasChannel(channel.getIdx(), channel.getUsers().get(0).getIdx());

        if (channel.getIdx() >= 0) {
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_CREATE_Channel.getStatus());
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

    public ResponseType getChannelMessage(int channelIdx, int start) {

        if (channelIdx >= 0) {
            //redis에서 메세지 가져오기
            listOperations= redisTemplate.opsForList();
            List<Message> redis_messageList = listOperations.range(RedisUtil.redisKey+ ":" + channelIdx, 0, -1);
            if(redis_messageList.size() > 0) {
                Collections.reverse(redis_messageList);
            }


            //mysql에서 메세지 가져오기
            List<Message> messages = channelMapper.getChannelMessage(channelIdx, start);

//            String sendDate = "";
////            if(messages.size() > 0) {
////                sendDate = messages.get(0).getSend_date();
////
////                for(int i=1;i<messages.size();i++){
////                    if(messages.get(i).getSend_date().equals(sendDate)){
////                        messages.get(i).setSend_date("");
////                    }else{
////                        sendDate = messages.get(i).getSend_date();
////                    }
////                }
////            }else {
////                return codeJsonParser.codeJsonParser(Status_1000.FAIL_Get_Message.getStatus()); //에러코드 수정하기
////            }


            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Get_Message.getStatus(), messages);
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_Get_Message.getStatus());
        }
    }

    public ResponseType getChannelUsers(int channelIdx) {
        if (channelIdx >= 0) {
            List<Integer> users_idx = channelMapper.getChannelUsers(channelIdx);
            List<User> users = new ArrayList<>();
            for (Integer idx : users_idx) {
                System.out.println("userIdx = " + idx);
                User user = userMapper.findByUserIdx(idx);
                users.add(user);
            }
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Get_Channel_Users.getStatus(), users);
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_Get_Channel_Users.getStatus());
        }
    }

    public ResponseType inviteChannelUser(int channelIdx, int userIdx) {
        if (channelIdx >= 0 || userIdx >= 0) {
            //user_has_channel에 해당 채널에 유저가 있는지 판단
            try {
                int idx = channelMapper.findByChannelIdxAndUserIdx(channelIdx, userIdx);
                System.out.println("idx = " + idx);
                //status를 확인
                int status = channelMapper.findByStatusFromIdx(idx);
                //status가 0이면 1로 바꿔줌
                if (status == 0) {
                    channelMapper.updateChannelStatus(1, channelIdx, userIdx);
                    return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Invite_Channel_User.getStatus());
                }
                //status가 1이면 이미 있는 사용자라고 말해줌
                else if (status == 1) {
                    return codeJsonParser.codeJsonParser(Status_1000.Channel_Already_Has_User.getStatus());
                }

                return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Invite_Channel_User.getStatus());
            } catch (Exception e) {
                //user_has_channel에 없으면 insert해줌
                channelMapper.inviteChannelUser(channelIdx, userIdx);
                return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Invite_Channel_User.getStatus());
            }
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_Invite_Channel_User.getStatus());
        }
    }

    public ResponseType deleteChannelUser(int channelIdx, int userIdx) {
        if (channelIdx >= 0 || userIdx >= 0) {
            channelMapper.deleteChannelUser(channelIdx, userIdx);
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Delete_Channel_User.getStatus());
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_Delete_Channel_User.getStatus());
        }
    }
}
