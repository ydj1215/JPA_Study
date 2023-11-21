package com.jpa.sample.controller;


import com.jpa.sample.dto.MemberDto;
import com.jpa.sample.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jpa.utils.Common.CORS_ORIGIN;

@RestController
@Slf4j // Log f4 : 로그를 기록 및 출력하기 위해 사용
@CrossOrigin(origins = CORS_ORIGIN)
@RequestMapping("/member")
@RequiredArgsConstructor // @Autowired 를 대체,
public class MemberController {
    private final MemberService memberService;

    // 회원 등록
    @PostMapping("/new")
    // Http 요청의 본문(body)를 MemberDto 타입으로 변환 후 memberDto라는 변수에 할당하며,
    public ResponseEntity<Boolean> memberRegister(@RequestBody MemberDto memberDto) {
        boolean isTrue = memberService.saveMember(memberDto); // 회원 정보를 저장하고,
        // isTrue : // 그 결과를 반환, true = 회원 정보 저장 성공, false = 회원 정보 저장 실패
        return ResponseEntity.ok(isTrue); // isTrue의 값을 Http 응답의 본문에 담아 상태 코드 200(ok)와 함께 반환
    }

    // 회원 전체 조회
    @GetMapping("/list")
    public ResponseEntity<List<MemberDto>> memberList() {
        // <> : 제네릭, List나 Map같은 컬렉션 클래스를 사용할 때, 이 컬렉션에 어떤 객체를 저장할 것인지를 <> 안에 명시
        List<MemberDto> list = memberService.getMemberList();
        return ResponseEntity.ok(list);
    }

    // 회원 상세 조회
    @GetMapping("/detail/{email}")
    public ResponseEntity<MemberDto> memberDetail(@PathVariable String email) {
        // @PathVariable : URL 에서 특정 부분을 추출하여 메서드의 매개변수로 전달할 때 사용
        MemberDto memberDto = memberService.getMemberDetail(email);
        return ResponseEntity.ok(memberDto);
    }

    // 페이지네이션 조회
    @GetMapping("/list/page")
    public ResponseEntity<List<MemberDto>> memberList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        List<MemberDto> list = memberService.getMemberList(page, size);
        return ResponseEntity.ok(list);
    }

    // 총 페이지 수 조회
    @GetMapping("/list/page-count")
    public  ResponseEntity<Integer> memberPageCount(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        int pageCnt = memberService.getMemberPage(page, size);
        return ResponseEntity.ok(pageCnt);
    }
}
