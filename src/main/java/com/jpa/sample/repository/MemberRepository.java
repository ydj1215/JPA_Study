package com.jpa.sample.repository;

import com.jpa.sample.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Bean 등록 => 싱글톤
// 네이밍 규칙에 따라 API를 작성하면, 그에 맞는 쿼리문을 하이버네트워크가 구현
public interface MemberRepository extends JpaRepository<Member, Long> {
    // .orElseThrow 를 사용하기 위해 Member -> Optional<Member>, NULL값이 넘어올 때가 아니면 Member형이 반환
    Optional<Member> findByEmail(String email);

    Member findByPassword(String password);
    Member findByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
}
