package com.aomc.coop.service;

import com.aomc.coop.mapper.ChannelMapper;
import com.aomc.coop.mapper.TeamMapper;
import com.aomc.coop.model.Channel;
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

    private TeamMapper teamMapper;

    @Autowired
    private ChannelMapper channelMapper;

    private JwtService jwtService;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

//    @Resource(name="redisTemplate")
//    private HashOperations<String, String, String> values;

    public TeamService(final TeamMapper teamMapper, JwtService jwtService) {
        this.teamMapper = teamMapper;
        this.jwtService = jwtService;
    }

    //팀생성
    //owner를 idx로 넣은것
    //channel을 배열로 넣는것
    @Transactional
    public ResponseType createTeam(final Team team) {
        try {
            Channel channel = new Channel();
            channel.setName("general");
//            List<Channel> channels = new ArrayList<>();
//            channels.add(channel);
//            team.setChannels(channels);

            teamMapper.createTeam(team);


            List<User> users = team.getUsers();
            team.setOwner(users.get(0).getIdx());

            for (User user : users) {
                if(user.getIdx()==team.getOwner()){
                    teamMapper.createUserHasTeam(team.getIdx(), user.getIdx(),1);
                }else{
                    teamMapper.createUserHasTeam(team.getIdx(), user.getIdx(),0);
                }
                channelMapper.createChannel(channel, team.getIdx(), user.getIdx());
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
