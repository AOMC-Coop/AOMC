package com.aomc.coop.utils.mail;

import com.aomc.coop.config.MailConfig;
import org.springframework.mail.javamail.JavaMailSender;

public class MailSend {

    public void mailsend(JavaMailSender mailSender, String uid, String authUrl){

        try{
            String url = "http://localhost:8082/members/" + authUrl;
            MailConfig sendMail = new MailConfig(mailSender);
            sendMail.setFrom("Starever222@gmail.com", "CoopDeveloper");
            sendMail.setTo(uid);
            sendMail.setSubject("[Let's finish signing up!]");
            sendMail.setText(new StringBuffer()
                    .append("(<b style=\"text-decoration:none\">Click this button to finish signing up!</b>) <br/>")
                    .append("<a href="+ url + " role='button'>CLICK</a>")
                    .toString());
            sendMail.send();

        }catch (Exception e){
// ***** 시간 남으면 에러 처리 할 것
        }

    }

}
