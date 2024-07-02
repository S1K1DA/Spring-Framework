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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import kr.co.green.board.model.dto.NewsDto;
import kr.co.green.board.model.service.NewsServiceImpl;
import kr.co.green.common.pageing.PageInfo;
import kr.co.green.common.pageing.Pagination;

@Controller
@RequestMapping("/news")
public class NewsController {

		private final NewsServiceImpl newsService;
		
		@Autowired
		public NewsController(NewsServiceImpl newsService) {
			this.newsService = newsService;
		}
		
		@GetMapping("/list.do")
		public String newsList(Model model,
								@RequestParam(value="cpage", defaultValue="1") int cpage,
								NewsDto news) {
			
			int listCount = newsService.getListCount(news);
			int pageLimit = 5;
			int boardLimit = 6;
			int row = listCount - (cpage-1) * boardLimit;
			
			PageInfo pi = Pagination.getPageInfo(listCount, cpage, pageLimit, boardLimit);
			
			List<NewsDto> list = newsService.newsList(pi, news);
			
			for(NewsDto item : list) {
				if(item.getUploadPath() != null && item.getUploadName() != null ) {
					int pathIndex = item.getUploadPath().lastIndexOf("resources");
					String path = "/" + item.getUploadPath().substring(pathIndex).replace("\\", "/");
					item.setUploadPath(path);
			}
			}
			
			model.addAttribute("row", row);
			model.addAttribute("pi", pi);
			model.addAttribute("list", list);  // 객체 바인딩
			
			return "board/news/newsList";
		}
		
		@GetMapping("/detail.do")
		public String newsDetail(NewsDto news, Model model, HttpSession session) {
			NewsDto result = newsService.getDetail(news, "detail");
			
			if(!Objects.isNull(result)) {  // result가 NULL이 아닐 때
				if(result.getUploadPath() != null && result.getUploadName() != null ) {
					int pathIndex = result.getUploadPath().lastIndexOf("resources");
					String path = "/" + result.getUploadPath().substring(pathIndex).replace("\\", "/");
					result.setUploadPath(path);
				}
				model.addAttribute("loginMemberNo", session.getAttribute("memberNo"));
				model.addAttribute("result", result); // 데이터 바인딩
				return "board/news/newsDetail";
			} else { // NULL일 때 에러 페이지로 이동
				return "common/error";
			}
		}
		
		@GetMapping("/enrollForm.do")
		public String enrollForm() {
			return "board/news/newsEnroll";
		}
		
		@PostMapping("/enroll.do")
		public String setEnroll(NewsDto news, 
								MultipartFile upload, 
								HttpSession session) {
			news.setMemberNo((int)session.getAttribute("memberNo"));
			int result = newsService.setEnroll(news, upload, session);
			System.out.println(result);
			if(result == 1) {
				return "redirect:/news/list.do";
			} else {
				return "common/error";
				
			}
		}
		
		@GetMapping("/delete.do")
//		public String delete(int boardNo, int memberNo, HttpSession session) {
			public String newsDelete(int boardNo, 
								 int memberNo, 
								 @SessionAttribute("memberNo") int loginMemberNo) {
			// UPDATE
			System.out.println(boardNo);
			int result = newsService.newsDelete(boardNo, memberNo, loginMemberNo);
			
			// 파일 삭제
			return result == 1 ? "redirect:/news/list.do" : "/common/error";
			
		}
		
		@PostMapping("/editForm.do")
		public String editForm(NewsDto news,
							   Model model,
							   HttpSession session) {
			NewsDto result = newsService.getDetail(news, "edit");
			
			System.out.println(result);
			
			if(!Objects.isNull(result)) {  // result가 NULL이 아닐 때
				if(result.getUploadPath() != null && result.getUploadName() != null ) {
					int pathIndex = result.getUploadPath().lastIndexOf("resources");
					String path = "/" + result.getUploadPath().substring(pathIndex).replace("\\", "/");
					result.setUploadPath(path);
				} 
				model.addAttribute("loginMemberNo", session.getAttribute("memberNo"));
				model.addAttribute("result", result); // 데이터 바인딩
				return "board/news/newsEdit";
			} else { // NULL일 때 에러 페이지로 이동
				return "common/error";
			}
		}
		
		@PostMapping("/edit.do")
		public String edit(NewsDto news,
						   MultipartFile upload,
						   @SessionAttribute("memberNo") int loginMemberNo) {
			int result = newsService.edit(news, upload, loginMemberNo);
			if(result == 1) {
				return "redirect:/news/detail.do?boardNo="+news.getBoardNo();
			}
			return "/common/error";
		}
	}
