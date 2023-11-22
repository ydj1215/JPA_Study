package com.jpa.sample.repository;

import com.jpa.sample.entity.Cart;
import com.jpa.sample.entity.Member;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional// 트랙잭션 : 데이터 베이스의 논리적인 작업 단위로서, 모두 성공 아니면 롤백
// @Slf4j // 로깅 데이터를 처리하기 위해 사용
@TestPropertySource(locations = "classpath:application-test.properties")
class CartRepositoryTest {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext // JPA의 EntityManager 주입 받기
    EntityManager em;

    // 장바구니는 회원 테이블과 1:1 관계를 가지므로, 회원 테이블의 데이터가 필수적으로 요구된다.
    // 고로 한개 만들어 줘야한다.
    public Member createMemberInfo() {
        Member member = new Member();
        member.setUserId("userId0520");
        member.setPassword("password0520");
        member.setName("유동재");
        member.setEmail("abc0520@naver.com");
        member.setRegDate(LocalDateTime.now());
        return member;
    }

    @Test
    @DisplayName("장바구니 회원 매핑 테스트")
    public void findCartAndMemberTest() {
        // 회원 등록
        Member member = createMemberInfo();
        memberRepository.save(member);

        // 장바구니 등록
        Cart cart = new Cart();
        cart.setCartName("쿠팡 장바구니");
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush(); // 영속성 컨텍스트에 데이터 저장 후 트랙잭션이 끝날 때 데이터베이스에 반영
        em.clear(); // 영속성 컨텍스트 비우기, 즉 다음 테스트를 위해 초기 상태로 롤백

        Cart saveCart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException::new);
        System.out.println("로그 : " + saveCart);
    }
}