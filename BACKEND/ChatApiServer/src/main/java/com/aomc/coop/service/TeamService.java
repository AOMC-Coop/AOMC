package com.aomc.coop.service;

import com.aomc.coop.mapper.TeamMapper;
import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Team;
import com.aomc.coop.model.User;
import com.aomc.coop.response.Status_1000;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TeamService {

    private TeamMapper teamMapper;
    private JwtService jwtService;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

//    @Resource(name="redisTemplate")
//    private HashOperations<String, String, String> values;

    public TeamService(final TeamMapper teamMapper, JwtService jwtService) {
        this.teamMapper = teamMapper;
        this.jwtService = jwtService;
    }


    public ResponseType createTeam(final Team team) {

        try {
            Channel channel = new Channel();
            channel.setName("general");

            teamMapper.createTeam(team);

            System.out.println("team_idx는"+team.getIdx());

            List<User> users = team.getUsers();

            System.out.println("users.size()는"+ users.size());
//            for (User user : users) {
//                System.out.println("user_idx는"+user.getIdx());
//                teamMapper.createUserHasTeam(team_idx, user.getIdx());
//                teamMapper.createChannel(channel, team_idx, user.getIdx());
//            }

            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_CREATE_TEAM.getStatus());

        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus());
        }

    }

}
