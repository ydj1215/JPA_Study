package com.jpa.sample.entity;

import com.jpa.sample.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // order 오류 발생
@ToString
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue // default : auto
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime regTime;
    private LocalDateTime updateTime;
    
    // 1 : n 관계 : 하나의 order에 여러가지 orderItem이 포함될 수 있다.
    // mappedBy : 연관 관계 소유자(=외래키를 관리하는 쪽)가 아님을 의미
    // 주인이 아닌 쪽은 읽기만 가능
    // cascade : 영속성 전이 = 부모 엔티티의 생명 주기에 따라 자식 엔티티의 생명 주기를 관리하고 싶을 때 사용
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) // 고아 객체도 알아서 다 사라지게 설정
    private List<OrderItem> orderItemList = new ArrayList<>(); // 즉 orderItem쪽에서 외래키를 관리
    // 양방향 관계를 왜 설정하는 것인가? : 한꺼번에 무언가를 하고 싶을때, 예를 들자면 고아 객체 제거, 이는 한꺼번에 제거를 의미한다.

}
