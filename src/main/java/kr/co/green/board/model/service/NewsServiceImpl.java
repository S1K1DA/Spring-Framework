package kr.co.green.board.model.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.green.board.model.dao.NewsDao;
import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.board.model.dto.NewsDto;
import kr.co.green.common.pageing.PageInfo;
import kr.co.green.common.upload.UploadFile;
import kr.co.green.common.validation.DataValidation;

@Service
public class NewsServiceImpl implements NewsService {
	
	private final NewsDao newsDao;
	private final DataValidation dataValidation;
	private NewsDto newsDto;
	private final UploadFile uploadFile;
	
	@Autowired
	public NewsServiceImpl(NewsDao newsDao, DataValidation dataValidation, UploadFile uploadFile) {
		this.newsDao = newsDao;
		this.newsDto = new NewsDto();
		this.dataValidation = dataValidation;
		this.uploadFile = uploadFile;
	}

	@Override
	public List<NewsDto> newsList(PageInfo pi, NewsDto news) {
		return newsDao.newsList(pi, news);
	}

	@Override
	public int getListCount(NewsDto news) {
		return newsDao.getListCount(news);
	}

	@Override
	public NewsDto getDetail(NewsDto news, String type) {
		
		try {
			int result = 0;
			
			if(type.equals("detail")) {
				// 조회수 증가
				result = newsDao.addViews(news);
			} else if(type.equals("edit")) {
				result = 1;
			}
			
			if(result == 1) {
				// 게시글 정보 조회
				newsDto = newsDao.getDetail(news);
				return newsDto;
			} else {
				return null;
			}
			
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
		
		
	}
	
	@Override
	public int setEnroll(NewsDto news, MultipartFile upload, HttpSession session) {
if(dataValidation.lengthCheck(news.getBoardTitle(), 100)) {
			
			uploadFile.newsUpload(news, upload, session);
			
			int result = newsDao.setEnroll(news);
			
			if(result == 1 && news.getUploadPath() != null) {
				return newsDao.setUpload(news);
			} else if(result == 1) {
				return result;
			}
		} 
		return 0;
    }
	
	@Override
//	public int delete(int boardNo, int memberNo, HttpSession session) {
		public int newsDelete(int boardNo, int memberNo, int loginMemberNo) {
		// 요청한 사용자가 글 작성자가 맞는지 검증
		if(memberNo == loginMemberNo) {
			int deleteResult = newsDao.newsDelete(boardNo);
			if(deleteResult == 1 && newsDao.getFileName(boardNo) != null) {
				NewsDto resultDto = newsDao.getFileName(boardNo); // 파일 이름 가져오기
				// 파일 삭제 -> fileName
				return uploadFile.newsDelete(resultDto) ? 1 : 0;
			}
			return deleteResult;
		} 
		return 0;
	}
	
	@Override
	public int edit(NewsDto news, MultipartFile upload, int loginMemberNo) {
		// 1. 사용자 검증
		// 2. 데이터 길이 검사
		//  -> 제목 : 최대 300byte
		int updateResult = 0;
		
		if(news.getMemberNo() == loginMemberNo 
		&& dataValidation.lengthCheck(news.getBoardTitle(), 300)) {
			// 3. 제목, 내용, UPDATE
			updateResult = newsDao.edit(news);
			
			if(updateResult == 1 && !upload.isEmpty()) {
				// 4. 업로드한 파일이 있을 때 : 기존 파일 삭제 -> 새로운 파일 업로드
				//                        upload  테이블 UPDATE
				NewsDto getFileName = newsDao.getFileName(news.getBoardNo());
				uploadFile.newsUpload(news, upload, null);
				
				if(uploadFile.newsDelete(getFileName) && news.getUploadName() != null) {
					return newsDao.setUploadUpdate(news) == 1 ? 1 : 0;
				}
			}
			// 5. 업로드한 파일이 없을 떄 :  띡히 할거 없음.
			
		}
		return updateResult;
	}
	}
	

