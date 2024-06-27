package kr.co.green.member.model.service;

import kr.co.green.member.model.dto.MemberDto;

public interface MemberService {
	
	
	int getCheckId(String memberId);
	
	int setRegister(MemberDto member);
	
	MemberDto login(MemberDto member);


}
