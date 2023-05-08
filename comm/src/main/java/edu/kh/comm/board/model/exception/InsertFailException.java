package edu.kh.comm.board.model.exception;

// 사용자 정의 예외 생성
// 1) 기존에 존재하는 예외 클래스 하나를 상속받음

// 2) 생성자 작성시 super() 생성자를 이용해서 코드 구현

public class InsertFailException extends RuntimeException {
	
	public InsertFailException() {
		super("게시글 삽입 실패");
	}
	
	public InsertFailException(String message) {
		super(message);
	}

}
