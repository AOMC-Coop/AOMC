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

    // File 함수를 여기에 작성 가능 (객체지향 : 객체 본인의 책임을 다한다. 객체에 대한 정보는 객체가 제공한다.)
}
