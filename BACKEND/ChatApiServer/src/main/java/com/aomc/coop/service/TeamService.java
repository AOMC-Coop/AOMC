package com.aomc.coop.service;

import com.aomc.coop.mapper.TeamMapper;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TeamService {

    private TeamMapper teamMapper;
    private JwtService jwtService;

    @Autowired
    CodeJsonParser codeJsonParser;

//    @Resource(name="redisTemplate")
//    private HashOperations<String, String, String> values;

    public TeamService(final TeamMapper teamMapper, JwtService jwtService) {
        this.teamMapper = teamMapper;
        this.jwtService = jwtService;
    }


    public ResponseType createTeam() {
//        final List userList = userMapper.findAll();


//        return errorJsonParser.errorJsonParser("1400");
        return codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus());
    }

}
