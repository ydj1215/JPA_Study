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
    
    // 1 : n 관계
    @OneToMany(mappedBy = "order") // 연관 관계 소유자가 아님을 의미
    private List<OrderItem> orderItemList = new ArrayList<>();
}
