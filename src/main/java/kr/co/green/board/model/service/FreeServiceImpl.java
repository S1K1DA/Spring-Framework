package kr.co.green.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.green.board.model.dao.FreeDao;
import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.common.pageing.PageInfo;

@Service
public class FreeServiceImpl implements BoardService {
	
	private final FreeDao freeDao;
	private FreeDto freeDto;
	
	@Autowired
	public FreeServiceImpl(FreeDao freeDao) {
		this.freeDao = freeDao;
		this.freeDto = new FreeDto();
	}
	
	@Override
	public List<FreeDto> freeList(PageInfo pi, FreeDto free) {
		return freeDao.freeList(pi, free);
		
	}
	
	@Override
	public int getListCount(FreeDto free) {
		return freeDao.getListCount(free);
	}
	
	@Override
	public FreeDto getDetail(FreeDto free) {
		
		freeDto = freeDao.getDetail(free);
		System.out.println(freeDto.getBoardTitle());
		
		return null;
		
		
		
		
	}
}
