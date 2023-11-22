package com.jpa.sample.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart") // 없으면 클래스 명으로 테이블 생성
@Getter
@Setter
@ToString
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // 보통 @Id에 붙으며, 기본키 생성 전략을 자동으로 생
    @Column(name="cart_id")
    private Long id;

    private String cartName;

    @OneToOne // 회원 엔티티와 1 : 1 매핑
    @JoinColumn(name="member")
    private Member member;


}
