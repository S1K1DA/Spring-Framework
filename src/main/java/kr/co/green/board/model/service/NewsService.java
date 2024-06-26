package kr.co.green.board.model.service;

import java.util.List;

import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.board.model.dto.NewsDto;
import kr.co.green.common.pageing.PageInfo;

public interface NewsService {
	
	// NewsList 게시글 조회
	List<NewsDto> newsList(PageInfo pi , NewsDto news);
	
	// NewsList 전체 게시글 수 조회
	int getListCount(NewsDto news);
	
	// NewsList 게시글 상세 보기
	NewsDto getDetail(NewsDto news);


}
