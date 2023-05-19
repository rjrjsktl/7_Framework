package edu.kh.comm.board.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.comm.board.model.service.ReplyService;
import edu.kh.comm.board.model.vo.Reply;
import edu.kh.comm.member.model.vo.Member;

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
	private ReplyService replyService;
	
	// 댓글 목록 조회 - 완료
	@GetMapping("/selectReplyList")
	public List<Reply> selectReply(@RequestParam("boardNo") int boardNo
	         						) {
		List<Reply> rList = replyService.selectReplyList(boardNo);
		
		return rList;
	}
	// 댓글 등록
	@PostMapping("/insert")		// loginMember 넣어주는 이유는 이거 안넣으면 mapper갔다오면 0으로 바뀜
	public int insertReply(/*@ModelAttribute("loginMember") Member loginMember
			   				,*/ Reply reply
			   				) {
	    System.out.println("댓글 오냐");
		int result = replyService.insertReply(reply);
	      
		return result;
	}

	// 댓글 삭제
	@GetMapping("/delete")
	public int deleteReply(@RequestParam("replyNo") int replyNo) {
		
		int result = replyService.deleteReply(replyNo);
		
		return result;
	}
	
	// 댓글 수정
	@PostMapping("/update")
	public int updateReply(Reply reply) {
		return replyService.updateReply(reply);
	}
	
	// ---------CRUD만드는거임
	
	
	
	
}
