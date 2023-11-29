package com.jpa.sample.repository;

import com.jpa.sample.constant.ItemSellStatus;
import com.jpa.sample.entity.Item;
import com.jpa.sample.entity.Member;
import com.jpa.sample.entity.Order;
import com.jpa.sample.entity.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
//@Commit // 트랙잭션 자동 롤백 취소
class OrderRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger(OrderRepositoryTest.class);

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
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
    public void cascadeTest() {
        Order order = new Order();
        for (int i = 0; i < 3; i++) {
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

    public Order createOrder() {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());

        for (int i = 0; i < 3; i++) {
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
    public void orphanRemovalTest() {
        Order order = this.createOrder();
        order.getOrderItemList().remove(0); // 배열을 지우면 데이터 베이스에서 지워질까?
        em.flush();
    }

    // 지연 로딩 : JPA에서 제공하는 기능으로, 연관된 엔티티를 실제로 사용하는 순간에 데이터베이스로부터 로드하는 방법
    // 지연 로딩은 불필요한 데이터 베이스 접근을 줄여, 성능적인 향상을 시켜준다.
    @Test
    @DisplayName("지연 로딩 테스트") // LAZY 전략
    public void lazyLoadingTest() {
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItemList().get(0).getId(); // 아이템 주문 리스트의 첫번째 아이템을 가져온다.
        em.flush(); // 영속성 컨텍스트를 즉시 데이터 베이스에 적용
        em.clear();  // 영속성 컨텍스트 비워주기

        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);

        // 지연 로딩 작동 확인을 위한 로깅
        log.warn("로거 : " + orderItem.getOrder().getClass()); // OrderItem 객체의 order 필드의 실제 클래스 확인
        /*
        지연 로딩이 적용되면, order 필드는 실제 Order 객체 대신 프록시 객체를 가지게 된다.
        프록시 객체는 실제 엔티티 객체에 대한 참조를 유지하면서,
        실제 엔티티의 데이터가 필요한 시점에 데이터베이스에서 데이터를 로드하는 역할을 한다.
        따라서 이 로그 구문을 통해서 order 필드가 프록시 객체를 가지고 있는지 확인할 수 있다.
        */

        /*
        프록시의 정의 : 실제 엔티티에 대한 참조만을 가지고 있는 가짜 객체로서,
        실제로 해당 엔티티에 접근해야 할 경우에만 데이터 베이스에서 데이터를 로드한다.
        */

        /*
        지연 로딩을 사용할 경우, 처음에는 프록시 객체만 생성되고 그 프록시 객체가 실제 엔티티의 데이터를 갖고 있지는 않다.
        이 프록시 객체는 실제 엔티티에 대한 참조만을 가지고 있다.
        그리고 실제로 그 엔티티의 데이터에 접근해야 할 경우, 즉 아래의 코드가 실행될때와 같은 경우에,
        그제서야 데이터 베이스에서 실제 데이터를 조회하여 프록시 객체에 로드하게 된다.
        */
        log.warn("지연 로딩 시간 확인 : " + orderItem.getOrder().getOrderDate());

        /*
        예를 들자면 order가 member를 참조하고 있을 때,
        order객체를 호출하면 member의 실제 데이터는 불러와지지 않지만,
        프록시가 ("이따가 member데이터를 가져올수도 있겠다" = member객체에 대한 참조) 는 알고 있는 것이다.
        또한, JPA 에서 프록시 객체는 실제 엔티티 클래스를 상속받아, 실제 엔티티 클래스의 메서드를 오버라이드하고 있다.
        고로 getOrderId() 와 같은 메소드를 호출했을때, 프록시가 없었더라면의 경우보다 더 빠르게 member의 실제 데이터를 불러올 수 있을 것이다. (성능 최적화)
        */
    }
}









