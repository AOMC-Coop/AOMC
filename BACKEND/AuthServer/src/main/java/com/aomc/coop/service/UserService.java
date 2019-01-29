package com.aomc.coop.service;

import java.util.List;
import com.aomc.coop.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aomc.coop.model.User;

@Service // 비지니스 메소드를 별도의 Service 객체에서 구현하도록 하고 Controller는 Service 객체를 사용하도록 합니다.
// 그리고 이제 Service 레이어가 Repository 레이어에 접근
// *** Admin Server기능이 여기 있으므로, 주석 처리한 부분을 추후 분리할 것
// *** parameter로 user를 안써도 되는 함수에서는 String userId로 통일하자
public class UserService {

    @Autowired
    private UserMapper userMapper;

    // my page 보는 용도
    public User getUser(String userId) { return userMapper.getUser(userId); }

    public boolean insertUser(User user) { return userMapper.insertUser(user); }

    // admin server
    // public List<User> getAllUsers() { return userMapper.getAllUsers(); }

    // admin server
    // public boolean deleteUser(String userId) { return userMapper.deleteUser(userId); }

    // mysql query문으로 자동 업데이트 될 것임 : 아니면 추후 복구
    // public boolean updateAccess_date(User user) { return userMapper.updateAccess_date(user); }
}
