package edu.kh.comm.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	/*
	 * http://localhost:8080/comm/main
	 * http://localhost:8080/comm/ -> forward의 경우는 요기까지 리다이렉트 시킴 그래서 main만 쓰면 됨
	 */
	@RequestMapping("/main")
	public String mainForward() {
		
		return "common/main";
	}
}
