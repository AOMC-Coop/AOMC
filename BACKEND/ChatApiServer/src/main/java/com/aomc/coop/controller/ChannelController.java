package com.aomc.coop.controller;

import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Team;
import com.aomc.coop.response.Status_1000;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.ChannelService;
import com.aomc.coop.utils.CodeJsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    @PostMapping
    @CrossOrigin
    public ResponseEntity makeChannel(@RequestBody final Channel channel){

        if(channel != null) {
            return new ResponseEntity<>(channelService.createChannel(channel), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);

        }
    }

    @PutMapping
    @CrossOrigin
    public ResponseEntity updateChannel(@RequestBody final Channel channel){
        if(channel != null) {
            //return new ResponseEntity<>(channelService.updateChannel(channel), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
        return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);

    }
}
