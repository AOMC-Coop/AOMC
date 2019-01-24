package com.aomc.coop.service;

import com.aomc.coop.config.MailConfig;
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
import com.aomc.coop.utils.mail.MailSend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    private JavaMailSender mailSender;

    MailSend mailSend = new MailSend();


    @Resource(name="redisTemplate")
    private HashOperations<String, String, String> values;

    public TeamService(JwtService jwtService, JavaMailSender mailSender) {
        this.jwtService = jwtService;
        this.mailSender = mailSender;
    }


    //팀생성
    @Transactional
    public ResponseType createTeam(final Team team) {
        try {
            List<User> users = team.getUsers();
            team.setOwner(users.get(0).getUid());

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

            User ownerUserInfo = userMapper.findBysUserid(users.get(0).getUid());
            messageMapper.createMessage(message, channel.getIdx(), ownerUserInfo.getIdx());

            for (User user : users) {

                User userTemp = userMapper.findBysUserid(user.getUid());

                final JwtService.TokenResponse token = new JwtService.TokenResponse(jwtService.create(team.getIdx(), user.getUid()));

                HashMap hashMap = new HashMap();
                hashMap.put("teamIdx", team.getIdx());

                //방장인 경우 메일 안보냄
                if(user.getUid().equals(team.getOwner())){
                    teamMapper.createUserHasTeam(team.getIdx(), userTemp.getIdx(),1);
                }else{

                    //초대받은팀원이 User가 아닌 경우 - userID만 넣기
                    if(userTemp==null){
                        hashMap.put("uid", user.getUid());
                        hashMap.put("userIdx", 0);

                    }else{//초대받은팀원이 User인 경우
                        //각테이블에 생성 데이터넣기
                        System.out.println(userTemp.getIdx());
                        teamMapper.createUserHasTeam(team.getIdx(), userTemp.getIdx(),0);
                        channelMapper.createUserHasChannel(channel.getIdx(), userTemp.getIdx());

                        //Redis에 정보 저장
                        hashMap.put("uid", user.getUid());
                        hashMap.put("userIdx", userTemp.getIdx());
                    }

                    values.putAll(token.getToken(), hashMap);

                    mailSend.mailsend(mailSender, user.getUid(), token.getToken());

                }


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

        final JwtService.TokenResponse token = new JwtService.TokenResponse(jwtService.create(teamIdx, uid));
        HashMap hashMap = new HashMap();
        hashMap.put("teamIdx", teamIdx);
        hashMap.put("uid", uid);

        if (user != null) {
            hashMap.put("userIdx", user.getIdx());
        }

        values.putAll(token.getToken(), hashMap);

        mailSend.mailsend(mailSender, uid, token.getToken());


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

        if(teamIdx==-1){
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_INCORRECT_AUTHKEY.getStatus());
        }
        if(userIdx==0){
            return codeJsonParser.codeJsonParser(Status_5000.PLEASE_SIGNUP.getStatus(), token);
        }
        teamMapper.updateAuthFlag(teamIdx, userIdx);
        return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_ACCEPT_INVITE.getStatus());


    }

}
