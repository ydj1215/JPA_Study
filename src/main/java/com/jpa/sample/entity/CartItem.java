package com.jpa.sample.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@ToString
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cart_item_id")
    private Long id;

    @ManyToOne // n : 1, 하나의 장바구니에는 여러가지 물품들이 담길 수 있다.
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne // n : 1, 하나의 물품은 여러 장바구니에 담기는 것이 가능하다.
    @JoinColumn(name="item_id")
    private Item item;

    private int count;
}
