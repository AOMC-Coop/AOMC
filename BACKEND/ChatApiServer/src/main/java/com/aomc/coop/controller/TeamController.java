package com.aomc.coop.controller;

import com.aomc.coop.model.Team;
import com.aomc.coop.model.User;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.TeamService;
import com.aomc.coop.utils.CodeJsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;
    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    public TeamController(final TeamService teamService) {
        this.teamService = teamService;
    }

    //팀 생성
    @PostMapping
    @CrossOrigin
    public ResponseEntity makeTeam(@RequestBody final Team team){
        try{
            return new ResponseEntity<>(teamService.createTeam(team), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    //팀조회
    @GetMapping(path="/{teamIdx}")
    @CrossOrigin
    public ResponseEntity makeTeam(@PathVariable(value = "teamIdx") final int teamIdx){
        try{
            return new ResponseEntity<>(teamService.readTeam(teamIdx), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    //팀수정
    @PutMapping
    @CrossOrigin
    public ResponseEntity updateTeam(@RequestBody final Team team){
        try{
            return new ResponseEntity<>(teamService.updateTeam(team), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    //팀비활성화
    @DeleteMapping(path="/{teamIdx}")
    @CrossOrigin
    public ResponseEntity deleteTeam(@PathVariable(value = "teamIdx") final int teamIdx){
        try{
            return new ResponseEntity<>(teamService.deleteTeam(teamIdx), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    //팀원초대
    @GetMapping(path="/invite/{teamIdx}&{uid}")
    @CrossOrigin
    public ResponseEntity inviteTeam(@PathVariable(value = "teamIdx") final int teamIdx, @PathVariable(value = "uid") final String uid){
        try{
            return new ResponseEntity<>(teamService.inviteTeam(teamIdx, uid), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    //채널조회
    @GetMapping(path="/channel/{teamIdx}&{userIdx}")
    @CrossOrigin
    public ResponseEntity readChannel(@PathVariable(value = "teamIdx") final int teamIdx, @PathVariable(value = "userIdx") final int userIdx){
        try{
            return new ResponseEntity<>(teamService.readChannel(teamIdx, userIdx), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    //ownwe만 팀에있는 멤버 비활성화
    @PutMapping(path="/manage/{teamIdx}&{userIdx}")
    @CrossOrigin
    public ResponseEntity deactiveUser(@PathVariable(value = "teamIdx") final int teamIdx, @PathVariable(value = "userIdx") final int userIdx){
        try{
            return new ResponseEntity<>(teamService.deactiveUser(teamIdx, userIdx), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

}
