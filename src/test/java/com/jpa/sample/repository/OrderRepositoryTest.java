package com.jpa.sample.repository;

import com.jpa.sample.constant.ItemSellStatus;
import com.jpa.sample.entity.Item;
import com.jpa.sample.entity.Member;
import com.jpa.sample.entity.Order;
import com.jpa.sample.entity.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger(OrderRepositoryTest.class);

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem(){
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNum(100);

        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(){
        Order order = new Order();
        for(int i = 0; i<3; i++){
            // 상품 생성
            Item item = this.createItem();
            itemRepository.save(item);

            // 주문 상품 정보 입력
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);

            order.getOrderItemList().add(orderItem);
        }
        // 주문 객체를 데이터 베이스에 저장하고 즉시 반영
        orderRepository.saveAndFlush(order); // 특정 경우에만 사용
        em.clear(); // 영속성 상태 초기화 = 객체의 상태를 초기화

        // 주문 엔티티 조회, 즉 데이터 베이스에서 방금 저장한 주문 상품 객체를 조회
        Order saveOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);

        // 주문 객체에 저장된 주문 상품 목록의 크기를 출력
        log.warn("logger: " + saveOrder.getOrderItemList().size());
    }

    public Order createOrder(){
        Order order = new Order();

        for(int i = 0; i<3; i++){
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);

            order.getOrderItemList().add(orderItem);
        }
        // test가 아닌 main이었다면 실제로 member를 만드는게 아닌 find~를 했을 것
        // 테스트를 위한 회원 생성
        Member member = new Member();
        member.setName("유동재");
        member.setEmail("aaa0520@naver.com");
        memberRepository.save(member);

        // 주문한 회원
        order.setMember(member);
        orderRepository.save(order);

        return order;
    }

    @Test
    @DisplayName("고아 객체 제거 테스트")

}









