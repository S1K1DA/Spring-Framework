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
	public FreeDto getDetail(FreeDto free, String type) {
		
		try {
			int result = 0;
			
			if(type.equals("detail")) {
				// 조회수 증가
				result = freeDao.addViews(free);
			} else if(type.equals("edit")) {
				result = 1;
			}
			
			
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
			} else if(result == 1) {
				return result;
			}
		} 
		return 0;
    }
	
	@Override
//	public int delete(int boardNo, int memberNo, HttpSession session) {
		public int delete(int boardNo, int memberNo, int loginMemberNo) {
		// 요청한 사용자가 글 작성자가 맞는지 검증
		if(memberNo == loginMemberNo) {
			int deleteResult = freeDao.delete(boardNo);
			if(deleteResult == 1 && freeDao.getFileName(boardNo) != null) {
				FreeDto resultDto = freeDao.getFileName(boardNo); // 파일 이름 가져오기
				// 파일 삭제 -> fileName
				return uploadFile.delete(resultDto) ? 1 : 0;
			}
			return deleteResult;
		} 
		return 0;
	}
	@Override
	public int edit(FreeDto free, MultipartFile upload, int loginMemberNo) {
		// 1. 사용자 검증
		// 2. 데이터 길이 검사
		//  -> 제목 : 최대 300byte
		int updateResult = 0;
		
		if(free.getMemberNo() == loginMemberNo 
		&& dataValidation.lengthCheck(free.getBoardTitle(), 300)) {
			// 3. 제목, 내용, UPDATE
			updateResult = freeDao.edit(free);
			
			if(updateResult == 1 && !upload.isEmpty()) {
				// 4. 업로드한 파일이 있을 때 : 기존 파일 삭제 -> 새로운 파일 업로드
				//                        upload  테이블 UPDATE
				FreeDto getFileName = freeDao.getFileName(free.getBoardNo());
				uploadFile.upload(free, upload, null);
				
				if(uploadFile.delete(getFileName) && free.getUploadName() != null) {
					return freeDao.setUploadUpdate(free) == 1 ? 1 : 0;
				}
			}
			// 5. 업로드한 파일이 없을 떄 :  띡히 할거 없음.
			
		}
		return updateResult;
	}
}


