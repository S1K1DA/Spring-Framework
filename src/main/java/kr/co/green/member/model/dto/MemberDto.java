package kr.co.green.member.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter // 게터 생성
@Setter // 세터 생성
public class MemberDto {
	
	private int memberNo;
	private String memberName;
	private String memberId;
	private String memberPassword;
	private String confirPassword;
	private String memberIndate;
	private String memberType;
	
}
