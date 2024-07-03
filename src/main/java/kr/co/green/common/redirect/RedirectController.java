package kr.co.green.common.redirect;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class RedirectController {

	@GetMapping("/accessDenied")
	public String accessDenied(String referer, Model model) {
		
		if(referer.equals("null") || referer.equals("/member/login.do")) {
			model.addAttribute("referer", "/");
		} else {
			model.addAttribute("referer", referer);
		}
		return "common/error";
	}
}
