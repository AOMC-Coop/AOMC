package com.aomc.coop.utils.mail;

import com.aomc.coop.config.MailConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MailSend {

    public void mailsend(JavaMailSender mailSender, String uid, String authUrl){

        try{
            String invite_token = "invitation";
            String url = "http://localhost:8082/api/members/eauth/" + authUrl + "/" + invite_token;
            MailConfig sendMail = new MailConfig(mailSender);
            sendMail.setFrom("Starever222@gmail.com", "CoopDeveloper");
            sendMail.setTo(uid);
            sendMail.setSubject("[Let's finish signing up!]");
            sendMail.setText(new StringBuffer()
                    .append("(<b style=\"text-decoration:none\">Click link below to finish signing up!</b>) <br/>")
                    .append("<a href="+ url + " role='button'>CLICK</a>")
                    .toString());
            sendMail.send();

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void mailsendToInvite(JavaMailSender mailSender, String uid, String authUrl, String invite_token){

        try{
            String url = "http://localhost:8082/api/members/eauth?authUrl="+authUrl + "&invite_token=" + invite_token;
            MailConfig sendMail = new MailConfig(mailSender);
            sendMail.setFrom("Starever222@gmail.com", "CoopDeveloper");
            sendMail.setTo(uid);
            sendMail.setSubject("[Let's finish signing up!]");
            sendMail.setText(new StringBuffer()
                    .append("(<b style=\"text-decoration:none\">Click link below to finish signing up!</b>) <br/>")
                    .append("<a href="+ url + " role='button'>CLICK</a>")
                    .toString());
            sendMail.send();

        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
