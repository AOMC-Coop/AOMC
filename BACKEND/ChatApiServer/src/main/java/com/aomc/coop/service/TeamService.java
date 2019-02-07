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
import com.aomc.coop.utils.rabbitMQ.RabbitMQUtil;
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
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RabbitMQUtil rabbitMQUtil;

    private JwtService jwtService;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    private JavaMailSender mailSender;

    MailSend mailSend = new MailSend();


    @Resource(name = "redisTemplate")
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

            User ownerUserInfo = userMapper.findBysUserid(users.get(0).getUid());

            Channel channel = new Channel();
            channel.setName("general");

            Message message = new Message();
            message.setContent("joined #general");
            message.setNickname(ownerUserInfo.getNickname());

            //
            message.setMessage_idx(0);
            message.setUser_idx(ownerUserInfo.getIdx());
            //

            List<Message> messages = new ArrayList<>();
            messages.add(message);

            channel.setMessages(messages);

            List<Channel> channels = new ArrayList<>();
            channels.add(channel);

            team.setChannels(channels);

            teamMapper.createTeam(team);
            channelMapper.createChannel(channel, team.getIdx());

            //
            message.setChannel_idx(channel.getIdx());
            rabbitMQUtil.sendRabbitMQ(message);
            //messageMapper.createMessage(message, channel.getIdx(), ownerUserInfo.getIdx());

            for (User user : users) {

                User userTemp = userMapper.findBysUserid(user.getUid());

                final JwtService.TokenResponse token = new JwtService.TokenResponse(jwtService.create(team.getIdx(), user.getUid()));

                HashMap hashMap = new HashMap();
                hashMap.put("teamIdx", team.getIdx());
                hashMap.put("channelIdx", channel.getIdx());

                //방장인 경우 메일 안보냄
                if (user.getUid().equals(team.getOwner())) {
                    teamMapper.createUserHasTeam(team.getIdx(), userTemp.getIdx(), 1, 1);
                    channelMapper.createUserHasChannel(channel.getIdx(), userTemp.getIdx());
                } else {

                    //초대받은팀원이 User가 아닌 경우 - userID만 넣기
                    if (userTemp == null) {
                        hashMap.put("uid", user.getUid());
                        hashMap.put("userIdx", 0);

                    } else {//초대받은팀원이 User인 경우
                        //각테이블에 생성 데이터넣기
                        System.out.println(userTemp.getIdx());
                        teamMapper.createUserHasTeam(team.getIdx(), userTemp.getIdx(), 0, 0);
                        channelMapper.createUserHasChannel(channel.getIdx(), userTemp.getIdx());

                        //Redis에 정보 저장
                        hashMap.put("uid", user.getUid());
                        hashMap.put("userIdx", userTemp.getIdx());
                    }

                    values.putAll(token.getToken(), hashMap);

                    //mailSend.mailsend(mailSender, user.getUid(), token.getToken());

                    SendMailThread sendMailThread = new SendMailThread(mailSender, user.getUid(), token.getToken(), team.getName(), team.getOwner());
                    sendMailThread.start();

                }
            }

            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_CREATE_TEAM.getStatus());

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_CREATE_TEAM.getStatus());
        }

    }

    //팀 상세조회
    public ResponseType readTeamDetail(final int teamIdx) {

        Team team = teamMapper.readTeamDetail(teamIdx);

        if (team == null) {
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_TEAM.getStatus());
        }
        return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_READ_TEAM.getStatus(), team); // team 데이터 보낼때

    }

    //팀수정
    @Transactional
    public ResponseType updateTeam(final Team team) {
        try {

            Team temp = teamMapper.readTeamDetail(team.getIdx());

            if (temp == null) {
                return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_TEAM.getStatus());
            }
            if (temp.getStatus() == 0) {
                return codeJsonParser.codeJsonParser(Status_5000.DEACTIVE_TEAM.getStatus());
            }

            teamMapper.updateTeam(team);
            return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_UPDATE_TEAM.getStatus());

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_UPDATE_TEAM.getStatus());
        }

    }

    //팀삭제(팀비활성화)
    @Transactional
    public ResponseType deleteTeam(final int teamIdx) {

        Team temp = teamMapper.readTeamDetail(teamIdx);

        if (temp == null) {
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_TEAM.getStatus());
        }

        if (temp.getStatus() == 0) {
            return codeJsonParser.codeJsonParser(Status_5000.DEACTIVE_TEAM.getStatus());
        }
        int delete_flag = teamMapper.deleteTeam(teamIdx);

        if (delete_flag != 0)
            return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_DELETE_TEAM.getStatus());
        else
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_DELETE_TEAM.getStatus());

    }


    //채널조회
    public ResponseType readChannel(final int teamIdx, final int userIdx) {

        List<Channel> channels = channelMapper.readChannelOfUser(teamIdx, userIdx);

        if (channels == null) {
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_CHANNEL.getStatus());
        }
        return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_READ_CHANNEL.getStatus(), channels);
    }

    //비활성화
    @Transactional
    public ResponseType deactiveUser(final int teamIdx, final int userIdx) {

        int teamFlag = teamMapper.deactiveUserOfTeam(teamIdx, userIdx);

        List<Channel> channels = channelMapper.readChannelOfUser(teamIdx, userIdx);

        for (Channel channel : channels) {
            int channelFlag = channelMapper.deactiveUserOfChannel(channel.getIdx(), userIdx);
            if (channelFlag != 1) { //예외처리
                return codeJsonParser.codeJsonParser(Status_5000.FAIL_DEACVITE_USER.getStatus());
            }
        }

        if (teamFlag == 1)
            return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_DEACVITE_USER.getStatus());
        else
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_DEACVITE_USER.getStatus());
    }


    //유저의 팀조회
    public ResponseType readTeamOfUser(final int userIdx) {

        User user = userMapper.findByUserIdx(userIdx);
        if (user == null) {
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_USER.getStatus());
        }

        List<Team> teams = teamMapper.readTeamOfUser(userIdx);
        List<Team> sendteams = new ArrayList<>();

        for (Team team : teams) {
            if (team.getStatus() == 1) {
                sendteams.add(team);
            }

        }

        if (teams == null) {
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_TEAM.getStatus());
        }
        return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_READ_TEAM.getStatus(), sendteams);

    }


    //팀의 유저조회
    public ResponseType readUserOfTeam(final int teamIdx) {

        Team team = teamMapper.readTeamDetail(teamIdx);
        if (team == null) {
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_TEAM.getStatus());
        }

        List<User> users = teamMapper.readUserOfTeam(teamIdx);

        if (users == null) {
            return codeJsonParser.codeJsonParser(Status_5000.FAIL_READ_USER.getStatus());
        }
        return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_READ_USER.getStatus(), users);

    }


    //팀에 초대하기, 메일보내기
    public ResponseType inviteTeam(final Team team) {

        List<User> inviteUsers = team.getUsers();
//        List<Channel> channels = team.getChannels();

        List<User> usersOfTeam = teamMapper.readUserOfTeam(team.getIdx());
        Team teamTemp  = teamMapper.readTeamDetail(team.getIdx());
        List<Channel> channels = channelMapper.readChannelOfTeam(team.getIdx());

        List<User> firstUsers = new ArrayList<>();
        List<User> existUsers = new ArrayList<>();


        for (User inviteUser : inviteUsers) {
            for (int i = 0; i < usersOfTeam.size(); i++) {

                if (inviteUser.getUid().equals(usersOfTeam.get(i).getUid())) {
                    existUsers.add(inviteUser);
                    break;
                }
                if (usersOfTeam.size() - 1 == i) {
                    firstUsers.add(inviteUser);
                }
            }
        }


        System.out.println("firstUsers : "+firstUsers);
        System.out.println("existUsers : "+existUsers);
        if(firstUsers.size()==0){
            return codeJsonParser.codeJsonParser(Status_5000.No_Invite_Member.getStatus());
        }


        for (User user : firstUsers) {

            User userTemp = userMapper.findBysUserid(user.getUid());

            final JwtService.TokenResponse token = new JwtService.TokenResponse(jwtService.create(team.getIdx(), user.getUid()));

            HashMap hashMap = new HashMap();
            hashMap.put("teamIdx", team.getIdx());
            hashMap.put("channelIdx", channels.get(0).getIdx());
            hashMap.put("uid", user.getUid());


            //초대받은팀원이 User가 아닌 경우 - userIdx에 0 넣기
            if (userTemp == null) {

                hashMap.put("userIdx", 0);

            } else {//초대받은팀원이 User인 경우 - userIdx 넣기
                //각테이블에 UserHasTeam 생성 데이터넣기

                hashMap.put("userIdx", userTemp.getIdx());

                teamMapper.createUserHasTeam(team.getIdx(), userTemp.getIdx(), 0, 0);

                channelMapper.createUserHasChannel(channels.get(0).getIdx(), userTemp.getIdx());

            }

            values.putAll(token.getToken(), hashMap);
            values.getOperations().expire(token.getToken(), 1L, TimeUnit.HOURS);


            SendMailThread sendMailThread = new SendMailThread(mailSender, user.getUid(), token.getToken(), teamTemp.getName(), inviteUsers.get(0).getUid());
            sendMailThread.start();

        }



        return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_INVITE.getStatus(), firstUsers, existUsers);

    }

    //초대 승낙
    public String acceptInvite(final String token) {

        String string_teamIdx = (String) values.get(token, "teamIdx");
        String string_userIdx = (String) values.get(token, "userIdx");

        int teamIdx = Integer.parseInt(string_teamIdx);
        int userIdx = Integer.parseInt(string_userIdx);

        if (teamIdx == -1) {
            return "http://localhost:9999/"; //회원가입창 //만료된주소입니다라는 메세지 보내야함
//            return codeJsonParser.codeJsonParser(Status_5000.FAIL_INCORRECT_AUTHKEY.getStatus());
        }
        if (userIdx == 0) {
            return "http://localhost:9999/"; //회원가입창
//            return codeJsonParser.codeJsonParser(Status_5000.PLEASE_SIGNUP.getStatus(), token);
        }
        teamMapper.updateInviteFlag(teamIdx, userIdx);
        return "http://localhost:9999/";
//        return codeJsonParser.codeJsonParser(Status_5000.SUCCESS_ACCEPT_INVITE.getStatus());


    }

    class SendMailThread extends Thread {

        JavaMailSender mailSender;
        String userId;
        String token;
        String teamName;
        String teamOwner;

        public SendMailThread(JavaMailSender mailSender, String userId, String token, String teamName, String teamOwner) {
                this.mailSender = mailSender;
                this.userId = userId;
                this.token = token;
                this.teamName = teamName;
                this.teamOwner = teamOwner;
        }

        public void run() {
            mailSend.mailsend(mailSender, userId, token, teamName, teamOwner);
        }
    }


}
