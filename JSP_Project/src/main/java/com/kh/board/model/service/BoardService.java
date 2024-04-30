package com.kh.board.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.board.model.dao.BoardDao;
import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Category;
import com.kh.board.model.vo.Reply;
import com.kh.common.JDBCTemplate;
import com.kh.common.model.vo.PageInfo;

public class BoardService {

	public int listCount() {
		
		//Connection 객체 
		Connection conn = JDBCTemplate.getConnection();
		
		int listCount = new BoardDao().listCount(conn);
		
		//조회구문이니 트랜잭션처리 필요없음
		JDBCTemplate.close(conn);
		
		return listCount;
	}
	
	//게시글 목록조회 메소드
	public ArrayList<Board> selectList(PageInfo pi) {
		Connection conn = JDBCTemplate.getConnection();
		
		ArrayList<Board> list = new BoardDao().selectList(conn,pi);
		
		//select 구문은 트랜잭션처리 필요없으니 자원반납만 하면 된다
		JDBCTemplate.close(conn);
		
		return list;
	}
	//조회수 증가메소드
	public int increaseCount(int bno) {
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new BoardDao().increaseCount(conn,bno);
		
		//DML- UPDATE 구문이 수행되었으니 트랜잭션 처리 필수
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		//트랜잭션 처리되었으니 자원반납
		JDBCTemplate.close(conn);
		
		return result;
	}
	//게시글 상세조회
	public Board selectBoard(int bno) {
		Connection conn = JDBCTemplate.getConnection();
		
		Board b = new BoardDao().selectBoard(conn,bno);
		//select구문이니 트랜잭션 처리필요없음 
		JDBCTemplate.close(conn);
		
		return b;
	}
	
	//카테고리 목록 조회 메소드
	public ArrayList<Category> selectCategoryList() {
		
		Connection conn = JDBCTemplate.getConnection();
		
		ArrayList<Category> cList = new BoardDao().selectCategoryList(conn);
		
		//select는 조회구문이니 트랜잭션 처리 필요없고 자원 반납만 하면 된다
		JDBCTemplate.close(conn);
		
		
		return cList;
	}
	//게시글 등록 메소드
	public int insertBoard(Board b, Attachment at) {
		Connection conn = JDBCTemplate.getConnection();
		//boardNo 를 먼저 추출하여 해당 번호로 게시글과 첨부파일을 넣어주기 
		int boardNo = new BoardDao().selectBoardNo(conn);
		
		if(boardNo != 0) { //추출한 게시글번호가 0이 아닐때(제대로 추출되었을때)
			
			//게시글 객체에 추출한 게시글번호 넣어주기 
			b.setBoardNo(boardNo);
			//게시글 정보 전달하며 처리 결과 받아오기 
			int result = new BoardDao().insertBoard(conn,b);
			
			//첨부파일이 없는 경우 게시글만 등록되어야하니까 1로 처리해두고 문제가생기면 0으로 변경되게끔 처리 
			int result2 = 1;
			//게시글이 성공적으로 등록되었고 
			//첨부파일이 있다면 등록해주기 
			if(result>0 && at!=null) {
				
				at.setRefBno(boardNo); //첨부파일객체에 참조게시글번호 넣어주기 
				//성공시 1 실패시 0 
				result2 = new BoardDao().insertAttachment(conn,at);
			}
			
			//게시글과 첨부파일 모두 잘 성공되었을때 확정작업하기 아니라면 되돌리기
			//두 작업다 성공했는지 판별 
			//result!=0 && result2!=0
			//result*result2  둘중 하나라도 0이면 0이되는 판별작업
			if(result*result2>0) {//둘다 0이 아니기때문에 성공(commit)
				JDBCTemplate.commit(conn);
			}else {//둘중에 하나라도 0이되었기때문에 실패 (rollback)
				JDBCTemplate.rollback(conn);
			}
			
			//자원반납
			JDBCTemplate.close(conn);
			//DML구문이 제대로 처리됐을때 또는 안됐을때 
			return result*result2; 
		}else {
			//자원반납
			JDBCTemplate.close(conn);
			//SELECT구문으로 번호 추출이 제대로 되지 않았을때
			return boardNo; //등록처리 하지 않고 되돌아가기 
		}
		
	}
	//첨부파일 조회메소드
	public Attachment selectAttachment(int bno) {
		Connection conn = JDBCTemplate.getConnection();
		
		Attachment at = new BoardDao().selectAttachment(conn,bno);
		
		//select 
		JDBCTemplate.close(conn);
		
		return at;
	}
	
