package com.aomc.coop.repository;

import com.aomc.coop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // *** 추후 Optional<User>로 바꿔보기
    User findByUid(String uid);
}
