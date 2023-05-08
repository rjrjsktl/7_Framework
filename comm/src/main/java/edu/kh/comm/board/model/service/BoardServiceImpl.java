package edu.kh.comm.board.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractUserDetailsReactiveAuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.comm.board.model.dao.BoardDAO;
import edu.kh.comm.board.model.exception.InsertFailException;
import edu.kh.comm.board.model.vo.Board;
import edu.kh.comm.board.model.vo.BoardDetail;
import edu.kh.comm.board.model.vo.BoardImage;
import edu.kh.comm.board.model.vo.BoardType;
import edu.kh.comm.board.model.vo.Pagination;
import edu.kh.comm.common.Util;

@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardDAO dao;

	// 게시판 코드, 이름 조회
	@Override
	public List<BoardType> selectBoardType() {
		
		return dao.selectBoardType();
	}

	// 게시글 목록 조회 서비스 구현
	@Override
	public Map<String, Object> selectBoardList(int cp, int boardCode) {
		// 1) 게시판 이름 조회 -> 인터셉터로 application에 올려둔 boardTypeList 쓸 수 있을듯?
		
		// 2) 페이지네이션 객체 생성(listCount)
		int listCount = dao.getListCount(boardCode);
		Pagination pagination = new Pagination(cp, listCount);
		
		
		// 3) 게시글 목록 조회
		List<Board> boardList = dao.selectBoardList(pagination, boardCode);
		
		// map 만들어 담기
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		
		return map;
	}

	// 게시글 상세 조회 서비스 구현
	@Override
	public BoardDetail selectBoardDetail(int boardNo) {

		return dao.selectBoardDetail(boardNo);
	}

//	@Override
//	public int updateReadCount(int boardNo) {
//
//		return dao.updatereadCount(boardNo);
//	}
	
	// 조회수 증가 서비스 구현
	@Override
	public int updateReadCount1(int boardNo) {
		return dao.updateReadCount1(boardNo);
	}

	// 게시글 삭제 서비스 구현
