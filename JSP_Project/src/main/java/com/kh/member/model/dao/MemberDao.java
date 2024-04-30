package com.kh.member.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.kh.common.JDBCTemplate;
import com.kh.member.model.vo.Member;

public class MemberDao {
	
	//sql 구문 읽어서 담아둘 prop 준비 
	private Properties prop = new Properties();
	
	//MemberDao() 기본생성자가 호출될때마다 파일을 읽어올 수 있도록 작업
	public MemberDao() {
		//member-mapper파일의 물리적인 주소(경로) 알아오기
		// getResource에서 /로 시작하면 classes 가 기준이 됨
		String filePath = MemberDao.class
								   .getResource("/resources/sql/member-mapper.xml")
								   .getPath();
//		System.out.println(filePath);
		//해당 sql 구문 파일 경로를 스트림에 담아준다 
		try {
			prop.loadFromXML(new FileInputStream(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//로그인 메소드
	public Member loginMember(Connection conn
							,String userId
							,String userPwd) {
		//select문
		ResultSet rset = null; //결과집합을 담을 ResultSet 
		PreparedStatement pstmt = null;//위치홀더를 이용하여 변수대입해줄수있는 객체
		//userId에 unique 제약조건이 걸려있기때문에 하나 또는 조회안됨 
		Member m = null;//조회된 데이터를 담아줄 객체 
		
		//읽어온 정보에서 키값으로 구문 찾아오기 
		String sql = prop.getProperty("loginMember");
		try {
			//미완성된 sql구문 전달하며 pstmt 객체 생성
			pstmt = conn.prepareStatement(sql);
			//미완성된 sql구문 위치홀더 채워서 완성시켜주기 (위치홀더는 1번부터 시작)
			pstmt.setString(1, userId);
			pstmt.setString(2, userPwd);
			
			//완성되었으니 실행 및 결과받아오기 (select구문이니 executeQuery()메소드 사용)
			rset = pstmt.executeQuery();
			
			//결과집합에 다음 접근할 행이 있나요? 
			if(rset.next()) {
				//있다면 Member 객체에 담아주기 
				m = new Member(rset.getInt("USER_NO")
							  ,rset.getString("USER_ID")
							  ,rset.getString("USER_PWD")
							  ,rset.getString("USER_NAME")
							  ,rset.getString("PHONE")
							  ,rset.getString("EMAIL")
							  ,rset.getString("ADDRESS")
							  ,rset.getString("INTEREST")
							  ,rset.getDate("ENROLL_DATE")
							  ,rset.getDate("MODIFY_DATE")
							  ,rset.getString("STATUS"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return m; //멤버객체변수 반환
	}
	
	//회원가입 메소드
	public int insertMember(Connection conn,Member m) {
		
		//준비물 (회원가입 (DML) : insert )
		PreparedStatement pstmt = null; //sql구문 전달할 객체 
		int result = 0; //처리된 행의 수를 돌려받을 변수 
		String sql = prop.getProperty("insertMember");
		
		try {
			//미완성 SQL구문 전달하며 PSTMT 객체 얻어오기
			pstmt = conn.prepareStatement(sql);
			//미완성 sql구문 완성 시키기 (각 위치홀더에 알맞은 데이터 넣어주기)
			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getPhone());
			pstmt.setString(5, m.getEmail());
			pstmt.setString(6, m.getAddress());
			pstmt.setString(7, m.getInterest());
			
			//완성시킨 sql문 실행하며 반환값 받기 
			//dml 구문은 executeUpdate()메소드 
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			//자원반납
			JDBCTemplate.close(pstmt);
		}
		return result; //결과값 반환하기
	}
	
	
	//회원정보수정 메소드
	public int updateMember(Connection conn,Member m) {
		//DML (UPDATE) 
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updateMember");
		
		try {
			//미완성 SQL 구문 전달하며 PSTMT 객체 생성
			pstmt = conn.prepareStatement(sql);
			//미완성인 sql구문 위치홀더에 맞는 데이터 넣어주기 
			pstmt.setString(1, m.getUserName());
			pstmt.setString(2, m.getPhone());
			pstmt.setString(3, m.getEmail());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getInterest());
			pstmt.setString(6, m.getUserId());
			
			//완성된 sql구문 실행 및 결과 받기 
			result = pstmt.executeUpdate();//처리된 행수
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	
	
	//아이디 하나로 회원정보 조회메소드 
	public Member selectMember(Connection conn,String userId) {
		//SELECT 구문
		ResultSet rset = null; //결과집합담을 변수
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("selectMember");
		Member m = null; //조회된 회원정보가 있으면 담아갈 객체변수
		
		try {
			//미완성 구문 전달하며 객체생성
			pstmt = conn.prepareStatement(sql);
			//위치홀더 채워주기 
			pstmt.setString(1, userId);
			
			//조회구문 실행 및 결과집합 반환받기
			rset = pstmt.executeQuery();
			
			//조회결과가 있다면 우리가 만든 VO객체변수에 담아주기 
			//userId 는 unique제약조건이기때문에 식별자로 사용된다(조회된다면 하나만 조회됨)
			if(rset.next()) {//다음 접근할 행이 있나요? 
				//있다면 해당 정보 옮겨 담기
				m = new Member(rset.getInt("USER_NO")
							  ,rset.getString("USER_ID")
							  ,rset.getString("USER_PWD")
							  ,rset.getString("USER_NAME")
							  ,rset.getString("PHONE")
							  ,rset.getString("EMAIL")
							  ,rset.getString("ADDRESS")
							  ,rset.getString("INTEREST")
							  ,rset.getDate("ENROLL_DATE")
							  ,rset.getDate("MODIFY_DATE")
							  ,rset.getString("STATUS"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return m;//결과객체 반환
		
	}
	
	//회원비밀번호 변경 메소드
	public int updatePwd(Connection conn,String userId,String userPwd,String updatePwd) {
		//DML - update
		int result = 0; //처리된 행수를 받아줄 변수 
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("updatePwd");
		
		try {
			//미완성 sql구문 전달하며 pstmt 객체 생성
			pstmt = conn.prepareStatement(sql);
			//미완성 sql구문 완성하기 (위치홀더 채워주기)
			pstmt.setString(1, updatePwd);
			pstmt.setString(2, userId);
			pstmt.setString(3, userPwd);
			
			//완성된 sql구문 실행 및 결과 반환받기 
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result; //결과값 반환
		
	}
	
	//회원탈퇴용 메소드
	public int deleteMember(Connection conn,String userId){
		//DML - (update로 상태값 변경하여 삭제처리)
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("deleteMember");
		
		try {
			//미완성 sql 구문 전달하며 pstmt 객체 생성
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			//완성된 sql구문 실행 및 결과 받기 
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	
	//아이디 중복확인 메소드 
	public boolean checkId(Connection conn, String inputId) {
		//select 
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("checkId");
		
		boolean flag = false;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inputId);
			
			//해당 아이디가 데이터베이스에 존재하는지 여부만 판별하면 된다.
			rset = pstmt.executeQuery();
			//조회된 행이 있으면 true를 반환하는 next() 메소드 이용하기
			flag = rset.next();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return flag; //조회된 행이 있는지 여부 반환하기
	}
	
	
	
	

}
