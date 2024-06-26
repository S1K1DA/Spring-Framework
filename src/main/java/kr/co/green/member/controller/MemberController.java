package kr.co.green.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.green.member.model.service.MemberServiceImpl;

@Controller
@RequestMapping("/member")
public class MemberController {
		private final MemberServiceImpl memberService;
	
	@Autowired
	public MemberController(MemberServiceImpl memberService) {
		this.memberService = memberService;
		
	}
	
	@GetMapping("/registerForm.do")
	public String getRegisterForm() {
		return "member/register";
	}

	@PostMapping("/checkId.do")
	@ResponseBody
	public String getCheckId() {
		// 이메일 중복 검사
		int result = memberService.getCheckId();
		
		if(result == 1) {
			return "false";
		} else {
			return "true";
		}
		
		
	}
	
}
