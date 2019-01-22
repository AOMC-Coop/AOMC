package com.aomc.coop.service;

import com.aomc.coop.mapper.ChannelMapper;
import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Message;
import com.aomc.coop.model.User;
import com.aomc.coop.response.Status_1000;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ChannelService {

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private JwtService jwtService;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    public ResponseType createChannel(Channel channel) {

        if(channel == null) {
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_CREATE_Channel.getStatus());
        }

        int idx = channelMapper.createChannel(channel, channel.getTeamIdx());

        if(idx >= 0) {
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_CREATE_Channel.getStatus());
        }else {
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_CREATE_Channel.getStatus());
        }

    }

    public ResponseType updateChannel(Channel channel) {
        if(channel != null) {
            channelMapper.updateChannel(channel);
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Update_Channel.getStatus());
        }else{
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_Update_Channel.getStatus());
        }
    }

    public ResponseType getChannelMessage(int channelIdx) {
        if(channelIdx >= 0) {
            List<Message> messages = channelMapper.getChannelMessage(channelIdx);
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Get_Message.getStatus(), messages);
        }else{
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_Get_Message.getStatus());
        }
    }

    public ResponseType getChannelUsers(int channelIdx) {
        if(channelIdx >= 0) {
            List<Integer> users_idx = channelMapper.getChannelUsers(channelIdx);
            List<User> users = new ArrayList<>();
            for(Integer idx : users_idx) {
                System.out.println("userIdx = " + idx);
                User user = channelMapper.findByUserIdx(idx);
                users.add(user);
            }
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Get_Channel_Users.getStatus(), users);
        }else{
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_Get_Channel_Users.getStatus());
        }
    }

    public ResponseType inviteChannelUser(int channelIdx, int userIdx) {
        if(channelIdx >= 0 || userIdx >= 0) {
            channelMapper.inviteChannelUser(channelIdx, userIdx);
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Invite_Channel_User.getStatus());
        }else{
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_Invite_Channel_User.getStatus());
        }
    }

    public ResponseType deleteChannelUser(int channelIdx, int userIdx) {
        if(channelIdx >= 0 || userIdx >= 0) {
            channelMapper.deleteChannelUser(channelIdx, userIdx);
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_Delete_Channel_User.getStatus());
        }else{
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_Delete_Channel_User.getStatus());
        }
    }
}
