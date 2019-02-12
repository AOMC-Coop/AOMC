package com.aomc.coop.controller;

import com.aomc.coop.model.Channel;
import com.aomc.coop.response.Status_1000;
import com.aomc.coop.response.Status_common;
import com.aomc.coop.service.ChannelService;
import com.aomc.coop.utils.CodeJsonParser;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**

 *

 * @brief API http://localhost:8083/api/channel

 * @details 채널을 생성, 수정, 삭제, 메세지 조회, 멤버초대 하는 클래스이다.

 * @author 이윤재

 * @date 2018-01-23

 * @version 1.0.0

 *

 */
@Slf4j
@RestController
@RequestMapping("/api/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    /**

     *

     *        @brief POST http://localhost:8083/api/channel

     *        @details 채널을 생성하는 함수
     *        test json
     *
     *        {
     * 	        "name" : "aomc-1",
     * 	        "teamIdx" : 23,
     * 	        "users":[
     *     	        {"idx":5}
     *     	    ]
     *        }

     *        @param RequestBody final Channel channel

     *        @return ResponseEntity<>

     *        성공시
     *
     *        {
     *          "status": 200,
     *          "message": "채널 생성 성공",
     *          "description": "Success Channel Create"
     *        }

     *        실패시

     *        {
     *           "status": 400,
     *           "message": "채널 생성 실패",
     *           "description": "Fail Channel Create"
     *        }

     *

     *        @throws

     *

     */
    @PostMapping
    public ResponseEntity makeChannel(@RequestBody final Channel channel){

        if(channel != null) {
            return new ResponseEntity<>(channelService.createChannel(channel), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

     /**

     *

     *        @brief PUT http://localhost:8083/api/channel

     *        @details 채널을 수정하는 함수
     *        test json
     *
     *        {
     * 	        "idx" : 22
     *        }

     *        @param RequestBody final Channel channel

     *        @return ResponseEntity<>

     *        성공시
     *
     *        {
     *           "status": 200,
     *           "message": "채널 수정 성공",
     *           "description": "Success Channel Update"
     *        }

     *        실패시

     *        {
     *           "status": 400,
     *           "message": "채널 수정 실패",
     *           "description": "Fail Channel Update"
     *        }

     *

     *        @throws

     *

     */
    @PutMapping
    public ResponseEntity updateChannel(@RequestBody final Channel channel){
        if(channel != null) {
            return new ResponseEntity<>(channelService.updateChannel(channel), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**

     *

     *        @brief GET http://localhost:8083/api/channel/message?channelIdx=21

     *        @details 채널의 메세지를 가져오는 함수

     *        @param RequestParam("channelIdx") final int channelIdx

     *        @return ResponseEntity<>

     *        성공시
     *
     *        {
     *           "status": 200,
     *           "message": "메세지 조회 성공",
     *           "description": "Success Message Get"
     *           "data": [
     *              {
     *             "idx": 14,
     *             "content": "hello"
     *              },
     *              {
     *             "idx": 15,
     *             "content": "hello2"
     *              }
     *           ]
     *        }

     *        실패시

     *        {
     *           "status": 400,
     *           "message": "메세지 조회 실패",
     *           "description": "Fail Message Get"
     *        }

     *

     *        @throws

     *

     */
    @GetMapping
    @RequestMapping("/message")
    public ResponseEntity getChannelMessage(@RequestParam("channelIdx") final int channelIdx, @RequestParam("start") final int start, @RequestParam("messageLastIdx") final int messageLastIdx){
        if(channelIdx >= 0) {
            return new ResponseEntity<>(channelService.getChannelMessage(channelIdx, start, messageLastIdx), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**

     *

     *        @brief GET http://localhost:8083/api/channel/users?channelIdx=19

     *        @details 채널의 멤버를 모두 가져오는 함수

     *        @param RequestParam("channelIdx") final int channelIdx

     *        @return ResponseEntity<>

     *        성공시
     *
     *        {
     *           "status": 200,
     *          "message": "채널 멤버 조회 성공",
     *          "description": "Success Channel Users Get",
     *          "data": [
     *              {
     *                   "idx": 4,
     *                  "uid": "dmsal",
     *                  "nickname": "mooming",
     *                  "gender": 0,
     *                  "role": 0,
     *                   "status": "1"
     *               },
     *              {
     *                   "idx": 5,
     *                   "uid": "yunjae",
     *                   "nickname": "yunyun",
     *                   "gender": 0,
     *                   "role": 0,
     *                   "status": "1"
     *               }
     *          ]
     *      }

     *        실패시

     *        {
     *           "status": 400,
     *           "message": "채널 멤버 조회 실패",
     *           "description": "Fail Channel Users Get"
     *        }

     *

     *        @throws

     *

     */
    @GetMapping
    @RequestMapping("/users")
    public ResponseEntity getChannelUsers(@RequestParam("channelIdx") final int channelIdx){
        if(channelIdx >= 0) {
            return new ResponseEntity<>(channelService.getChannelUsers(channelIdx), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**

     *

     *        @brief POST http://localhost:8083/api/channel/invite

     *        @details 채널에 멤버를 초대하는 함수
     *        test json
     *        {
     * 	        "idx":81,
     * 	        "users":[
     * 		        {
     * 			        "idx": 9
     * 		        },
     * 		        {
     * 			        "idx": 16
     * 		        }
     * 	        ]
     *      }

     *        @param @RequestBody Channel channel

     *        @return ResponseEntity<>

     *        성공시
     *
     *        {
     *          "status": 200,
     *          "message": "채널 멤버 초대 성공",
     *          "description": "Success Channel User Invite"
     *        }
     *
     *        이미 멤버가 있을시
     *
     *        {
     *          "status": 401,
     *          "message": "채널에 이미 멤버가 있음",
     *          "description": "Channel Already Has User"
     *       }

     *        실패시

     *        {
     *           "status": 400,
     *           "message": "채널 멤버 초대 실패",
     *           "description": "Fail Channel User Invite"
     *        }

     *

     *        @throws

     *

     */
    @PostMapping
    @RequestMapping("/invite")
    public ResponseEntity inviteChannelUser(@RequestBody Channel channel){

        if(channel != null) {
            return new ResponseEntity<>(channelService.inviteChannelUser(channel), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    /**

     *

     *        @brief DELETE http://localhost:8083/api/channel/19/6

     *        @details 멤버가 채널 나가기 하는 함수

     *        @param RequestParam(value = "channelIdx") int channelIdx

     *        @param RequestParam(value = "userIdx") int userIdx

     *        @return ResponseEntity<>

     *        성공시
     *
     *        {
     *          "status": 200,
     *          "message": "채널 나가기 성공",
     *          "description": "Success Channel User Delete"
     *        }

     *        실패시

     *        {
     *           "status": 400,
     *           "message": "채널 나가기 실패",
     *           "description": "Fail Channel User Delete"
     *        }

     *

     *        @throws

     *

     */
    @DeleteMapping
    public ResponseEntity deleteChannelUser(@RequestParam(value = "channelIdx") int channelIdx, @RequestParam(value = "userIdx") int userIdx){

        if(channelIdx >= 0 || userIdx >= 0) {
            return new ResponseEntity<>(channelService.deleteChannelUser(channelIdx, userIdx), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

    @GetMapping
    @RequestMapping("/star")
    public ResponseEntity updateUserHasChannelStar(@RequestParam(value = "channelIdx") int channelIdx, @RequestParam(value = "userIdx") int userIdx, @RequestParam(value = "starFlag") int starFlag) {
        if(channelIdx >= 0 || userIdx >= 0) {
            return new ResponseEntity<>(channelService.updateUserHasChannelStar(channelIdx, userIdx, starFlag), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(codeJsonParser.codeJsonParser(Status_common.INTERNAL_SERVER_ERROR.getStatus()), HttpStatus.OK);
        }
    }

}
