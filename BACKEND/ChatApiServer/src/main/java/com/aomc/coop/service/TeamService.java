package com.aomc.coop.service;

import com.aomc.coop.mapper.ChannelMapper;
import com.aomc.coop.mapper.MessageMapper;
import com.aomc.coop.mapper.TeamMapper;
import com.aomc.coop.mapper.UserMapper;
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

    @Autowired
    private UserMapper userMapper;

    private JwtService jwtService;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();
//    ResponseType<T> responseType = new ResponseType<T>();

//    @Resource(name="redisTemplate")
//    private HashOperations<String, String, String> values;


    //팀생성
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
            channelMapper.createChannel(channel, team.getIdx());
            messageMapper.createMessage(message, channel.getIdx(), users.get(0).getIdx());

            for (User user : users) {
                if(user.getIdx()==team.getOwner()){
                    teamMapper.createUserHasTeam(team.getIdx(), user.getIdx(),1);
                }else{
                    teamMapper.createUserHasTeam(team.getIdx(), user.getIdx(),0);
                }
                channelMapper.createUserHasChannel(channel.getIdx(), user.getIdx());
            }


            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_CREATE_TEAM.getStatus());

        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus());
        }

    }

    //팀조회
    public ResponseType readTeam(final int teamIdx) {

            Team team = teamMapper.readTeam(teamIdx);

            if(team == null){
                return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ.getStatus());
            }
            return ResponseType.res(200,"팀 조회 성공", "Success Team Read", team);

    }

    //팀수정
    @Transactional
    public ResponseType updateTeam(final Team team) {
        try {

            Team temp = teamMapper.readTeam(team.getIdx());

            if(temp == null){
                return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ.getStatus());
            }
            if(temp.getStatus()==0){
                return codeJsonParser.codeJsonParser(Status_5000.DEACTIVE_TEAM.getStatus());
            }

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

        Team temp = teamMapper.readTeam(teamIdx);

        if(temp == null){
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ.getStatus());
        }

        if(temp.getStatus()==0){
            return codeJsonParser.codeJsonParser(Status_5000.DEACTIVE_TEAM.getStatus());
        }
        int delete_flag = teamMapper.deleteTeam(teamIdx);

        if(delete_flag!=0)
            return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_DELETE_TEAM.getStatus());
        else
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_DELETE_TEAM.getStatus());

    }


    //팀초대
    @Transactional
    public ResponseType inviteTeam(final int teamIdx, final String uid) {


        User userTemp = userMapper.findBysUserid(uid);
        if(userTemp ==null){
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ.getStatus());
        }

        int flag = teamMapper.createUserHasTeam(teamIdx, userTemp.getIdx(), 0);

        if(flag==1)
            return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_INVITE.getStatus());
        else
            return codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus());
    }

    //채널조회
    public ResponseType readChannel(final int teamIdx, final int userIdx) {

        List<Channel> channels= channelMapper.readChannel(teamIdx, userIdx);

        if(channels == null){
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ.getStatus());
        }
        return ResponseType.res(200,"채널 조회 성공", "Success Channel Read", channels);

    }

    //채널조회
    @Transactional
    public ResponseType deactiveUser(final int teamIdx, final int userIdx) {

        int teamFlag = teamMapper.deactiveUserOfTeam(teamIdx, userIdx);

        List<Channel> channels= channelMapper.readChannel(teamIdx, userIdx);

        for (Channel channel : channels) {
            int channelFlag = channelMapper.deactiveUserOfChannel(channel.getIdx(), userIdx);
            if(channelFlag!=1){ //예외처리
                return codeJsonParser.codeJsonParser(Status_5000.FAIL_DEACVITE_USER.getStatus());
            }
        }

        if(teamFlag==1)
            return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_DEACVITE_USER.getStatus());
        else
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_DEACVITE_USER.getStatus());
    }

}
