package edu.kh.comm.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.comm.member.model.vo.Member;

@Repository // 영속성을 가지는 DB/파일과 연결되는 클래스임을 명시 + bean 등록
public class MemberDAO {

	/* DAO는 DB랑 연결하기 위한 Connection이 공통적으로 필요하다!
	 * -> 필드 선언
	 * 
	 * + Mybatis 영속성 프레임워크를 통해서 이용하려면 Connection을 이요해 만든 객체
	 * 	SqlSessionTemplate을 사용 
	 * 
	 */
	@Autowired // root-context.xml에서 생성된 SqlSessionTemplate bean을 의존성 주입(DI)
	private SqlSessionTemplate sqlSession;
	
	private Logger logger = LoggerFactory.getLogger(MemberDAO.class);
	
	public Member login(Member inputMember) {
		
		// 1행 조회(파라미터 x) 방법
		// int count = sqlSession.selectOne("namespace값.id값");
//		int count = sqlSession.selectOne("memberMapper.test1");
//		logger.debug(count + "");
		
		// 1행 조회(파라미터 o) 방법
		// String memberNickname = sqlSession.selectOne("memberMapper.test2", inputMember.getMemberEmail());
		// logger.debug(memberNickname);
		
		// 1행 조회(파라미터 VO 경우)
		// String memberTel = sqlSession.selectOne("memberMapper.test3", inputMember);
																	// memberEmail, memberPw
		// logger.debug(memberTel);
		
		// 1행 조회(파라미터 VO, 반환되는 결과 VO) 
		Member loginMember = sqlSession.selectOne("memberMapper.login", inputMember);
		
		return loginMember;
	}

	/** 이메일 중복 검사 DAO
	 * @param memberEmail
	 * @return
	 */
	public int emailDupCheck(String memberEmail) {
		return sqlSession.selectOne("memberMapper.emailDupCheck", memberEmail);
	}

	/** 닉네임 중복 검사 DAO
	 * @param memberNickname
	 * @return
	 */
	public int nicknameDupCheck(String memberNickname) {
		return sqlSession.selectOne("memberMapper.nicknameDupCheck", memberNickname);
	}

	/** 회원 가입 DAO
	 * @param mem
	 * @return
	 */
	public int signUp(Member mem) {
		return sqlSession.insert("memberMapper.signUp", mem);
	}

}
