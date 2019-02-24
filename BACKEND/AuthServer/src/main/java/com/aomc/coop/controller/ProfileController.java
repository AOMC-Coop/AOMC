package com.aomc.coop.controller;

import com.aomc.coop.model.ProfileWithToken;
import com.aomc.coop.model.User;
import com.aomc.coop.model.UserWithToken;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.ProfileService;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    @Autowired
    private ProfileService profileService;

    // 프로필 조회
// ***** 원래는 @GetMapping이 맞는데, vue에서 axios.get으로는 UserWithToken을 전달할 수 없다. 때문에 @PostMapping으로 바꿈... @GetMapping으로 돌아가게 할 수는 없을까?
// ***** 토큰을 헤더에 담아서 전송하면 해결 될 것.
//    @Auth
    @PostMapping(path="/{idx}")
    @CrossOrigin
    public ResponseEntity getProfile(@RequestBody UserWithToken userWithToken, @PathVariable(value = "idx") int idx) { // header, body(json), HTTP.status //
        try {
            return new ResponseEntity(profileService.getProfile(userWithToken, idx), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    // 프로필 수정
//    @Auth
    @PutMapping(path="/{idx}")
    @CrossOrigin
    public ResponseEntity setProfile(@RequestBody ProfileWithToken profileWithToken, @PathVariable(value = "idx") int idx) { // header, body(json), HTTP.status //
        try {
            return new ResponseEntity(profileService.setProfile(profileWithToken, idx), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }
}
