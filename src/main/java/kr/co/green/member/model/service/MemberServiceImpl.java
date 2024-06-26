package kr.co.green.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.green.member.model.dao.MemberDao;

@Service
public class MemberServiceImpl implements MemberService {
	
	
	@Autowired
	public MemberServiceImpl(MemberDao memberDao) {
		
	}
	
	@Override
	public String getCheckId(String id) {
		return MemberDao.getCheckId(id);
		
	}
	
}
