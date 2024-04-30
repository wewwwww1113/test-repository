package com.kh.member.model.service;

import java.sql.Connection;

import com.kh.common.JDBCTemplate;
import com.kh.member.model.dao.MemberDao;
import com.kh.member.model.vo.Member;

public class MemberService {

	// 서비스에서는 Connection에 관련된 작업을 수행하는 구간

	// 로그인 메소드 (아이디와 비밀번호를 controller에서 전달받는다)
	public Member loginMember(String userId, String userPwd) {
		// 연결객체(Connection)얻어오기
		Connection conn = JDBCTemplate.getConnection();

		// DAO에게 전달받은 데이터와 Connection 객체 전달하기
		Member m = new MemberDao().loginMember(conn, userId, userPwd);

		// select구문이니 트랜잭션처리는 필요없다. 자원반납만 하면 됨
		JDBCTemplate.close(conn);

		// controller에게 반환받은 데이터 전달
		return m;
	}

	// 회원가입 메소드
	public int insertMember(Member m) {

		// 커넥션객체 얻어오기
		Connection conn = JDBCTemplate.getConnection();
		// 커넥션과 전달받은데이터를 함께 dao에게 전달하며 요청보내고 반환값 받아주기
		int result = new MemberDao().insertMember(conn, m);

		// 트랜잭션 처리하기 (DML 구문이기때문에)
		if (result > 0) { // 성공시 확정
			JDBCTemplate.commit(conn);
		} else {// 실패시 되돌리기
			JDBCTemplate.rollback(conn);
		}

		// 처리 끝났으니 자원반납
		JDBCTemplate.close(conn);

		// 컨트롤러에서 사용자에게 성공 실패시 화면을 판별하여 보여주기 위해 반환값 전달하기
		return result;
	}
	/*
	 * //회원정보 수정메소드 - 로그인정보 갱신작업 컨트롤러부터 진행 public int updateMember(Member m) {
	 * //커넥션객체 얻어오기 Connection conn = JDBCTemplate.getConnection(); //dao에 전달받은 데이터와
	 * 커넥션 같이 전달 및 요청보내기 int result = new MemberDao().updateMember(conn,m);
	 * 
	 * //DML구문은 트랜잭션처리 if(result>0) {//성공 JDBCTemplate.commit(conn);
	 * 
	 * }else { //실패 JDBCTemplate.rollback(conn); }
	 * 
	 * JDBCTemplate.close(conn);
	 * 
	 * return result;//결과값 반환 }
	 */

	// 회원정보 수정메소드 - 로그인정보 갱신작업 서비스부터 처리
	public Member updateMember(Member m) {
		// 커넥션객체 얻어오기
		Connection conn = JDBCTemplate.getConnection();
		// dao에 전달받은 데이터와 커넥션 같이 전달 및 요청보내기
		int result = new MemberDao().updateMember(conn, m);
		// 갱신정보 담을 객체변수
		Member updateMember = null;
		// DML구문은 트랜잭션처리
		if (result > 0) {// 성공
			JDBCTemplate.commit(conn);
			// 갱신된 회원의 정보를 조회하여 반환하기
			// selectMember 메소드를 이용해서 userId만 가지고 회원정보 조회해오기
			updateMember = new MemberDao().selectMember(conn, m.getUserId());

		} else { // 실패
			JDBCTemplate.rollback(conn);
		}

		JDBCTemplate.close(conn);

		return updateMember;// 결과값 반환

	}
	
	//회원비밀번호 변경 메소드
	public int updatePwd(String userId,String userPwd,String updatePwd) {
		//연결객체 얻어오기
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new MemberDao().updatePwd(conn,userId,userPwd,updatePwd);
		
		//dml구문이니 트랜잭션처리할것
		if(result>0) {//성공시 확정
			JDBCTemplate.commit(conn); 
		}else {//실패시 되돌리기
			JDBCTemplate.rollback(conn);
		}
	
		//자원반납
		JDBCTemplate.close(conn); 
		
		return result;
	}
	
	//회원탈퇴용 메소드
	public int deleteMember(String userId) {
		Connection conn = JDBCTemplate.getConnection();
		
		
		int result = new MemberDao().deleteMember(conn,userId);
		
		//dml구문은 트랜잭션 처리하기
		if(result>0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}
	
	//아이디 중복체크 메소드
	public Boolean checkId(String inputId) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		Boolean flag = new MemberDao().checkId(conn,inputId);
		
		JDBCTemplate.close(conn);
		
		return flag;
		
	}
	
	
	
	
	

}
