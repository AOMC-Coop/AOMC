package com.aomc.coop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aomc.coop.dao.UserDao;
import com.aomc.coop.model.User;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getUser(String userId) {
        return userDao.getUser(userId);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public boolean insertUser(User user) { return userDao.insertUser(user); }

    public boolean deleteUser(String userId) { return userDao.deleteUser(userId); }

    public boolean updateAccess_date(User user) { return userDao.updateAccess_date(user); } // *** parameter를 String userId로 통일하는 건 어떨까?

    //public User getUser(User user) { return userDao.getUser(user); }
}
