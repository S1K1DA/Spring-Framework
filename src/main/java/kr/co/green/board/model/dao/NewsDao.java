package kr.co.green.board.model.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.board.model.dto.NewsDto;
import kr.co.green.common.pageing.PageInfo;

@Repository
public class NewsDao {
	private final SqlSessionTemplate sqlSession;
	
	@Autowired
	public NewsDao(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public List<NewsDto> newsList(PageInfo pi, NewsDto news) {
		RowBounds rb = new RowBounds(pi.getOffset(), pi.getBoardLimit());
		return sqlSession.selectList("newsMapper.newsList", news, rb);
	}
	
	public int getListCount(NewsDto news) {
		return sqlSession.selectOne("newsMapper.getListCount", news);
	}
	
	public NewsDto getDetail(NewsDto news) {
		return sqlSession.selectOne("newsMapper.getDetail", news);
	}
	
	public int addViews(NewsDto newsDto) {
		return sqlSession.update("newsMapper.addViews", newsDto);
	}
	
	public int setEnroll(NewsDto news) {
		return sqlSession.insert("newsMapper.setEnroll", news);
	}

	public int setUpload(NewsDto news) {
		return sqlSession.insert("newsMapper.setUpload", news);
	}
	
	public int newsDelete(int boardNo) {
		return sqlSession.update("newsMapper.newsDelete", boardNo);
	}

	public NewsDto getFileName(int boardNo) {
		return sqlSession.selectOne("newsMapper.getFileName", boardNo);
	}
	
	public int setUploadUpdate(NewsDto news) {
		return sqlSession.update("newsMapper.setUploadUpdate", news);
	}
	
	public int edit(NewsDto news) {
		return sqlSession.update("newsMapper.edit", news);
	}
}










