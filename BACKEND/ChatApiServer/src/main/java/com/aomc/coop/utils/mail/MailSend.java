package com.aomc.coop.utils.mail;

import com.aomc.coop.config.MailConfig;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.utils.CodeJsonParser;
import org.springframework.mail.javamail.JavaMailSender;

public class MailSend {

    public void mailsend(JavaMailSender mailSender, String uid, String token){

        try{
            MailConfig sendMail = new MailConfig(mailSender);
            sendMail.setFrom("dmsal2525@gmail.com", "coopDeveloper");
            sendMail.setTo(uid);
            sendMail.setSubject("[You invited to join a Coop workspace]");
            sendMail.setText(new StringBuffer().append("<h1>Join on Coop</h1><br/>").append("아래 링크를 클릭하면 초대가 수락됩니다.<br/>").append("<a href='http://localhost:8083/api/team/accept/").append(token).append("' target='_blenk'>이메일로 초대 수락</a>").toString());
//            sendMail.setText(new StringBuffer().append("http://localhost:8083/api/team/accept/").append(token).toString());
            sendMail.send();

        }catch (Exception e){

        }

    }

}
