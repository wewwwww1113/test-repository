package com.kh.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplate {
	
//	public static void main(String[] args) {
//		String filePath = JDBCTemplate.class.getResource("/resources/driver/driver.properties").getPath();
//		System.out.println(filePath);
//	}
	//1.Connection 객체 반환하는 메소드 getConnection() 작성하기 
	public static Connection getConnection() {
		
		Properties prop = new Properties();
		//driver.properties 파일을 읽어오기 
		//이때 읽어오는 작업을 하려면 물리적인 위치를 지정해서 찾아와야한다.
		String filePath = JDBCTemplate.class.getResource("/resources/driver/driver.properties").getPath();
		//커넥션 선언 준비
		Connection conn = null;
		try {
			prop.load(new FileInputStream(filePath));
			
//			System.out.println(filePath);
			
			//properties 파일 읽어왔으니 driver 등록하기
			//1.jdbc Driver 등록
			Class.forName(prop.getProperty("driver"));
			//2.Connection 객체 생성
			conn = DriverManager.getConnection(prop.getProperty("url")
											  ,prop.getProperty("username")
											  ,prop.getProperty("password"));
			//3.autoCommit 해제하기 (트랜잭션처리 직접할것)
			conn.setAutoCommit(false);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//생성한 커넥션 객체 반환하기
		return conn;
	}
	
	//2. commit 메소드
	public static void commit(Connection conn) {
		
		//커넥션 객체가 null이거나 닫혀있지 않은지 확인 작업 
		try {
			//null이 아니고 닫혀있지 않은지
			if(conn != null && !conn.isClosed()) {
				conn.commit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//3. rollback 메소드
		public static void rollback(Connection conn) {
			
			//커넥션 객체가 null이거나 닫혀있지 않은지 확인 작업 
			try {
				//null이 아니고 닫혀있지 않은지
				if(conn != null && !conn.isClosed()) {
					conn.rollback();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
		//4.자원반납 메소드 3개 (conn,stmt,rset)
		public static void close(Connection conn) {
			
			try {
				if(conn!=null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Statement 객체 자원반납 - (PreparedStatement 는 다형성적용으로 한번에 처리가능)
		public static void close(Statement stmt) {
			
			try {
				if(stmt!=null&& !stmt.isClosed()) {
					stmt.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//ResultSet 객체 자원반납
		public static void close(ResultSet rset) {
			try {
				if(rset != null && !rset.isClosed()) {
					rset.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	
	
}
