package com.jpa.sample.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// 요청과 응답에 대한 객체
public class MemberDto {
    private String email;
    private String name;
    private String password;
    private LocalDateTime regDate;
}
