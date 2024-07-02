package kr.co.green.board.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

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
	public String freeDetail(FreeDto free, Model model, HttpSession session) {
		
	//   HttpServletRequest request
		// 컨트롤러를 이용한 referer 처리
//		String referer = request.getHeader("referer");
//		System.out.println("referer : " + referer);
//		if(referer == null || !referer.endsWith("/list.do")) {
//			return "common/error";
//		}
		
		
		FreeDto result = freeService.getDetail(free, "detail");
		
		// 1. resources 이후의 문자열 가져오기
		
		
		
		if(!Objects.isNull(result)) {  // result가 NULL이 아닐 때
			if(result.getUploadPath() != null && result.getUploadName() != null ) {
				int pathIndex = result.getUploadPath().lastIndexOf("resources");
				String path = "/" + result.getUploadPath().substring(pathIndex).replace("\\", "/");
				result.setUploadPath(path);
			} 
			model.addAttribute("loginMemberNo", session.getAttribute("memberNo"));
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
	public String setEnroll(FreeDto free, 
							MultipartFile upload, 
							HttpSession session) {
		free.setMemberNo((int)session.getAttribute("memberNo"));
		int result = freeService.setEnroll(free, upload, session);
		System.out.println(result);
		if(result == 1) {
			return "redirect:/free/list.do";
		} else {
			return "common/error";
			
		}
	}
	
	@GetMapping("/delete.do")
//	public String delete(int boardNo, int memberNo, HttpSession session) {
		public String delete(int boardNo, 
							 int memberNo, 
							 @SessionAttribute("memberNo") int loginMemberNo) {
		// UPDATE
		System.out.println(boardNo);
		int result = freeService.delete(boardNo, memberNo, loginMemberNo);
		
		// 파일 삭제
		return result == 1 ? "redirect:/free/list.do" : "/common/error";
		
	}
	
	@PostMapping("/editForm.do")
	public String editForm(FreeDto free,
						   Model model,
						   HttpSession session) {
		FreeDto result = freeService.getDetail(free, "edit");
		
		
		if(!Objects.isNull(result)) {  // result가 NULL이 아닐 때
			if(result.getUploadPath() != null && result.getUploadName() != null ) {
				int pathIndex = result.getUploadPath().lastIndexOf("resources");
				String path = "/" + result.getUploadPath().substring(pathIndex).replace("\\", "/");
				result.setUploadPath(path);
			} 
			model.addAttribute("loginMemberNo", session.getAttribute("memberNo"));
			model.addAttribute("result", result); // 데이터 바인딩
			return "board/free/freeEdit";
		} else { // NULL일 때 에러 페이지로 이동
			return "common/error";
		}
	}
	
	@PostMapping("/edit.do")
	public String edit(FreeDto free,
					   MultipartFile upload,
					   @SessionAttribute("memberNo") int loginMemberNo) {
		int result = freeService.edit(free, upload, loginMemberNo);
		if(result == 1) {
			return "redirect:/free/detail.do?boardNo="+free.getBoardNo();
		}
		return "/common/error";
	}
}
	
	


