package edu.kh.comm.board.model.service;

import java.util.List;

import edu.kh.comm.board.model.vo.Reply;

public interface ReplyService {

	/** 댓글 목록 조회
	 * @param boardNo
	 * @return
	 */
	List<Reply> selectReplyList(int boardNo);

	/** 댓글 등록 서비스
	 * @param reply
	 * @return result
	 */
	int insertReply(Reply reply);

	

}