	//게시글 수정 메소드 
	public int updateBoard(Board b, Attachment at) {
		Connection conn = JDBCTemplate.getConnection();
		
		//게시글 수정 
		int result = new BoardDao().updateBoard(conn,b);
		
		//첨부파일 수정 
		int result2 = 1; //첨부파일이 없어도 일반게시글 처리만으로 수행될수있도록 1로 만들어두기
		
		
		//첨부파일이 있다면 등록or수정 작업을 하시오
		if(at!=null) {
			
			//등록인지 수정인지 판별하기 
			if(at.getFileNo()!=0) { //fileNo가 0(기본값)이 아닐때 - 기존첨부파일이 있는경우 
				result2 = new BoardDao().updateAttachment(conn,at);
			}else { //기존에 첨부파일이 없던 경우 - 등록
				result2 = new BoardDao().insertAttachment(conn, at);
			}
		}
		//트랜잭션처리하기 
		//둘중 하나라도 문제가 있다면 전체 되돌리기 위한 묶음처리 
		if(result*result2>0) { //둘다성공 또는 게시글만성공(첨부파일없는경우)
			JDBCTemplate.commit(conn);
		}else {//둘중 하나라도 실패한경우 
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);
		
		return result*result2;
	}
	
	//게시글 삭제메소드
	public int deleteBoard(int boardNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new BoardDao().deleteBoard(conn,boardNo);
		//DML (UPDATE/DELETE) 구문이니 트랜잭션 처리
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);
		
		return result;
	}
	
	//사진게시글 작성메소드
	public int insertThumbnail(Board b, ArrayList<Attachment> atList) {
		Connection conn = JDBCTemplate.getConnection();
		
		//게시글 번호 먼저 추출하기 (해당 번호를 첨부파일이 참조해야하기때문에)  
		int boardNo = new BoardDao().selectBoardNo(conn);
		
		//게시글 등록 
		b.setBoardNo(boardNo); //게시글번호 넣어주기 
		int result = new BoardDao().insertThumbnail(conn, b);
		
		//사진 등록(파일처리) 
		//참조할 게시글 번호 추가해주기 
		int result2 = new BoardDao().insertAttachmentList(conn,atList,boardNo);
		
		//트랜잭션처리하기
		if(result*result2>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result*result2;
	}
	
	//사진게시글 목록 조회메소드
	public ArrayList<Board> selectThumbnailList() {
		Connection conn = JDBCTemplate.getConnection();
		
		ArrayList<Board> list = new BoardDao().selectThumbnailList(conn);
		
		//select니까 자원반납만 
		JDBCTemplate.close(conn);
		
		return list;
	}
	
	//사진게시글 상세보기 사진목록 조회 메소드 
	public ArrayList<Attachment> selectAttachmentList(int boardNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		ArrayList<Attachment> atList = new BoardDao().selectAttachmentList(conn, boardNo);
		
		JDBCTemplate.close(conn);
		
		return atList;
	}
	//댓글작성
	public int insertReply(Reply r) {
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new BoardDao().insertReply(conn,r);
		
		//DML 트랜잭션처리 
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}
	//댓글 목록 조회
	public ArrayList<Reply> replyList(int refBno) {
		Connection conn = JDBCTemplate.getConnection();
		
		ArrayList<Reply> list = new BoardDao().replyList(conn,refBno);
		
		//select 트랜잭션 X
		
		JDBCTemplate.close(conn);
		
		return list;
	}
	

}
