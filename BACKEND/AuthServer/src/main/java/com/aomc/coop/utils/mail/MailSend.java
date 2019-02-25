package com.aomc.coop.utils.mail;

import com.aomc.coop.config.MailConfig;
import org.springframework.mail.javamail.JavaMailSender;

public class MailSend {

    public void mailsend(JavaMailSender mailSender, String uid, String authUrl){

        try{
            String invite_token = "invitation";
// ***** 아래 URL 양식에 맞게 바꿀 것 (필요하면)
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

        }catch (Exception e){
// ***** 시간 남으면 에러 처리 할 것
        }

    }

    public void mailsendToInvite(JavaMailSender mailSender, String uid, String authUrl, String invite_token){

        try{
// ***** RequestParam에 맞는 양식으로 변경함
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

        }catch (Exception e){
// ***** 시간 남으면 에러 처리 할 것
        }

    }

//    public void mailsendForMissingPwd(JavaMailSender mailSender, String uid, int idx){
//
//        try{
//            String url = "http://localhost:8082/members/pwd/" + idx;
//            MailConfig sendMail = new MailConfig(mailSender);
//            sendMail.setFrom("Starever222@gmail.com", "CoopDeveloper");
//            sendMail.setTo(uid);
//            sendMail.setSubject("[Let's change password!]");
//            sendMail.setText(new StringBuffer()
//                    .append("(<b style=\"text-decoration:none\">Click link below to change password!</b>) <br/>")
//                    .append("<a href="+ url + " role='button'>CLICK</a>")
//                    .toString());
//            sendMail.send();
//
//        }catch (Exception e){
//// ***** 시간 남으면 에러 처리 할 것
//        }
//
//    }
}
