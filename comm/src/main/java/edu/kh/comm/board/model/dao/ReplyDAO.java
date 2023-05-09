package edu.kh.comm.board.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.comm.board.model.vo.Reply;

@Repository
public class ReplyDAO {

	@Autowired
	private SqlSessionTemplate sqlSession;


	public List<Reply> selectReplyList(int boardNo) {
		return sqlSession.selectList("replyMapper.selectReplyList", boardNo);
	}


	public int insertReply(Reply reply) {
		return sqlSession.insert("replyMapper.insertReply", reply);
	}
}
