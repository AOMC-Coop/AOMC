package com.aomc.coop.service;

import com.aomc.coop.mapper.UserMapper;
import com.aomc.coop.dto.Profile;
import com.aomc.coop.dto.User;
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
//    profile 프로필 회원수정 비밀번호 찾기
    CodeJsonParser codeJsonParser = CodeJsonParser.getInstance();

    private final UserMapper userMapper;
    private final JwtService jwtService;

    @Resource(name="redisTemplate")
    private HashOperations<String, String, Object> hashOperations;

    public ResponseType getProfile(@RequestBody User user, int idx){

        int userIdx = user.getIdx();
        if(idx == userIdx) {
            // String token;
            Map userInfo = hashOperations.entries(user.getLogin_token());

            String uid = (String) userInfo.get("uid");
            String nickname = (String) userInfo.get("nickname");

            Profile profile = new Profile();
            profile.setUid(uid);
            profile.setNickname(nickname);
// *** 프로필 사진 정보 추가해야 함

            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Get_Profile.getStatus(), profile);
        } else {
            // URL로 다른 유저의 idx 요청을 보냈다면
// *** 토큰 정보를 활용하여
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Get_Profile_Wrong_Idx.getStatus());
        }

    }

    // 프로필 수정
    public ResponseType setProfile(@RequestBody Profile profile, int idx){

        // URL로 들어온 idx와 토큰에 저장된 idx가 같아야 함 : 유저는 자기 idx에 해당하는 자기 정보만 수정할 수 있어야 함
        int userIdx = profile.getIdx();
        if(idx == userIdx) {
            // *** 토큰 헤더로 받아오기
            String token = "tmp";
            Map userInfo = hashOperations.entries(token);
            String newNickname = profile.getNickname();

            // redis 정보 수정
            userInfo.replace("nickname", newNickname);
// *** 기존 key에 있는 정보 삭제하던가, nickname만 잘 바꾸던가
            hashOperations.putAll(token, userInfo);

            // db 정보 수정
            userMapper.updateUserInfo(newNickname, idx);

            // *** 클라이언트 단에서는 Vue에서 입력한 정보로 바로 수정된 내역이 출력되도록 하자. 때문에 여기서 response에 담아서 줄 필요 X
            return codeJsonParser.codeJsonParser(Status_3000.SUCCESS_Set_Profile.getStatus());
        } else {
            return codeJsonParser.codeJsonParser(Status_3000.FAIL_Set_Profile_Wrong_Idx.getStatus());
        }
    }
}
