package com.aomc.coop.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "user_has_channel")
public class UserHasChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private int idx;

    @ManyToOne
    @JoinColumn(name = "channel_idx")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User user;

    @Column(name = "star_flag", nullable = false)
    private int star_flag;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "user_has_last_idx")
    private int user_has_last_idx;

    @Builder
    public UserHasChannel(Channel channel, User user, int star_flag, int status) {
        this.channel = channel;
        this.user = user;
        this.star_flag = star_flag;
        this.status = status;
    }
}
