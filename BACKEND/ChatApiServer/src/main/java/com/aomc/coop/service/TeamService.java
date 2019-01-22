package com.aomc.coop.service;

import com.aomc.coop.mapper.ChannelMapper;
import com.aomc.coop.mapper.MessageMapper;
import com.aomc.coop.mapper.TeamMapper;
import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Message;
import com.aomc.coop.model.Team;
import com.aomc.coop.model.User;
import com.aomc.coop.response.Status_1000;
import com.aomc.coop.response.Status_5000;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TeamService {

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private MessageMapper messageMapper;

    private JwtService jwtService;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

//    @Resource(name="redisTemplate")
//    private HashOperations<String, String, String> values;


    //팀생성
    //owner를 idx로 넣은것
    //channel을 배열로 넣는것
    @Transactional
    public ResponseType createTeam(final Team team) {
        try {
            List<User> users = team.getUsers();
            team.setOwner(users.get(0).getIdx());

            Channel channel = new Channel();
            channel.setName("general");

            Message message = new Message();
            message.setContent("joined #general");
            message.setUser(users.get(0));
            List<Message> messages = new ArrayList<>();
            messages.add(message);

            channel.setMessages(messages);

            List<Channel> channels = new ArrayList<>();
            channels.add(channel);

            team.setChannels(channels);

            teamMapper.createTeam(team);

            System.out.println("createTeam");

            channelMapper.createChannel(channel, team.getIdx());

            System.out.println("createChannel " + channel.getIdx());

            messageMapper.createMessage(message, channel.getIdx(), users.get(0).getIdx());

            System.out.println("createMessage");


            for (User user : users) {
                if(user.getIdx()==team.getOwner()){
                    teamMapper.createUserHasTeam(team.getIdx(), user.getIdx(),1);
                    System.out.println("createUserHasTeam1");
                }else{
                    teamMapper.createUserHasTeam(team.getIdx(), user.getIdx(),0);
                    System.out.println("createUserHasTeam2");
                }
                channelMapper.createUserHasChannel(channel.getIdx(), user.getIdx());
                System.out.println("createUserHasChannel");
            }


            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_CREATE_TEAM.getStatus());

        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus());
        }

    }

    //팀수정
    @Transactional
    public ResponseType updateTeam(final Team team) {
        try {
            teamMapper.updateTeam(team);
            return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_UPDATE_TEAM.getStatus());

        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_UPDATE_TEAM.getStatus());
        }

    }

    //팀삭제(팀비활성화)
    @Transactional
    public ResponseType deleteTeam(final int teamIdx) {

            int delete_flag = teamMapper.deleteTeam(teamIdx);
            System.out.println(delete_flag);

            if(delete_flag!=0)
                return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_DELETE_TEAM.getStatus());
            else
                return codeJsonParser.codeJsonParser(Status_5000.FAIL_DELETE_TEAM.getStatus());

    }

}
