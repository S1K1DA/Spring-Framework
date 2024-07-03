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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String login(MemberDto member, HttpSession session, RedirectAttributes redirectAttributes) {
		
		MemberDto loginUser = memberService.login(member);
		
		if(!Objects.isNull(loginUser)) { // 로그인 성공
			session.setAttribute("memberNo", loginUser.getMemberNo());
			session.setAttribute("memberType", loginUser.getMemberType());
			session.setAttribute("memberName", loginUser.getMemberName());
//			session.setAttribute("msg", "로그인되었습니다.");
			
//		    addAttribute  vs  addFlashAttribute
//			1. addAttribute : int, String 넘길 때 사용
//			                  -> 쿼리 파라미터로 전달
//			2. addFlashAttribute : 객체를 넘기고 싶거나, 일회성 변수를 사용하고 싶을 때
//			   					  -> 한번 사용 후 사라짐 (휘발성 데이터)
			redirectAttributes.addFlashAttribute("icon", "success");
			redirectAttributes.addFlashAttribute("title", "로그인 성공");
			redirectAttributes.addFlashAttribute("text", "로그인에 성공했습니다.");
			return "redirect:/";
		} else {
			redirectAttributes.addFlashAttribute("icon", "error");
			redirectAttributes.addFlashAttribute("title", "로그인 실패");
			redirectAttributes.addFlashAttribute("text", "아이디 또는 비밀번호를 확인해주세요.");
			return "redirect:/member/loginForm.do";
		}
	}
	
	@GetMapping("/logout.do")
	public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		// 현재 세션이 있으면 세션 가져오기
		// 현재 세션이 없으면 null 반환
		HttpSession session = request.getSession(false);
		if(session != null) {
			redirectAttributes.addFlashAttribute("icon", "success");
			redirectAttributes.addFlashAttribute("title", "로그아웃");
			redirectAttributes.addFlashAttribute("text", "로그아웃 되었습니다.");
			session.invalidate();
		}
		return "redirect:/";
	}

	
	
}
