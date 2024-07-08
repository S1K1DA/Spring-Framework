package kr.co.green.member.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/form")
public class MemberApiForm {
	@GetMapping("/info")
	public String infoPage() {
		return "member/getInfo";
	}

}
