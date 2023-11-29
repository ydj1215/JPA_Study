package com.jpa.sample.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_item")
@ToString
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue // default : auto
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // OrderRepositoryTest와 함께 보기
    // fetch = FetchType.LAZY : 지연 로딩을 설정
    // 즉 OrderItem 엔티티를 조회할 때, Item과 Order 엔티티는 같이 조회되지 않고,
    // orderItem.getOrder()과 같은 코드가 실행될 때, order테이블이 로드된다.

    // 만약 fetch = FetchType.EAGER로 설정한다면, OrderItem을 조회할 때, Item과 Order이 함께 조회되며, 이를 즉시 로딩이라고 한다.
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice;
    private int count;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}
