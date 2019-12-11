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
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private int idx;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "create_date", nullable = false, updatable = false)
    @CreationTimestamp
    private Date create_date;

    @Column(name = "update_date", nullable = false)
    @UpdateTimestamp
    private Date update_date;

    @Column(name = "status")
    private int status;
}
