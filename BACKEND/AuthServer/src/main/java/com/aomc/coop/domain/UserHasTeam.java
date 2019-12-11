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
@Table(name = "user_has_team")
public class UserHasTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private int idx;

    @ManyToOne
    @JoinColumn(name = "team_idx")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User user;

    @Column(name = "join_date", nullable = false)
    private Date join_date;

    @Column(name = "owner_flag", nullable = false)
    private int owner_flag;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "invite_flag", nullable = false)
    private int invite_flag;
}
