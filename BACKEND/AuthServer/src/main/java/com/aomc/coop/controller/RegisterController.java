package com.aomc.coop.controller;

import com.aomc.coop.model.User;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.RegisterService;
import com.aomc.coop.utils.CodeJsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api")
public class RegisterController {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();
    @Autowired
    private RegisterService registerService;

    @PostMapping(value = "/register")
    @CrossOrigin
    public ResponseEntity register(@RequestBody User user) { // header, body(json), HTTP.status //
        try {
            return new ResponseEntity(registerService.register(user), HttpStatus.OK);
        } catch (Exception e) {
//            log.error(e.getMessage());
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

}
