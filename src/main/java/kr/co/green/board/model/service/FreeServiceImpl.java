package kr.co.green.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.green.board.model.dao.FreeDao;
import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.common.pageing.PageInfo;
import kr.co.green.common.validation.DataValidation;

@Service
public class FreeServiceImpl implements FreeService {
	
	private final FreeDao freeDao;
	private final DataValidation dataValidation;
	private FreeDto freeDto;
	
	@Autowired
	public FreeServiceImpl(FreeDao freeDao, DataValidation dataValidation) {
		this.freeDao = freeDao;
		this.dataValidation = dataValidation;
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
		
		try {
			// 조회수 증가
			int result = freeDao.addViews(free);
			
			if(result == 1) {
				// 게시글 정보 조회
				freeDto = freeDao.getDetail(free);
				return freeDto;
			} else {
				return null;
			}
			
		} catch(Exception e) {
			return null;
		}
		
		
	}
	
	@Override
	public int setEnroll(FreeDto free) {
        return freeDao.setEnroll(free);
    }
	
}
