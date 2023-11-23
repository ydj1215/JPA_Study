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
