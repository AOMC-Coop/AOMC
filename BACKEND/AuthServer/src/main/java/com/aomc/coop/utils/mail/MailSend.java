package com.aomc.coop.utils.mail;

import com.aomc.coop.config.MailConfig;
import org.springframework.mail.javamail.JavaMailSender;

public class MailSend {

    public void mailsend(JavaMailSender mailSender, String uid){

        try{
            MailConfig sendMail = new MailConfig(mailSender);
            sendMail.setFrom("Starever222@gmail.com", "CoopDeveloper");
            sendMail.setTo(uid);
            sendMail.setSubject("[Authorization code to sign up]");
            sendMail.setText(new StringBuffer()
                    .append("(<b style=\"text-decoration:none\"></b>) Your authorization code is : <b></b>. Please enter this code to sign up our service!<br/>")
                    .append("<a href='http://localhost:8082/chat")
                    .append("' target='_blenk'>이메일로 초대 수락</a>")
                    .toString());
            sendMail.send();

        }catch (Exception e){

        }

    }

}
