package kr.co.green.board.model.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.board.model.dto.NewsDto;
import kr.co.green.common.pageing.PageInfo;

public interface NewsService {
	
	// NewsList 게시글 조회
	List<NewsDto> newsList(PageInfo pi , NewsDto news);
	
	// NewsList 전체 게시글 수 조회
	int getListCount(NewsDto news);
	
	// NewsList 게시글 상세 보기
	NewsDto getDetail(NewsDto news, String type);
	
	// NewsList 게시글 등록
	int setEnroll(NewsDto news, MultipartFile upload, HttpSession session);
	
	// NewsList 게시글 삭제
	int newsDelete(int boardNo, int memberNo, int loginMemberNo);
	
	// NewsList 게시글 수정
	int edit(NewsDto free, MultipartFile upload, int loginMemberNo);
	



}
