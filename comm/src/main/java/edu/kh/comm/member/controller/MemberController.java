package edu.kh.comm.member.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.comm.member.model.vo.Member;

// POJO 기반 프레임워크 : 외부 라이브러리 상속 X

// class : 객체를 만들기 위한 설계도
// -> 객체를 생성 되어야지 기능 수행이 가능함 new 클래스명()
// --> IOC(제어의 역전, 객체 생명주기를 스프링이 관리)를 이용하여 객체 생성
// ** 이 때, 스프링이 생성한 객체를 bean 이라고 한다 **

// bean 등록 == 스프링이 객체로 만들어서 가지고 있어라

@Component // 해당 클래스를 bean으로 등록하라고 프로그램에게 알려주는 주석(Annotation)

@Controller // 생성된 bean이 Controller임을 명시 + bean 등록

@RequestMapping("/member") // localhost:8080/comm/member 이하의 요청을 처리하는 컨드롤러

// localhost:8080/comm/member/login
// localhost:8080/comm/member/logout
// 등의 주소를 가져온다
public class MemberController {
	
	private Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	// Controller : 요청/응답을 제어하는 역할을 지닌 클래스
	
	/* @RequestMapping : 클라이언트 요청(url)에 맞는 클래스 or 메서드를 연결시켜주는 어노테이션
	 * 
	 * [위치에 따른 해석]
	 * - 클래스 레벨 : 공통주소 ex)@RequestMapping("/member")
	 * - 메서드 레벨 : 공통주소 외 나머지 주소 ex)
	 * 
	 * 단, 클래스 레벨이 @RequestMapping이 존재하지 않는다면
	 * - 메서드 레벨 : 단독 요청 처리 주소
	 * 
	 * [작성법에 따른 해석]
	 * 
	 * 1) @RequestMapping("url")
	 * --> 요청 방식(GET/POST) 관계 없이 url이 일치하는 요청 처리
	 * 
	 * 2) @RequestMapping(value = "url", method = RequestMethod.GET || POST)
	 * --> 요청 바식에 따라 요청 처리
	 * 
	 * ** 메서드 레벨에서 GET/POST 방식을 구분하여 매핑 **
	 * 
	 * @GetMapping("url") / @PostMapping("url")
	 * 
	 * 
	 */
	
	// 로그인
	// 요청 시 파라미터를 얻어오는 방법 1
	//  -> HttpServletRequest 이용
	/*@RequestMapping("/login")
	public String login(HttpServletRequest req) {
		logger.info("로그인 요청됨");
		
		String inputEmail = req.getParameter("inputEmail");
		String inputPw = req.getParameter("inputPw");
		
		logger.debug("inputEmail:: " + inputEmail);
		logger.debug("inputPw:: " + inputPw);

		return "redirect:/";
	}*/
	
	
	// 요청 시 파라미터를 얻어오는 방법 2
	// -> @RequestParam 어노테이션 사용
	
	// @RequestParam("name속성값") 자료형 변수명
	// - 클라이언트 요청 시 같이 전달된 파라미터를 변수에 저장
	// --> 어떤 파라미터를 변수에 저장할지는 "name속성값"을 이용해 지정
	
	// @RequestParam을 생략하지만 파라미터를 얻어오는 방법!
	// -> name 속성값과 파라미터를 저장할 변수 이름을 동일하게 작성
	
	// [ 속성 ]
	// value : input 태그의 name 속성값
	// required : 입력된 name 속성값이 필수적으로 파라미터에 포함되어야 하는지를 지정
	//			required = true / false (기본값 true)
	
	// defaultValue : required가 false인 상태에서 파라미터가 존재하지 않을 경우의 값을 지정
	
//	@RequestMapping("/login")			
//	public String login(/*@RequestParam("inputEmail")*/ String inputEmail,
//						/*@RequestParam("inputPw")*/ String inputPw,
//						@RequestParam(value="inputName", required=false, defaultValue = "홍길동") String inputName
//						// 밑에 뜻은 inputEmail(Pw)로 가져온 값을 email(pw)에 저장한다는 뜻
//			) {
//						
//	
//		logger.info("로그인 요청됨");
//		
//		logger.debug("inputEmail:: " + inputEmail);
//		logger.debug("inputPw:: " + inputPw);		
//		logger.debug("inputName::" + inputName);
//		
//		
//		
//		return "redirect:/";
//	}
	
	// 요청 시 파라미터를 얻어오는 방법 3
	// -> @ModelAttribute 어노테이션 사용
	
	// [@ModelAttribute를 매개변수에 작성하는 경우]
	// @ModelAttribute VO타입 변수명
	
	// @RequestMapping(value = "/login", method = RequestMethod.POST)
	@PostMapping("/login")
	public String login( @ModelAttribute Member inputMember ) {
		
		logger.info("로그인 기능 수행됨");

		return "redirect:/";
	}
	
	// 회원 가입 화면 전환
	@GetMapping("/signUp") // Get방식 : /comm/member/signUp 요청이 옴
	public String signUp() {
		
		return "member/signUp";
	}
	
	
}
