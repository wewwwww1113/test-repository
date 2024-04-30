package com.kh.notice.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.common.JDBCTemplate;
import com.kh.notice.model.vo.Notice;

public class NoticeDao {
	//기본생성자에서 notice-mapper 파일을 읽어오는 작업 해두기
	
	private Properties prop = new Properties();
	
	
	public NoticeDao() {
		
		String filePath = NoticeDao.class.getResource("/resources/sql/notice-mapper.xml").getPath();
		
		try {
			prop.loadFromXML(new FileInputStream(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//공지사항 목록 메소드
	public ArrayList<Notice> selectNoticeList(Connection conn) {
		//목록 조회 - SELECT 
		//목록을 담아줄 컬렉션 준비 
		ArrayList<Notice> list = new ArrayList<>();
		ResultSet rset = null;
		Statement stmt = null;
		String sql = prop.getProperty("selectNoticeList");
		
		try {
			stmt = conn.createStatement();
			//구문 실행하고 결과집합 받아오기
			rset = stmt.executeQuery(sql);
			
			
//				NOTICE_NO
//		      ,NOTICE_TITLE
//		      ,USER_ID
//		      ,COUNT
//		      ,CREATE_DATE
			//여러 행이 조회될 수 있으니 모두 접근해서 담아주기 
			while(rset.next()) {
				list.add(new Notice(rset.getInt("NOTICE_NO")
								   ,rset.getString("NOTICE_TITLE")
								   ,rset.getString("USER_ID")
								   ,rset.getInt("COUNT")
								   ,rset.getDate("CREATE_DATE")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(stmt);
		}
		return list;
	}
	
	//공지사항 글 작성 메소드
	public int insertNotice(Connection conn, Notice n) {
		
		//DML - insert 
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertNotice");
		
		try {
			//미완성 sql구문 전달하며 pstmt 객체 생성
			pstmt = conn.prepareStatement(sql);
			//미완성 sql구문 완성시키기 위치홀더 채워주기
			pstmt.setString(1, n.getNoticeTitle());
			pstmt.setString(2, n.getNoticeContent());
			//noticeWriter는 number타입이기때문에 int로 넣어주기
			pstmt.setInt(3, Integer.parseInt(n.getNoticeWriter()));
			
			//완성되었으면 실행 및 결과행수 돌려받기 
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		//결과행수 반환하기
		return result;
	}
	
	//상세보기 메소드
	public Notice selectNotice(Connection conn,int nno) {
		//select 
		Notice n = null;//공지글 담을 객체변수
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("selectNotice");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nno);
			
			rset = pstmt.executeQuery();
			
			//단일조회(식별자를 이용한) 
			if(rset.next()) {
				n = new Notice(rset.getInt("NOTICE_NO")
							  ,rset.getString("NOTICE_TITLE")
							  ,rset.getString("NOTICE_CONTENT")
							  ,rset.getString("USER_ID")
							  ,rset.getInt("COUNT")
							  ,rset.getDate("CREATE_DATE"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return n;
	}
	
	
	//조회수 증가 메소드
	public int increaseCount(Connection conn,int nno) {
		
		//DML (UPDATE) 
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("increaseCount");
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nno);
			
			result = pstmt.executeUpdate();
		
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}
	//공지 수정 메소드
	public int updateNotice(Connection conn, Notice n) {
		//DML - UPDATE
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("updateNotice");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, n.getNoticeTitle());
			pstmt.setString(2, n.getNoticeContent());
			pstmt.setInt(3, n.getNoticeNo());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
}
