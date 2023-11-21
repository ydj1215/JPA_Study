package com.jpa.sample.entity;

import com.jpa.sample.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity // ORD
@Table(name = "item") // Entity와 Mapping할 테이블 지정
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 상품 코드

    @Column(nullable = false, length = 50) // Not NULL, 글자수 제한
    private String itemName; // 상품명

    @Column(name = "price", nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private int stockNum; // 재고 수량

    @Lob // CLOB
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING) // enum으로 정의된 값을 실제 문자열 타입으로 저장
    // EnumType.ORDINAL : 실제 문자열이 아닌, 1 혹은 2, 일련번호로 저장
    private ItemSellStatus itemSellStatus; // SELL || SOLD_OUT

    private LocalDateTime regTime; // 등록 시
    private LocalDateTime updateTime; // 수령 시간
}
