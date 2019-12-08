package com.aomc.coop.service;

import com.aomc.coop.dto.ProfileResponse;
import com.aomc.coop.mapper.UserMapper;
import com.aomc.coop.dto.ProfileRequest;
import com.aomc.coop.response.Status_3000;
import com.aomc.coop.utils.CodeJsonParser;
import com.aomc.coop.utils.ResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    private final UserMapper userMapper;

    @Resource(name="redisTemplate")
    private HashOperations<String, String, Object> hashOperations;

    public ResponseType getProfile(String token, @RequestBody ProfileRequest profileRequest, int idx) {

        int userIdx = profileRequest.getIdx();
        if(idx == userIdx) {
            Map userInfo = hashOperations.entries(token);

            String uid = (String) userInfo.get("uid");
            String nickname = (String) userInfo.get("nickname");
            String image_url = (String) userInfo.get("image_url");

            ProfileResponse profileResponse = new ProfileResponse();
            profileResponse.setUid(uid);
            profileResponse.setNickname(nickname);
            profileResponse.setImage_url(image_url);


            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Get_Profile.getStatus(), profileResponse);
        } else {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Get_Profile_Wrong_Idx.getStatus());
        }
    }

    public ResponseType setProfile(String token, @RequestBody ProfileRequest profileRequest, int idx) {

        int userIdx = profileRequest.getIdx();
        if(idx == userIdx) {
            Map userInfo = hashOperations.entries(token);
            String newNickname = profileRequest.getNickname();

            userInfo.replace("nickname", newNickname);
            hashOperations.putAll(token, userInfo);

            userMapper.updateUserInfo(newNickname, idx);

            // *** 클라이언트 단에서는 Vue에서 입력한 정보로 바로 수정된 내역이 출력되도록 하자. 때문에 여기서 response에 담아서 줄 필요 X
            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Set_Profile.getStatus());
        } else {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Set_Profile_Wrong_Idx.getStatus());
        }
    }
}
