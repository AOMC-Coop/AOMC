package com.aomc.coop.domain;

import com.aomc.coop.dto.NewPasswordRequest;
import com.aomc.coop.dto.ProfileRequest;
import com.aomc.coop.dto.WithdrawalRequest;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private int idx;

    @Column(name = "uid", unique = true, nullable = false, length = 45)
    private String uid;

    @Column(name = "pwd", nullable = false, length = 80)
    private String pwd;

    @Column(name = "salt", nullable = false, length = 45)
    private String salt;

    @Column(name = "nickname", nullable = false, length = 45)
    private String nickname;

    @Column(name = "role", nullable = false)
    private int role;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "reg_date", nullable = false, updatable = false)
    @CreationTimestamp
    private Date reg_date;

    // access_date에 해당하는 jpa annotation은 없을지
    @Column(name = "access_date", nullable = false)
    @UpdateTimestamp
    private Date access_date;

    @Column(name = "update_date", nullable = false)
    @UpdateTimestamp
    private Date update_date;

    @Column(name = "image")
    private String image;

    @Builder
    public User(String uid, String pwd, String salt, String nickname, int role, int status, String image) {
        this.uid = uid;
        this.pwd = pwd;
        this.salt = salt;
        this.nickname = nickname;
        this.role = role;
        this.status = status;
        this.image = image;
    }

    public void updateMyNickName(ProfileRequest profileRequest) {
        this.nickname = profileRequest.getNickname();
    }

    public void withdrawalMyUser(WithdrawalRequest withdrawalRequest) {
        this.status = withdrawalRequest.getStatus();
    }

    public void changePassword(String newHashPassword, String newSalt) {
        this.pwd = newHashPassword;
        this.salt = newSalt;
    }
}
