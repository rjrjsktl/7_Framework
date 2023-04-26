package edu.kh.comm.member.model.service;

import edu.kh.comm.member.model.vo.Member;

/* Service Interface를 사용하는 이유
 * 
 * 1. 프로젝트에 규칙성을 부여하기 위해서
 * 
 * 2. Spring의 AOP(관점 지향 프로그래밍)를 위해서 필요
 * 
 * 3. 클래스간의 결합도를 약화 시키기 위해서
 */

public interface MemberService {
	
	// 모든 메서드가 추상 메서드이다 (묵시적으로 public abstract)
	// 모든 필드는 상수				 (묵시적으로 public static final)
	
	/** 로그인 서비스
	 * @param inputMember
	 * @return loginMember
	 */
	//public abstract이게 없어도 됨
	public abstract Member login(Member inputMember);

	/** 이메일 중복검사 서비스
	 * @param memberEmail
	 * @return result
	 */
	public abstract int emailDupCheck(String memberEmail);

	/** 닉네임 중복검사 서비스
	 * @param memberNickname
	 * @return
	 */
	public abstract int nicknameDupCheck(String memberNickname);

	public abstract int signUp(Member mem);

	
}
