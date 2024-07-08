package kr.co.green.member.model.service;

import org.springframework.web.bind.annotation.PathVariable;

import kr.co.green.member.model.dto.MemberDto;

public interface MemberService {
	
	
	int getCheckId(String memberId);
	
	int setRegister(MemberDto member);
	
	MemberDto login(MemberDto member);
	
	MemberDto getInfo(String id);
	
	int deleteMember(String id);


}
