package kr.co.green.member.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.green.member.model.dto.MemberDto;
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
	
	@GetMapping("/loginForm.do")
	public String getLoginForm() {
		return "member/login";
	}
	
	

	@PostMapping("/checkId.do")
	@ResponseBody
	public String getCheckId(String memberId) {
		// 아이디 중복 검사
		System.out.println(memberId);
		int result = memberService.getCheckId(memberId);
		
//		if(result == 1) { // 사용 불가
//			return "false";
//		} else {
//			return "true";
//		}
		return intReturn(result, "false", "true");
	}
	
	@PostMapping("/register.do")
	public String setRegister(MemberDto member) {
		int result = memberService.setRegister(member);
		
		return intReturn(result, "member/login", "common/error");
	}
	
	private String intReturn(int result, String path, String errorPath) {
		if(result == 1) {
			return path;
		} else {
			return "common/error";
		}
	}
	
	@PostMapping("/login.do")
	public String login(MemberDto member, HttpSession session) {
		
		MemberDto loginUser = memberService.login(member);
		
		if(!Objects.isNull(loginUser)) { // 로그인 성공
			session.setAttribute("memberNo", loginUser.getMemberNo());
			session.setAttribute("memberType", loginUser.getMemberType());
			session.setAttribute("memberName", loginUser.getMemberName());
			return "home";
		} else {
			return "common/error";
		}
	}
	
	@GetMapping("/logout.do")
	public String logout(HttpServletRequest request) {
		// 현재 세션이 있으면 세션 가져오기
		// 현재 세션이 없으면 null 반환
		HttpSession session = request.getSession(false);
		if(session != null) {
			session.invalidate();
		}
		return "home";
	}

	
	
}
