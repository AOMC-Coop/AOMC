package com.aomc.coop.controller;

import com.aomc.coop.model.Team;
import com.aomc.coop.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(final TeamService teamService) {
        this.teamService = teamService;
    }


    @PostMapping
    @CrossOrigin
    public ResponseEntity makeTeam(@RequestBody final Team team){
        try{
            return new ResponseEntity<>(teamService.createTeam(team), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    @CrossOrigin
    public ResponseEntity updateTeam(@RequestBody final Team team){
        try{
            return new ResponseEntity<>(teamService.updateTeam(team), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
