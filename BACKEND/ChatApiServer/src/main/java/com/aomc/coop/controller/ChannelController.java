package com.aomc.coop.controller;

import com.aomc.coop.model.Channel;
import com.aomc.coop.model.Team;
import com.aomc.coop.service.ChannelService;
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

    @PostMapping
    @CrossOrigin
    public ResponseEntity makeChannel(@RequestBody final Channel channel){
        try{
            return new ResponseEntity<>(channelService.createChannel(channel), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
