package edu.kh.comm.board.model.service;

import java.util.List;

import edu.kh.comm.board.model.vo.Reply;

public interface ReplyService {

	/** 댓글 목록 조회
	 * @param boardNo
	 * @return
	 */
	List<Reply> selectReplyList(int boardNo);

	int insertReply(Reply reply);

	

}
