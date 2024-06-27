package kr.co.green.board.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.board.model.service.FreeServiceImpl;
import kr.co.green.common.pageing.PageInfo;
import kr.co.green.common.pageing.Pagination;

@Controller  // 이 클래스가 컨트롤러 역할을 하겠다.
@RequestMapping("/free")  // '/free'로 오는 요청을 받겠다. GET+POST
public class FreeController {
	
//	@Autowired private FreeDto fd;
	 
	
	private final FreeServiceImpl freeService;
	
	@Autowired
	public FreeController(FreeServiceImpl freeService) {
		this.freeService = freeService;
	}
	
	@GetMapping("/list.do")  //  '/list.do'로 오는 요청을 받겠다. GET
	public String freeList(Model model,
							@RequestParam(value="cpage", defaultValue="1") int cpage,
							FreeDto free) {
		// RequestParam 어노테이션 : 쿼리스트링을 받을 때 사용
		// value : 쿼리스트링 키
		
		// 1. 전체 게시글 수 구하기(페이징 처리)
		int listCount = freeService.getListCount(free);
		int pageLimit = 5;
		int boardLimit = 5;
		int row = listCount - (cpage-1) * boardLimit;
		
		PageInfo pi = Pagination.getPageInfo(listCount, cpage, pageLimit, boardLimit);
		
		List<FreeDto> list = freeService.freeList(pi, free);
		
		model.addAttribute("row", row);
		model.addAttribute("pi", pi);
		model.addAttribute("list", list);  // 객체 바인딩
		return "board/free/freeList";
		
	}
	
	@GetMapping("/detail.do")
	public String freeDetail(FreeDto free, Model model) {
		FreeDto result = freeService.getDetail(free);
		
		if(!Objects.isNull(result)) {  // result가 NULL이 아닐 때
			model.addAttribute("result", result); // 데이터 바인딩
			return "board/free/freeDetail";
		} else { // NULL일 때 에러 페이지로 이동
			return "common/error";
		}
	}
	
	@GetMapping("/enrollForm.do")
	public String enrollForm() {
		
		return "board/free/freeEnroll";
	}
	
	@PostMapping("/enroll.do")
	public String setEnroll(FreeDto free, HttpSession session) {
		free.setMemberNo((int)session.getAttribute("memberNo"));
		int result = freeService.setEnroll(free);
		
		if(result == 1) {
			return "redirect:/free/list.do";
		} else {
			return "common/error";
		}
		
	}
	
	
	

}
