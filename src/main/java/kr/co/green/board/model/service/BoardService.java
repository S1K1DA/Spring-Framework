package kr.co.green.board.model.service;

import java.util.List;

import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.common.pageing.PageInfo;

public interface BoardService {
	// 게시글 조회
	List<FreeDto> freeList(PageInfo pi , FreeDto free);
	
	// 전체 게시글 수 조회
	int getListCount(FreeDto free);
	
	// 게시글 조회
	FreeDto getDetail(FreeDto free);
}
