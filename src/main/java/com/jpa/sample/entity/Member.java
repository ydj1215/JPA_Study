package com.jpa.sample.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "member") // 없으면 클래스 이름으로 테이블 생성
@Getter
@Setter
@ToString
public class Member {
    // 테이블의 기본키 설정 전략을 자동으로 지정
    @Id // id 속성을 기본키로 지정
    @GeneratedValue(strategy = GenerationType.AUTO) // 보통 @Id에 붙으며, 기본키 생성 전략을 자동으로 생
    private Long id;
    
    private String name;
    private String password;

    @Column(unique = true)
    private String email; // 이메일 중복 허용 X

    private LocalDateTime regDate;

    @PrePersist // 데베에 데이터 삽입 전에, 미리 시간 정보를 계산해서 삽입하기 위해 사용
    public void prePersist() {
        regDate = LocalDateTime.now();
    }
}

