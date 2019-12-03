package com.aomc.coop.controller;

import com.aomc.coop.dto.Profile;
import com.aomc.coop.dto.User;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.ProfileService;
import com.aomc.coop.utils.CodeJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    /**
     *
     *        @brief GET http://localhost:8082/api/profile/{idx}
     *        @details 유저의 프로필 조회 요청을 처리하는 함수
     *        @param RequestBody UserWithToken userWithToken, PathVariable(value = "idx") int idx
     *        @return ResponseEntity<>
     *
     *        성공시
     *
     *        실패시
     *
     *        @throws

     *

     */

// *** 원래는 @GetMapping이 맞는데, vue에서 axios.get으로는 UserWithToken을 전달할 수 없다. 때문에 @PostMapping으로 바꿈... @GetMapping으로 돌아가게 할 수는 없을까?
// *** 토큰을 헤더에 담아서 전송하면 해결 될 것.
    @PostMapping(path="/{idx}")
    public ResponseEntity getProfile(@RequestBody User userWithToken, @PathVariable(value = "idx") int idx) {
        try {
            return new ResponseEntity(profileService.getProfile(userWithToken, idx), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**
     *
     *        @brief GET http://localhost:8082/api/profile/{idx}
     *        @details 유저의 프로필 수정 요청을 처리하는 함수
     *        @param RequestBody ProfileWithToken profileWithToken, PathVariable(value = "idx") int idx
     *        @return ResponseEntity<>
     *
     *        성공시
     *        {
     *              "status": 200,
     *              "message": "프로필 사진 업로드 성공",
     *              "description": "Success Profile Picture Upload"
     *        }
     *
     *        실패시
     *        {
     *              "status": 400,
     *              "message": "프로필 사진 업로드 실패",
     *              "description": "Fail Profile Picture Upload"
     *        }
     *
     *        @throws
     *

     */

    @PutMapping(path="/{idx}")
    public ResponseEntity setProfile(@RequestBody Profile profileWithToken, @PathVariable(value = "idx") int idx) {
        try {
            return new ResponseEntity(profileService.setProfile(profileWithToken, idx), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }
}