//	@Override
//	public int updateBoardDelete(int boardNo) {
//		return dao.updateBoardDelete(boardNo);
//	}

	// 게시글 삽인 + 이미지 삽입 서비스 구현
	@Transactional(rollbackFor = {Exception.class})
	@Override
	public int insertBoard(BoardDetail detail, List<MultipartFile> imageList, String webPath, String folderPath) throws IOException {
		
		// 1. 게시글 삽입
		
		// 1) XSS 방지 처리 + 개행문자 처리 (util에 이미 다 만들어 둠)
		detail.setBoardTitle(Util.XSSHandling(detail.getBoardTitle()));
		detail.setBoardContent(Util.XSSHandling(detail.getBoardContent())); // 이 두줄이 방지 처리
		
		detail.setBoardContent(Util.newLineHandling(detail.getBoardContent())); // 개행문자 처리
		
		// 2) 게시글 삽입 DAO 호출 후 게시글 번호 반환 받기
		// 1. 서비스 결과 반환 후 컨트롤러에서 상세조회로 리다이렉트 하기 위해서
		// 2. 동일한 시간에 삽입이 2회이상 진행된 경우 시퀀스 번호가 의도와 달리 여러번 증가
		//  -> 이후에 작성된 이미지 삽입 코드에 영향을 미치는걸 방지하기 위해
		
		int boardNo = dao.insertBoard(detail);
		
		if (boardNo > 0 ) { // boardNo 값이 왔을경우
			// 이미지 삽입 ㄱㄱ
		
			// imageList : 실제 우리가 올린 파일이 담겨 있음
			// boardImageList : DB에 삽입할 이미지 정보만 닮겨있는 리스트
			// reNameList : 변경된 파일명이 담겨있는 리스트
			
			List<BoardImage> boardImageList = new ArrayList<BoardImage>();
			List<String> reNameList = new ArrayList<String>();
			
			// imageList에 담겨있는 파일 정보 중 실제 업로드된 파일만 분류하는 작업
			for(int i = 0; i < imageList.size(); i++) {
				if(imageList.get(i).getSize() > 0) { // i번째 요소에 업로드된 이미지가 있을 경우
					
					// 변경된 파일명 저장
					// 2025024022_20 형식 같이 넣기
					String reName = Util.fileRename(imageList.get(i).getOriginalFilename());
					reNameList.add(reName);
					
					// BoardImage 객체를 생성하여 값 세팅후 boardImageList 추가
					BoardImage img = new BoardImage();
					img.setBoardNo(boardNo); // 게시글 번호
					img.setImageLevel(i); // 이미지 순서, 파일레벨 ( 순서대로 들어와야 함 )
					img.setImageOriginal(imageList.get(i).getOriginalFilename()); // 원본 파일명
					img.setImageReName(webPath + reName); // 웹 접근 경로 + 변경된 파일명
					
					boardImageList.add(img);
					
				}
			}
			
			// 분류 작업 종료 후 boardImageList가 비어있지 않은 경우 == 파일이 업로드가 된 경우
			if( !boardImageList.isEmpty() ) {
				int result = dao.insertBoardImageList(boardImageList);
				
				// result == 삽입 성공한 행의 개수
				
				if( result == boardImageList.size() ) { // 삽압된 행의 개수와 업로드 이미지 수가 같을 경우
					
					// 서버에 이미지 저장
					for(int i = 0; i < boardImageList.size(); i++) {
						int index = boardImageList.get(i).getImageLevel();
						
						imageList.get(index).transferTo(new File( folderPath + reNameList.get(i)));
					}
					
				} else { // 이미지 삽입 실패 시
					
					// 강제로 예외를 발생시켜 rollback을 수행하게 함
					// -> 사용자 정의 예외
					throw new InsertFailException();
				}
				
			}
			
		}
		return boardNo;
	}
	// 위에 관한 내용
	// Spring에서 트랜잭션 처리하는 방법
	// * AOP(관점 지향 프로그래밍)을 이요해서 DAO -> Service 코드 수행 시점에
	// 예외가 발생하면 rollback을 수행
	// 방법 1) <tx:advice> XML을 이요한 방법 -> 패턴을 지정하여 일치하는 메서드 호출 시 자동으로 트랜잭션 제어

	// 방법 2) @Transactional 선언적 트랜잭션 처리 방법
	//		-> RuntimeException(Unchecked Exception) 처리를 기본값으로 찾음.
	
	// checked Exception : 예외 처리가 필수 (transFerto()와 같은) -> SQL 관련 예외, 파일 업로드 관련 예외
	// Unchecked Exception : 예외 처리가 선택 (int a = 10/0;와 같은)
	
	// rollbackFor : rollback을 수행하기 위한 예외의 종류를 작성

	
	
	// 게시글 수정
	@Transactional(rollbackFor = {Exception.class}) // 모든 종류의 예외 발생시 롤백
	@Override
	public int updateBoard(BoardDetail detail, List<MultipartFile> imageList, String webPath, String folderPath,
			String deleteList) throws IOException {
		
		// 1) XSS 방지 처리 + 개행문자 처리 (util에 이미 다 만들어 둠)
		detail.setBoardTitle(Util.XSSHandling(detail.getBoardTitle()));
		detail.setBoardContent(Util.XSSHandling(detail.getBoardContent())); // 이 두줄이 방지 처리
				
		detail.setBoardContent(Util.newLineHandling(detail.getBoardContent())); // 개행문자 처리

		// 2) 게시글(제목, 내용, 마지막 수정일(sysdate))만 수정하는 DAO 호출
		int result = dao.updateBoard(detail);
		
		if(result > 0) {
			// 3) 업로드된 이미지만 분류하는 작업 수행
			

			List<BoardImage> boardImageList = new ArrayList<BoardImage>();
			List<String> reNameList = new ArrayList<String>();
			
			// imageList에 담겨있는 파일 정보 중 실제 업로드된 파일만 분류하는 작업
			for(int i = 0; i < imageList.size(); i++) {
				if(imageList.get(i).getSize() > 0) { // i번째 요소에 업로드된 이미지가 있을 경우
					
					// 변경된 파일명 저장
					// 2025024022_20 형식 같이 넣기
					String reName = Util.fileRename(imageList.get(i).getOriginalFilename());
					reNameList.add(reName);
					
					// BoardImage 객체를 생성하여 값 세팅후 boardImageList 추가
					BoardImage img = new BoardImage();
					img.setBoardNo(detail.getBoardNo()); // 게시글 번호
					img.setImageLevel(i); // 이미지 순서, 파일레벨 ( 순서대로 들어와야 함 )
					img.setImageOriginal(imageList.get(i).getOriginalFilename()); // 원본 파일명
					img.setImageReName(webPath + reName); // 웹 접근 경로 + 변경된 파일명
					
					boardImageList.add(img);
					
				}
			}
			// 4) deleteList를 이용해서 삭제된 이미지 delete
			if(deleteList.equals("")) {
				
				Map<String, Object> map = new HashMap</*String, Object*/>();
				
				map.put("boardNo", detail.getBoardNo());
				map.put("deleteList", deleteList);
				
				result = dao.deleteBoardImage(map);
			}
			
			if(result > 0) {
				
				// 5) boardImageList를 순차 접근하면서 하나씩 update
				for(BoardImage img : boardImageList) {
					result = dao.updateBoardImage(img); // 변경명, 원본명, 게시글번호, 레벨
					// 결과 1 -> 수정 o -> 기존 이미지가 있었다
					// 결과 0 -> 수정 x -> 기존 이미지가 없었다
					// -> insert 작업 수행
					
					// 6) update를 실패하면 insert
					if(result == 0) {
						result = dao.insertBoardImage(img);
						// -> 값을 하나씩 대입해서 삽입하는 경우 결과가 0이 나올 수 없다!
						// 단, 예외(제약조건 위배, sql 문법 오류 등)는 발생할 수 있다
						
					}
				}
				
				// 7) 업로드된 이미지가 있다면 서버에 저장
				if( !boardImageList.isEmpty() && result != 0) {
					
					// 서버에 이미지 저장
					for(int i = 0; i < boardImageList.size(); i++) {
						int index = boardImageList.get(i).getImageLevel();
						
						imageList.get(index).transferTo(new File( folderPath + reNameList.get(i)));
					}
				}
			}
		}
		return result;
	}
	
	
	
	
	
}
