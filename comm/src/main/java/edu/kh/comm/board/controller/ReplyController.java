package edu.kh.comm.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.comm.board.model.service.ReplyService;
import edu.kh.comm.board.model.vo.Reply;

// Rest(Reprsentational State TransFer)
// - 자원을 이름으로 구분(Representational, 자원의표현)하여
// 자원의 상태(state)를 주고 받는 것(Transfer)

// -> 특정한 이름(주소)로 요청이 오면 값으로 응답 

// RestController : 요청에 따른 응답이 모두 데이터(값) 자체인 컨트롤러
// -> @Controller + @ResponseBody

@RestController
@RequestMapping("/reply")
public class ReplyController {

	@Autowired
	private ReplyService service;
	
	// 댓글 목록 조회 - 완료
	
	// 댓글 등록
	@PostMapping("/insert")
	@ResponseBody
	public String insertReply(@RequestParam("replyContent") String replyContent
								, @RequestParam("memberNo") int memberNo
								, @RequestParam("boardNo") int boardNo
								) {
		Reply reply = new Reply();
		
		reply.setReplyContent(replyContent);
        reply.setMemberNo(memberNo);
        reply.setBoardNo(boardNo);

		int result = service.insertReply(reply);
		
		return result;
	}
	
	// 댓글 삭제
	
	// 댓글 수정
	
	// ---------CRUD만드는거임
	
	
	
	
}
