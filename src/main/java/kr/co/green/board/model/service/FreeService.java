package kr.co.green.board.model.service;

import java.util.List;

import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.board.model.dto.NewsDto;
import kr.co.green.common.pageing.PageInfo;

public interface FreeService {
	
	// freeList 게시글 조회
	List<FreeDto> freeList(PageInfo pi , FreeDto free);
	
	// freeList 전체 게시글 수 조회
	int getListCount(FreeDto free);
	
	// freeList 게시글 상세 보기
	FreeDto getDetail(FreeDto free);
	
	// freeList 게시글 등록
	int setEnroll(FreeDto free);

}
