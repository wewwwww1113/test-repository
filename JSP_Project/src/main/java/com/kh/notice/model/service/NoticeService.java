package com.kh.notice.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.common.JDBCTemplate;
import com.kh.notice.model.dao.NoticeDao;
import com.kh.notice.model.vo.Notice;

public class NoticeService {
	
	//공지사항 목록 메소드
	public ArrayList<Notice> selectNoticeList() {
		//연결객체
		Connection conn = JDBCTemplate.getConnection();
		
		ArrayList<Notice> list = new NoticeDao().selectNoticeList(conn);
		
		//조회(select) 구문이기때문에 트랜잭션 필요없음
		JDBCTemplate.close(conn);
		
		return list;
	}
	//공지사항 글 작성 메소드
	public int insertNotice(Notice n) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new NoticeDao().insertNotice(conn,n);
		
		//dml구문이니 트랜잭션처리 하기 
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
		
	}
	
	//상세보기 메소드
	public Notice selectNotice(int nno) {
		Connection conn = JDBCTemplate.getConnection();
		
		Notice n = new NoticeDao().selectNotice(conn,nno);
		
		//조회구문은 트랜잭션 처리 필요없음
		
		JDBCTemplate.close(conn);
		
		
		return n;
	}
	
	//조회수 증가 메소드
	public int increaseCount(int nno) {
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new NoticeDao().increaseCount(conn,nno);
		
		//dml구문이니 트랜잭션
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}
	//공지사항 수정 메소드
	public int updateNotice(Notice n) {

		Connection conn = JDBCTemplate.getConnection();
		
		int result = new NoticeDao().updateNotice(conn,n);
		
		//dml 구문 - 트랜잭션처리하기 
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
