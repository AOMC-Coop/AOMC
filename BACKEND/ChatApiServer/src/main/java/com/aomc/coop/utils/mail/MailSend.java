package com.aomc.coop.utils.mail;

import com.aomc.coop.config.MailConfig;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.utils.CodeJsonParser;
import org.springframework.mail.javamail.JavaMailSender;

public class MailSend {

    public void mailsend(JavaMailSender mailSender, String uid, String token, String teamName, String teamOwner){

        try{
            MailConfig sendMail = new MailConfig(mailSender);
            sendMail.setFrom("dmsal2525@gmail.com", "CoopDeveloper");
            sendMail.setTo(uid);
            sendMail.setSubject("[You are invited to join a Coop workspace]");
            String imgHtml = "<img src=\"https://github.com/AOMC-Coop/AOMC/blob/master/image/coop_logo2.png?raw=true\" width=\"50\" height=\"50\">";
            sendMail.setText(new StringBuffer().append(imgHtml + "<h1>Join " + teamName + " on Coop</h1><br/>")
                    .append("(<b style=\"text-decoration:none\">" + teamOwner + "</b>) has invited you to join the Slack workspace <b>" + teamName + "</b>. Join now to start collaborating!<br/>")
                    .append("<a href='http://localhost:8083/api/team/accept/")
                    .append(token)
                    .append("' target='_blenk'>이메일로 초대 수락</a>")
                    .toString());
            sendMail.send();

        }catch (Exception e){

        }

    }

}
