package com.aomc.coop.controller;

import com.aomc.coop.dto.ProfileRequest;
import com.aomc.coop.service.ProfileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     *
     *        @brief GET http://localhost:8082/api/profile/{idx}
     *        @details 유저의 프로필 조회 요청을 처리하는 함수
     *        @param @RequestBody UserWithToken userWithToken, PathVariable(value = "idx") int idx
     *        @return ResponseEntity<>
     *
     *        성공시
     *
     *        실패시
     *
     *        @throws

     *

     */

    @ApiOperation(value = "유저의 프로필 조회 요청을 처리", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로필 조회 성공"),
            @ApiResponse(code = 400, message = "프로필 조회 실패")
    })
    @GetMapping(path="/{idx}")
    public ResponseEntity getProfile(HttpServletRequest request, @RequestBody ProfileRequest profileRequest, @PathVariable(value = "idx") int idx) {
        String token = request.getHeader("X-Auth-Token");
        return new ResponseEntity(profileService.getProfile(token, profileRequest, idx), HttpStatus.OK);
    }

    /**
     *
     *        @brief GET http://localhost:8082/api/profile/{idx}
     *        @details 유저의 프로필 수정 요청을 처리하는 함수
     *        @param @RequestBody ProfileWithToken profileWithToken, PathVariable(value = "idx") int idx
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

    @ApiOperation(value = "유저의 프로필 수정 요청을 처리", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로필 사진 업로드 성공"),
            @ApiResponse(code = 400, message = "프로필 사진 업로드 실패")
    })
    @PutMapping(path="/{idx}")
    public ResponseEntity setProfile(HttpServletRequest request, @RequestBody ProfileRequest profileRequest, @PathVariable(value = "idx") int idx) {
        String token = request.getHeader("X-Auth-Token");
        return new ResponseEntity(profileService.setProfile(token, profileRequest, idx), HttpStatus.OK);
    }
}
