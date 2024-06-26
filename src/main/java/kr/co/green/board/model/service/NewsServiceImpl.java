package kr.co.green.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.green.board.model.dao.NewsDao;
import kr.co.green.board.model.dto.NewsDto;
import kr.co.green.common.pageing.PageInfo;

@Service
public class NewsServiceImpl implements NewsService {
	
	private final NewsDao newsDao;
	private NewsDto newsDto;
	
	@Autowired
	public NewsServiceImpl(NewsDao newsDao) {
		this.newsDao = newsDao;
		this.newsDto = new NewsDto();
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
	public NewsDto getDetail(NewsDto news) {
		
		try {
			// 조회수 증가
			int result = newsDao.addViews(news);
			
			if(result == 1) {
				// 게시글 정보 조회
				newsDto = newsDao.getDetail(news);
				return newsDto;
			} else {
				return null;
			}
			
		} catch(Exception e) {
			return null;
		}
	}
	
}
