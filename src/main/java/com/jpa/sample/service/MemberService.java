package com.jpa.sample.service;


import com.jpa.sample.dto.MemberDto;
import com.jpa.sample.entity.Member;
import com.jpa.sample.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service // 해당 객체 bean 등록
@RequiredArgsConstructor // final로 선언되거나, @NonNull로 선언된 필드만을 매개변수로 하는 생성자를 자동으로 생성
public class MemberService {
    private final MemberRepository memberRepository; // final : 한번 초기화 후 변경 불가

    // 회원 등록
    public boolean saveMember(MemberDto memberDto) {
        // 이미 등록된 회원인지 확인하는 쿼리문
        boolean isReg = memberRepository.existsByEmail(memberDto.getEmail());
        if (isReg) return false;

        Member member = new Member(); // Entity
        /* 서비스와 리포지토리는 비즈니스 로직을 수행하거나 데베와의 연동을 담당하기 때문에
        의존성 주입(싱글톤 bean으로 관리된다.)을 사용하였지만, 엔티티 클래스는 데베 테이블을 의미하며,
        하나의 엔티티 인스턴스는 테이블의 한 행을 나타내기 때문에, 각 요청 및 트랜잭션마다 별도의 인스턴스가 필요하다.
        즉, new을 사용하며 직접 생성하는 것이 적절하다.
         */

        member.setEmail(memberDto.getEmail());
        member.setPassword(memberDto.getPassword());
        member.setName(memberDto.getName());
        // 시간 데이터는 자동으로 기입

        memberRepository.save(member); // save : JPA에 기본적으로 존재
        return true;
    }

    // 회원 전체 조회
    public List<MemberDto> getMemberList() {
        List<MemberDto> memberDtoList = new ArrayList<>();
        List<Member> memberList = memberRepository.findAll(); // findAll : JPA에 기본적으로 존재
        for (Member member : memberList) { // 향상된 for문 : 배열의 개수 만큼 순환
            memberDtoList.add(convertEntityToDto(member));
        }
        return memberDtoList;
    }

    // 페이지네이션 조회 (메서드 오버로딩)
    public List<MemberDto> getMemberList(int page, int size){
        Pageable pageable = PageRequest.of(page, size); // 읽어내야할 페이지를 반환
        List<MemberDto> memberDtoList = new ArrayList<>();
        List<Member> memberList = memberRepository.findAll(pageable).getContent();
        for(Member member : memberList){
            memberDtoList.add(convertEntityToDto(member));
        }
        return memberDtoList;
    }

    // 전체 페이지 수 조회
    public int getMemberPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return memberRepository.findAll(pageable).getTotalPages();
    }
    
    // 회원 엔티티를 DTO로 변환하는 메서드 만들기
    private MemberDto convertEntityToDto(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setEmail(member.getEmail());
        memberDto.setPassword(member.getPassword());
        memberDto.setName(member.getName());
        memberDto.setRegDate(member.getRegDate());
        return memberDto;
    }

    // 회원 상세 조회
    public MemberDto getMemberDetail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));
     return convertEntityToDto(member);
    }
}
