package com.aomc.coop.utils.mail;


import com.aomc.coop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class MailsendUtil {

    @Autowired
    private MailSender sender;

    public void mailsend(String uid, String token){

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("CoopDeveloper");
        msg.setTo(uid);
        msg.setSubject("[COOP Team 초대 이메일 인증]");
        msg.setText(new StringBuffer().append("<h1>메일인증</h1>").append("<a href='http://localhost:8083/api/team/accept/").append(token).append("' target='_blenk'>이메일 인증 확인</a>").toString());
        this.sender.send(msg);

    }
}
