package kr.co.green.member.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.green.member.model.dto.MemberDto;
import kr.co.green.member.model.service.MemberServiceImpl;

// Controller + ResponsBody
@RestController
@RequestMapping("/api/member")
public class MemberApi {
	private final MemberServiceImpl memberService;
	
	public MemberApi(MemberServiceImpl memberService) {
		this.memberService = memberService;
	}

	
	@GetMapping("/{id}")
	public ResponseEntity getInfo(@PathVariable("id") String id) {
		// id에 대한 회원을 조회하는 로직
		
		
		// 1. MemberService 생성자 주입
		// 2. MemberDto타입으로 id가 일치하는 회원의 정보를 가져오기
		MemberDto result = memberService.getInfo(id);
		
//		MemberDto md = new MemberDto();
//		md.setMemberIndate("2024-07-08");
//		md.setMemberName("정은식");
//		md.setMemberType("일반회원");
		
		// 조회한 데이터를 변환
		return new ResponseEntity(result, HttpStatus.OK);
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity deleteMember(@PathVariable("id") String id) {
		// 일치하는 id를 가진 회원 삭제
		int result = memberService.deleteMember(id);
		
		if(result == 1) {
			return new ResponseEntity("success", HttpStatus.OK);
		} else {
			return new ResponseEntity("faild", HttpStatus.OK);
		}
		
	}
	
	@PutMapping
	public ResponseEntity editMember(@RequestBody MemberDto member) {
		System.out.println("hi");
		System.out.println(member.getMemberId());
		System.out.println(member.getMemberName());
		System.out.println(member.getMemberPassword());
		
		return new ResponseEntity<>(null, HttpStatus.OK);
		
	}
	
	
}
