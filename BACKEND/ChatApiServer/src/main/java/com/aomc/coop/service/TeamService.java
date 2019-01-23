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
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import com.aomc.coop.utils.mail.MailsendUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Autowired
    static private MailSender sender;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    @Resource(name="redisTemplate")
    private HashOperations<String, String, String> values;

    public TeamService(JwtService jwtService) {
        this.jwtService = jwtService;
    }


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
                if(user.getIdx()== team.getOwner()){
                    teamMapper.createUserHasTeam(team.getIdx(), user.getIdx(),1);

//                    final JwtService.TokenResponse token = new JwtService.TokenResponse(jwtService.create(team.getIdx(), user.getIdx()));
//
//                    SimpleMailMessage msg = new SimpleMailMessage();
//                    msg.setFrom("CoopDeveloper");
//                    msg.setTo(user.getUid());
//                    msg.setSubject("[COOP Team 초대 이메일 인증]");
//                    msg.setText(new StringBuffer().append("<h1>메일인증</h1>").append("<a href='http://localhost:8083/api/team/accept/").append(token.getToken()).append("' target='_blenk'>이메일 인증 확인</a>").toString());
//                    this.sender.send(msg);
//
//                    HashMap hashMap = new HashMap();
//                    hashMap.put("teamIdx", team.getIdx());
//                    hashMap.put("userIdx", user.getIdx());
//                    values.putAll(token.getToken(), hashMap);

                }else{
                    teamMapper.createUserHasTeam(team.getIdx(), user.getIdx(),0);



                }
                channelMapper.createUserHasChannel(channel.getIdx(), user.getIdx());
            }


            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_CREATE_TEAM.getStatus());

        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_CREATE_TEAM.getStatus());
        }

    }

    //팀 상세조회
    public ResponseType readTeamDetail(final int teamIdx) {

            Team team = teamMapper.readTeamDetail(teamIdx);

            if(team == null){
                return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_TEAM.getStatus());
            }
            return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_READ_TEAM.getStatus(), team);

    }

    //팀수정
    @Transactional
    public ResponseType updateTeam(final Team team) {
        try {

            Team temp = teamMapper.readTeamDetail(team.getIdx());

            if(temp == null){
                return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_TEAM.getStatus());
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

        Team temp = teamMapper.readTeamDetail(teamIdx);

        if(temp == null){
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_TEAM.getStatus());
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
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_USER.getStatus());
        }

        int flag = teamMapper.createUserHasTeam(teamIdx, userTemp.getIdx(), 0);

        if(flag==1)
            return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_INVITE.getStatus());
        else
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_INVITE.getStatus());
    }

    //채널조회
    public ResponseType readChannel(final int teamIdx, final int userIdx) {

        List<Channel> channels= channelMapper.readChannel(teamIdx, userIdx);

        if(channels == null){
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_CHANNEL.getStatus());
        }
        return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_READ_CHANNEL.getStatus(), channels);
    }

    //비활성화
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


    //유저의 팀조회
    public ResponseType readTeamOfUser(final int userIdx) {

        User user = userMapper.findByUserIdx(userIdx);
        if(user == null){
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_USER.getStatus());
        }

        List<Team> teams = teamMapper.readTeamOfUser(userIdx);
        List<Team> sendteams = new ArrayList<>();

        for(Team team : teams){
            if(team.getStatus()==1){
                sendteams.add(team);
            }

        }

        if(teams == null){
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_TEAM.getStatus());
        }
        return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_READ_TEAM.getStatus(), sendteams);

    }


    //팀의 유저조회
    public ResponseType readUserOfTeam(final int teamIdx) {

        Team team = teamMapper.readTeamDetail(teamIdx);
        if(team==null){
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_TEAM.getStatus());
        }

        List<User> users = teamMapper.readUserOfTeam(teamIdx);

        if(users == null){
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_USER.getStatus());
        }
        return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_READ_USER.getStatus(), users);

    }


    //메일보내기
    public ResponseType sendMail(final int teamIdx, final String uid) {

        User user = userMapper.findBysUserid(uid);
        if(user==null){
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_USER.getStatus());
        }


        final JwtService.TokenResponse token = new JwtService.TokenResponse(jwtService.create(teamIdx, user.getIdx()));


        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("CoopDeveloper");
        msg.setTo(user.getUid());
        msg.setSubject("[Invite you to join a Coop workspace]");
        msg.setText(new StringBuffer().append("<h1>메일인증</h1>").append("<a href='http://localhost:8083/api/team/accept/").append(token.getToken()).append("' target='_blenk'>이메일 인증 확인</a>").toString());
        this.sender.send(msg);

        HashMap hashMap = new HashMap();
        hashMap.put("teamIdx", teamIdx);
        hashMap.put("userIdx", user.getIdx());
        values.putAll(token.getToken(), hashMap);

        return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_SEND_MAIL.getStatus());

    }

    //초대 승낙
    public ResponseType acceptInvite(final String token) {

        String string_teamIdx = (String) values.get(token, "teamIdx");
        String string_userIdx = (String)values.get(token, "userIdx");

        int teamIdx = Integer.parseInt(string_teamIdx);
        int userIdx = Integer.parseInt(string_userIdx);

        System.out.println(teamIdx);
        System.out.println(userIdx);

        if(teamIdx==-1||userIdx==-1){
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_INCORRECT_AUTHKEY.getStatus());
        }
        teamMapper.updateAuthFlag(teamIdx, userIdx);
        return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_ACCEPT_INVITE.getStatus());


    }

}
