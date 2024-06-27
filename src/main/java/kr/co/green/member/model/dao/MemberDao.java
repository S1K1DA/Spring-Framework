package kr.co.green.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.green.member.model.dto.MemberDto;

@Repository
public class MemberDao {
	private final SqlSessionTemplate sqlSession;
	
	@Autowired
	public MemberDao(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	// id 중복 체크
	public int getCheckId(String memberId) {
		return sqlSession.selectOne("memberMapper.getCheckId", memberId);
	}
	
	// 회원 가입
	public int setRegister(MemberDto member) {
		return sqlSession.insert("memberMapper.setRegister", member);
	}
	
	// 로그인
	public MemberDto getInfo(MemberDto member) {
		return sqlSession.selectOne("memberMapper.getInfo", member);
	}

	
}