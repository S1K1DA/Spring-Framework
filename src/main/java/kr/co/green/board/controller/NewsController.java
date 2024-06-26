package kr.co.green.board.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
			
			model.addAttribute("row", row);
			model.addAttribute("pi", pi);
			model.addAttribute("list", list);  // 객체 바인딩
			
			return "board/news/newsList";
		}
		
		@GetMapping("/detail.do")
		public String newsDetail(NewsDto news, Model model) {
			NewsDto result = newsService.getDetail(news);
			
			if(!Objects.isNull(result)) {  // result가 NULL이 아닐 때
				model.addAttribute("result", result); // 데이터 바인딩
				return "board/news/newsDetail";
			} else { // NULL일 때 에러 페이지로 이동
				return "common/error";
			}
		}
}
