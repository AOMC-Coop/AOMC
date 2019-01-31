package com.aomc.coop.controller;

import com.aomc.coop.model.User;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.utils.CodeJsonParser;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.springframework.beans.factory.annotation.Autowired;
import com.aomc.coop.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();
    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/login")
    @CrossOrigin
    public ResponseEntity login(@RequestBody User user) { // header, body(json), HTTP.status //
        try {
            return new ResponseEntity(loginService.loginUser(user), HttpStatus.OK);
        } catch (Exception e) {
//            log.error(e.getMessage());
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }
}
