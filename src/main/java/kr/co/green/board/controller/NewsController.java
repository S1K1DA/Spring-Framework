package kr.co.green.board.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.green.board.model.dto.NewsDto;
import kr.co.green.board.model.service.NewsServiceImpl;
import kr.co.green.common.pageing.PageInfo;
import kr.co.green.common.pageing.Pagination;

@Controller
@RequestMapping("/news")
public class NewsController {
	
		private static final Logger logger = LogManager.getLogger();

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
			
			logger.info("상세 조회 요청: boardNo={}, memberNo={}", news.getBoardNo(), session.getAttribute("memberNo"));
			
			NewsDto result = newsService.getDetail(news, "detail");
			
			if(!Objects.isNull(result)) {  // result가 NULL이 아닐 때
				if(result.getUploadPath() != null && result.getUploadName() != null ) {
					int pathIndex = result.getUploadPath().lastIndexOf("resources");
					String path = "/" + result.getUploadPath().substring(pathIndex).replace("\\", "/");
					result.setUploadPath(path);
				}
				model.addAttribute("loginMemberNo", session.getAttribute("memberNo"));
				model.addAttribute("result", result); // 데이터 바인딩
				
				logger.info("상세 조회 성공: {}", result);
				
				return "board/news/newsDetail";
			} else { // NULL일 때 에러 페이지로 이동
				
				logger.error("상세 조회 실패: boardNo={}", news.getBoardNo());
				
				return "common/error";
			}
		}
		
		@GetMapping("/enrollForm.do")
		public String enrollForm() {
			return "board/news/newsEnroll";
		}
		
		@PostMapping("/enroll.do")
		@RequestMapping("/enroll.do")
		public String setEnroll(NewsDto news, 
								MultipartFile upload, 
								HttpSession session,
								RedirectAttributes redirectAttributes) {
			news.setMemberNo((int)session.getAttribute("memberNo"));
			
			logger.info("[뉴스게시판] 게시글 등록 요청: memberNo={}", news.getMemberNo());
			
			int result = newsService.setEnroll(news, upload, session);
			System.out.println(result);
			if(result == 1) {
				logger.info("[자유게시판] 게시글 등록 성공: boardNo={}", news.getBoardNo());
				redirectAttributes.addFlashAttribute("icon", "success");
				redirectAttributes.addFlashAttribute("title", "등록 완료");
				redirectAttributes.addFlashAttribute("text", "게시글이 등록되었습니다.");
				return "redirect:/news/list.do";
			} else {
				logger.error("[자유게시판] 게시글 등록 실패: boardNo={}", news.getBoardNo());
				return "common/error";
				
			}
		}
		
		@GetMapping("/delete.do")
		@RequestMapping("/delete.do")
//		public String delete(int boardNo, int memberNo, HttpSession session) {
			public String newsDelete(int boardNo, 
								 int memberNo, 
								 @SessionAttribute("memberNo") int loginMemberNo,
								 RedirectAttributes redirectAttributes) {
			
			logger.info("게시글 삭제 요청: boardNo={}, memberNo={}", boardNo, memberNo);
			// UPDATE
			System.out.println(boardNo);
			int result = newsService.newsDelete(boardNo, memberNo, loginMemberNo);
			
			if (result == 1) {
	            redirectAttributes.addFlashAttribute("icon", "success");
	            redirectAttributes.addFlashAttribute("title", "삭제 완료");
	            redirectAttributes.addFlashAttribute("text", "게시글이 삭제되었습니다.");
	            
	            // Logging for successful news delete
	            logger.info("[뉴스게시판] 게시판 삭제 성공: boardNo={}", boardNo);
	            
	            return "redirect:/news/list.do";
	        } else {
	            // Logging for failed news delete
	            logger.error("[뉴스게시판] 게시글 삭제 실패 : boardNo={}", boardNo);
	            
	            return "/common/error";
	        }
	        
	    }
		
		@PostMapping("/editForm.do")
		@RequestMapping("/editForm.do")
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
						   @SessionAttribute("memberNo") int loginMemberNo,
						   RedirectAttributes redirectAttributes) {
			logger.info("게시글 수정 요청: boardNo={}, loginMemberNo={}", news.getBoardNo(), loginMemberNo);
			int result = newsService.edit(news, upload, loginMemberNo);
			if(result == 1) {
				redirectAttributes.addFlashAttribute("icon", "success");
				redirectAttributes.addFlashAttribute("title", "수정 완료");
				redirectAttributes.addFlashAttribute("text", "게시글이 수정되었습니다.");
				logger.info("게시글 수정 성공: boardNo={}", news.getBoardNo());
				return "redirect:/news/detail.do?boardNo="+news.getBoardNo();
			}
			logger.error("게시글 수정 실패 : boardNo={}", news.getBoardNo());
			return "/common/error";
		}
	}
