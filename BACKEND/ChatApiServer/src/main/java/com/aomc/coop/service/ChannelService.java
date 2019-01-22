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

        channelMapper.createChannel(channel, team.getIdx(), user.getIdx());

        // 성공
        return codeJsonParser.codeJsonParser(Status_1000.SUCCESS_CREATE_TEAM.getStatus());

        // 실패
        //return codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus());
    }
}
