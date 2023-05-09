package edu.kh.comm.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.comm.board.model.dao.ReplyDAO;
import edu.kh.comm.board.model.vo.Reply;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyDAO dao;

	@Override
	public List<Reply> selectReplyList(int boardNo) {
		
		return dao.selectReplyList(boardNo);
	}

	@Override
	public int insertReply(Reply reply) {
		return dao.insertReply(reply);
	}
	
	
}
