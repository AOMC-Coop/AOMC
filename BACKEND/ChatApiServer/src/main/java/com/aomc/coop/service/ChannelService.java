package com.aomc.coop.service;

import com.aomc.coop.mapper.ChannelMapper;
import com.aomc.coop.mapper.TeamMapper;
import com.aomc.coop.model.Channel;
import com.aomc.coop.response.Status_1000;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class ChannelService {

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private JwtService jwtService;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    public ResponseType createChannel(Channel channel) {

        if(channel == null) {
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_CREATE_Channel.getStatus());
        }

        int idx = channelMapper.createChannel(channel, channel.getTeamIdx());

        if(idx >= 0) {
            return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_CREATE_Channel.getStatus());
        }else {
            return codeJsonParser.codeJsonParser(Status_1000.FAIL_CREATE_Channel.getStatus());
        }

    }
}
