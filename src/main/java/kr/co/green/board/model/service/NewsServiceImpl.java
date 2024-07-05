package kr.co.green.board.model.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import kr.co.green.board.model.dao.NewsDao;
import kr.co.green.board.model.dto.NewsDto;
import kr.co.green.common.pageing.PageInfo;
import kr.co.green.common.transaction.TransactionHandler;
import kr.co.green.common.upload.UploadFile;
import kr.co.green.common.validation.DataValidation;

@Service
public class NewsServiceImpl implements NewsService {
	
	private static final Logger logger = LogManager.getLogger(NewsServiceImpl.class);
	
	private final NewsDao newsDao;
	private final DataValidation dataValidation;
	private final TransactionHandler transactionHandler;
	private NewsDto newsDto;
	private final UploadFile uploadFile;
	
	@Autowired
	public NewsServiceImpl(NewsDao newsDao, DataValidation dataValidation, UploadFile uploadFile,TransactionHandler transactionHandler) {
		this.newsDao = newsDao;
		this.newsDto = new NewsDto();
		this.dataValidation = dataValidation;
		this.transactionHandler = transactionHandler;
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
				logger.info("게시글 상세 조회 성공: boardNo={}", news.getBoardNo());
				return newsDto;
			} else {
				logger.warn("게시글 조회 실패: 결과 없음, boardNo={}", news.getBoardNo());
				return null;
			}
			
		} catch(Exception e) {
			logger.error("게시글 상세 조회 중 예외 발생, Exception : ", e);
			System.out.println(e);
			return null;
		}
		
		
	}
	
	@Override
	public int setEnroll(NewsDto news, MultipartFile upload, HttpSession session) {
		logger.info("게시글 등록 요청: BoardDto={}, MultipartFile={}", news, upload.getOriginalFilename());

		if (dataValidation.lengthCheck(news.getBoardTitle(), 100)) {
			uploadFile.newsUpload(news, upload, session);
			logger.info("파일 업로드 성공: boardNo={}", news.getBoardNo());
			int result = newsDao.setEnroll(news);

			if (result == 1 && news.getUploadPath() != null) {
				logger.info("게시글 등록 및 파일 업로드 정보 저장 성공 : boardNo={}", news.getBoardNo());
				return newsDao.setUpload(news);
			} else if (result == 1) {
                logger.info("게시글 등록 성공(업로드 파일 없음) : boardNo={}", news.getBoardNo());
				return result;
			}
		} else {
			logger.warn("게시글 등록 실패: 제목 길이 검증 실패");
		}
		return 0;
	}
	
	@Override
//	public int delete(int boardNo, int memberNo, HttpSession session) {
	public int newsDelete(int boardNo, int memberNo, int loginMemberNo) {
		// 요청한 사용자가 글 작성자가 맞는지 검증
		if (memberNo == loginMemberNo) {
			int deleteResult = newsDao.newsDelete(boardNo);

			if (deleteResult == 1) {
				NewsDto resultDto = newsDao.getFileName(boardNo); // 파일 이름 가져오기
				if (resultDto != null) {
					// 파일 삭제 성공 시
					if (uploadFile.newsDelete(resultDto)) {
						logger.info("파일 삭제 성공: boardNo={}, boardUploadName={}", boardNo, resultDto.getUploadName());
						return 1;
					} else {
						logger.warn("파일 삭제 실패: boardNo={}, boardUploadName={}", boardNo, resultDto.getUploadName());
						return 0;
					}
				} else {
					logger.warn("파일 삭제 실패: boardNo={} (파일 정보 없음)", boardNo);
				}
			} else {
				logger.warn("게시글 삭제 실패: boardNo={}", boardNo);
			}
			return deleteResult;
		} else {
			logger.warn("게시글 삭제 실패: 사용자 검증 실패");
		}
		return 0;
	}
	
	@Override
	public int edit(NewsDto news, MultipartFile upload, int loginMemberNo) {
		HashMap<String, Object> getTransaction = transactionHandler.getStatus();
		TransactionStatus status = (TransactionStatus) getTransaction.get("status");
		PlatformTransactionManager transactionManager = (PlatformTransactionManager) getTransaction.get("transactionManager");
		// 1. 사용자 검증
		// 2. 데이터 길이 검사
		//  -> 제목 : 최대 300byte
		int updateResult = 0;
		
		if(news.getMemberNo() == loginMemberNo 
		&& dataValidation.lengthCheck(news.getBoardTitle(), 300)) {
	        logger.info("게시글 수정 요청: BoardDto={}, loginMemberNo={}", news, loginMemberNo);
			// 3. 제목, 내용, UPDATE
			updateResult = newsDao.edit(news);
			
			if(updateResult == 1 && !upload.isEmpty()) {
				// 4. 업로드한 파일이 있을 때 : 기존 파일 삭제 -> 새로운 파일 업로드
				//                        upload  테이블 UPDATE
				NewsDto getFileName = newsDao.getFileName(news.getBoardNo());
				uploadFile.newsUpload(news, upload, null);
				
				if(news.getUploadName() == null && news.getUploadPath() == null && news.getUploadOriginName() == null) {
					transactionManager.rollback(status);
				}
				
				boolean deleteResult = uploadFile.newsDelete(getFileName);
				
				if(deleteResult && news.getUploadName() != null) {
					transactionManager.commit(status);
					logger.info("새로운 파일 업로드 성공: boardNo={}, boardUploadName={}", news.getBoardNo(), news.getUploadName());
					return newsDao.setUploadUpdate(news) == 1 ? 1 : 0;
				} else {
					transactionManager.rollback(status);
                    logger.warn("새로운 파일 업로드 실패: boardNo={}, boardUploadName={}", news.getBoardNo(), news.getUploadName());
				}
			}
			// 5. 업로드한 파일이 없을 떄 :  띡히 할거 없음.
			
		} else {
			transactionManager.rollback(status);
			logger.warn("게시글 등록 실패: 제목 길이 검증 실패");
		}
		logger.info("게시글 수정 성공: boardNo={}", news.getBoardNo());
		return updateResult;
	}
	}
	

