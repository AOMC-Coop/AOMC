package com.aomc.coop.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "users")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private int idx;

    @Column(name = "content", unique = true, nullable = false, length = 100)
    private String content;

    @Column(name = "type", nullable = false, length = 10)
    private String type;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Builder
    public File(String content, String type, String name) {
        this.content = content;
        this.type = type;
        this.name = name;
    }
}
