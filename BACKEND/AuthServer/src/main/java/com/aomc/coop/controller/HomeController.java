package com.aomc.coop.controller;

import com.aomc.coop.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.aomc.coop.utils.CodeJsonParser;

// ***** 얘를 login.vue에 맞게 고쳐야 함
@RestController
public class HomeController {
// 이 HomeController와 AuthSystem-APIServer의 HomeController -> 둘이 환경이 아예 달라서 둘 다 있어야 함. 중복이 있을지라도.
    //test 주석입니다
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void home() { //test function
        System.out.println("TEST");
        User user = new User();
        String uid = user.getUid();
    }
    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();
}
