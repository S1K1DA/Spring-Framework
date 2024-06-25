package kr.co.green.board.model.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.common.pageing.PageInfo;

@Repository
public class FreeDao {
	private final SqlSessionTemplate sqlSession;
	
	@Autowired
	public FreeDao(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public List<FreeDto> freeList(PageInfo pi, FreeDto free) {
		// 작은 규모일 때 사용 권장
		// 단점 : 성능이 안좋음
		RowBounds rb = new RowBounds(pi.getOffset(), pi.getBoardLimit());
		return sqlSession.selectList("freeMapper.freeList", free, rb);
	}

	public int getListCount(FreeDto free) {
		return sqlSession.selectOne("freeMapper.getListCount", free);
	}

	public FreeDto getDetail(FreeDto free) {
		return sqlSession.selectOne("freeMapper.getDetail", free);
	}
	
	
}










