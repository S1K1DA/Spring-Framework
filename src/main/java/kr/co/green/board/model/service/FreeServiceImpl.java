package kr.co.green.board.model.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.green.board.model.dao.FreeDao;
import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.common.pageing.PageInfo;
import kr.co.green.common.upload.UploadFile;
import kr.co.green.common.validation.DataValidation;

@Service
public class FreeServiceImpl implements FreeService {
	
	private final FreeDao freeDao;
	private final DataValidation dataValidation;
	private FreeDto freeDto;
	private final UploadFile uploadFile;
	
	@Autowired
	public FreeServiceImpl(FreeDao freeDao, DataValidation dataValidation, UploadFile uploadFile) {
		this.freeDao = freeDao;
		this.dataValidation = dataValidation;
		this.uploadFile = uploadFile;
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
			System.out.println(e);
			return null;
		}
		
		
	}
	
	@Override
	public int setEnroll(FreeDto free, MultipartFile upload, HttpSession session) {
		if(dataValidation.lengthCheck(free.getBoardTitle(), 100)) {
			
			uploadFile.upload(free, upload, session);
			
			int result = freeDao.setEnroll(free);
			
			if(result == 1 && free.getUploadPath() != null) {
				return freeDao.setUpload(free);
			}
		} 
		return 0;
    }
	
}
