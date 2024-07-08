package kr.co.green.member.model.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import kr.co.green.member.model.dao.MemberDao;
import kr.co.green.member.model.dto.MemberDto;

@Service
public class MemberServiceImpl implements MemberService {
	
	// 순환 참조 문제
//	@Autowired
//	public MemberDao memberDao;
	
	private final MemberDao memberDao;
	private final BCryptPasswordEncoder passwordEncoder;
	@Autowired
	public MemberServiceImpl(MemberDao memberDao,
			                 BCryptPasswordEncoder passwordEncoder) {
		this.memberDao = memberDao;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public int getCheckId(String memberId) {
		return memberDao.getCheckId(memberId);
		
	}
	
	@Override
	public int setRegister(MemberDto member) {
		String name = member.getMemberName();
		String password = member.getMemberPassword();
		String confirmPassword = member.getConfirPassword();
		
		String nameRegex = "^[가-힣]+$";
		String passwordRegex = "^(?=.*[a-zA-Z])(?=.*[@!$^*])[a-zA-Z0-9@!$^*]{6,20}$";
		
		if(name.matches(nameRegex) && password.matches(passwordRegex) && password.equals(confirmPassword)) {
			
			// 패스워드 암호화
			String getpassword = passwordEncoder.encode(member.getMemberPassword());
			member.setMemberPassword(getpassword);
			
			return memberDao.setRegister(member);
		} else {
			return 0;
		}
		
	}
	
	@Override
	public MemberDto login(MemberDto member) {
		MemberDto result = memberDao.getInfo(member);
		
		// passwordEncoder.matcher(사용자가입력한 패스워드, 암호화된 패스워드)
		if(!Objects.isNull(result) &&
			passwordEncoder.matches(member.getMemberPassword(), result.getMemberPassword())) {
			return result;
		} else {
			return null;
		}
	}
	
	@Override
	public MemberDto getInfo(String id) {
		return memberDao.getInfo(id);
	}
	
	@Override
	public int deleteMember(String id) {
		return memberDao.deleteMember(id);
	}
	
}
