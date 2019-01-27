package com.aomc.coop.controller;

import com.aomc.coop.model.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.aomc.coop.utils.CodeJsonParser;

// ***** 얘를 login.vue에 맞게 고쳐야 함
@RestController
public class HomeController {
// *** AuthSystem-APIServer의 HomeController와 다른 점은 무엇인지?
    //test 주석입니다
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void home() { //test function
        System.out.println("TEST");
        UserInfo userInfo = new UserInfo();
        String userId = userInfo.getUserId();
    }

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();
}
