package com.kh.board.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Category;
import com.kh.board.model.vo.Reply;
import com.kh.common.JDBCTemplate;
import com.kh.common.model.vo.PageInfo;

public class BoardDao {
	
	
	private Properties prop = new Properties();
	
	
	public BoardDao() {
		
		String filePath = BoardDao.class.getResource("/resources/sql/board-mapper.xml").getPath();
		try {
			prop.loadFromXML(new FileInputStream(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//게시글 총 개수 
	public int listCount(Connection conn) {
		//select 
		ResultSet rset = null;
		Statement stmt = null;
		String sql = prop.getProperty("listCount");
		
		int listCount = 0; //게시글 개수 담을 변수 
		
		
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);
			
			if(rset.next()) {
				//조회된 게시글 개수 
				listCount = rset.getInt("COUNT");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(stmt);
		}
		
		return listCount;
	}
	
	//게시글 목록 조회 메소드
	public ArrayList<Board> selectList(Connection conn, PageInfo pi) {
		//준비물
		ArrayList<Board> list = new ArrayList<>();
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		
		String sql = prop.getProperty("selectList");
		
		//1페이지 : 게시글 1~10번 보여주기 
		//5페이지 : 게시글 41~50번 보여주기 
		//currentPage : 현재페이지 / boardLimit : 한 페이지에서 보여질 게시글 총 개수 
		int startRow = (pi.getCurrentPage()-1) * pi.getBoardLimit()+1;
		int endRow = pi.getCurrentPage() * pi.getBoardLimit();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow); //시작값
			pstmt.setInt(2, endRow); //끝값 
			
			rset = pstmt.executeQuery();
			//목록조회이니 더이상 조회될행이 없을때까지 추출하기
			while(rset.next()) {
				list.add(new Board(rset.getInt("BOARD_NO")
								  ,rset.getString("CATEGORY_NAME")
								  ,rset.getString("BOARD_TITLE")
								  ,rset.getString("USER_ID")
								  ,rset.getInt("COUNT")
								  ,rset.getDate("CREATE_DATE")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}
	//조회수 증가메소드
	public int increaseCount(Connection conn, int bno) {
		
		//dml
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("increaseCount");
		
		try {
			//미완성 sql문 전달하며 pstmt 객체 생성
			pstmt = conn.prepareStatement(sql);
			//위치홀더 채워서 sql구문 완성시키기
			pstmt.setInt(1, bno);
			
			//완성되었으니 실행 및 결과받기
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	//게시글 상세조회
	public Board selectBoard(Connection conn, int bno) {
		
		Board b = null; //게시글정보담을 객체변수
		ResultSet rset = null; 
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("selectBoard");
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				b = new Board(rset.getInt("BOARD_NO")
							  ,rset.getString("CATEGORY_NAME")
							  ,rset.getString("BOARD_TITLE")
							  ,rset.getString("BOARD_CONTENT")
							  ,rset.getString("USER_ID")
							  ,rset.getDate("CREATE_DATE"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return b;
	}
	//카테고리 목록 조회 메소드
	public ArrayList<Category> selectCategoryList(Connection conn) {
		//select 
		ResultSet rset = null;
		ArrayList<Category> cList = new ArrayList<>();
		Statement stmt = null;
		
		String sql = prop.getProperty("selectCategoryList");
		
		try {
			stmt = conn.createStatement();
			
			rset = stmt.executeQuery(sql);
			
			while(rset.next()) {
				cList.add(new Category(rset.getInt("CATEGORY_NO")
									  ,rset.getString("CATEGORY_NAME")));		
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(stmt);
		}
		return cList;
	}
	
	
	//게시글번호 추출 메소드
	public int selectBoardNo(Connection conn) {
		//select 구문으로 시퀀스 발행시키기 
		int boardNo = 0;
		ResultSet rset = null;
		Statement stmt = null; 
		
		String sql = prop.getProperty("selectBoardNo");
		
		try {
			stmt = conn.createStatement();
			
			rset = stmt.executeQuery(sql);
			
			if(rset.next()) {
				boardNo = rset.getInt("BNO");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(stmt);
		}
		
		return boardNo;
	}
	
	
	//게시글 등록 메소드
	public int insertBoard(Connection conn, Board b) {
		//DML - insert 
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertBoard");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, b.getBoardNo());
			//Number타입 컬럼에 문자열로된 숫자를 넣어도 자동형변환이 되어 가능하다
			pstmt.setString(2, b.getCategory());
			pstmt.setString(3, b.getBoardTitle());
			pstmt.setString(4, b.getBoardContent());
			//Number타입 컬럼에 문자열로된 숫자를 넣어도 자동형변환이 되어 가능하다
			pstmt.setString(5, b.getBoardWriter());
			
			//sql 완성시킨 후 실행 및 결과받기
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	//첨부파일정보 등록 메소드
	public int insertAttachment(Connection conn, Attachment at) {
		//DML - insert
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertAttachment");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, at.getRefBno());
			pstmt.setString(2, at.getOriginName());
			pstmt.setString(3, at.getChangeName());
			pstmt.setString(4, at.getFilePath());
			
			//완성되었으니 실행 및 결과 받기
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	//첨부파일 정보조회 메소드
	public Attachment selectAttachment(Connection conn, int bno) {
		//select
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		Attachment at = null; //조회된 데이터 있으면 생성하기 위해 변수선언
		String sql = prop.getProperty("selectAttachment");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				at = new Attachment(rset.getInt("FILE_NO")
								   ,rset.getString("ORIGIN_NAME")
								   ,rset.getString("CHANGE_NAME")
								   ,rset.getString("FILE_PATH"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return at;
	}
	//게시글 수정 메소드
	public int updateBoard(Connection conn, Board b) {
		//DML - UPDATE
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("updateBoard");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, b.getBoardTitle());
			pstmt.setString(2, b.getBoardContent());
			pstmt.setString(3, b.getCategory());
			pstmt.setInt(4, b.getBoardNo());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	
	//첨부파일정보 수정메소드
	public int updateAttachment(Connection conn, Attachment at) {
		//DML - UPDATE
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("updateAttachment");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, at.getOriginName());
			pstmt.setString(2, at.getChangeName());
			pstmt.setString(3, at.getFilePath());
			pstmt.setInt(4, at.getFileNo());
			
			result = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}
	//게시글 삭제 메소드
	public int deleteBoard(Connection conn, int boardNo) {
		//DML - UPDATE 
		int result = 0; 
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("deleteBoard");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	
	
	//사진게시글 작성
	public int insertThumbnail(Connection conn, Board b) {
		//DML - insert 
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertThumbnail");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, b.getBoardNo());
			pstmt.setString(2, b.getBoardTitle());
			pstmt.setString(3, b.getBoardContent());
			pstmt.setString(4, b.getBoardWriter());
			
			result = pstmt.executeUpdate();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	
	
	//파일 목록처리 (등록메소드)
	public int insertAttachmentList(Connection conn, ArrayList<Attachment> atList,int boardNo) {
		//파일 목록에 대해서 모두 등록처리하기 
		int result = 1; //insert문을 여러번 사용(하나라도 0이면 0이되게 처리)
		
		PreparedStatement pstmt = null;
		
		String sql = prop.getProperty("insertAttachmentList");
		
		try {
			//전부 넣어야하니 향상된 for문 이용하기 
			for(Attachment at : atList) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, boardNo);
				pstmt.setString(2, at.getOriginName());
				pstmt.setString(3, at.getChangeName());
				pstmt.setString(4, at.getFilePath());
				pstmt.setInt(5, at.getFileLevel());
				
				result *= pstmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			result = 0; //처음부터 문제가 생겼다면 0으로 바꿀수있도록 처리
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}
	
	//사진게시글 목록 조회메소드
	public ArrayList<Board> selectThumbnailList(Connection conn) {
		//select 
		ResultSet rset = null;
		ArrayList<Board> list = new ArrayList<>();
		Statement stmt = null;
		
		String sql = prop.getProperty("selectThumbnailList");
				
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);
			
			while(rset.next()) {
				list.add(new Board(rset.getInt("BOARD_NO")
								  ,rset.getString("BOARD_TITLE")
								  ,rset.getInt("COUNT")
								  ,rset.getString("THUMBNAIL")));
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
	
	//사진게시글 상세보기 첨부파일 목록 조회
	public ArrayList<Attachment> selectAttachmentList(Connection conn, int boardNo) {
		//select
		ResultSet rset = null;
		ArrayList<Attachment> atList = new ArrayList<>();
		PreparedStatement pstmt = null;
		//기존에 일반게시판에서 작성했던 첨부파일 정보 조회 구문 사용하기 
		String sql =prop.getProperty("selectAttachment");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			rset = pstmt.executeQuery();
			
			//한 게시글에 여러 첨부파일이 존재할수있으니 반복처리 전부 불러오기 
			while(rset.next()) {
				atList.add(new Attachment(rset.getInt("FILE_NO")
										 ,rset.getString("ORIGIN_NAME")
										 ,rset.getString("CHANGE_NAME")
										 ,rset.getString("FILE_PATH")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return atList;
	}
	//댓글 작성
	public int insertReply(Connection conn, Reply r) {
		//DML (INSERT)
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertReply");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, r.getReplyContent());
			pstmt.setInt(2, r.getRefBno());
			pstmt.setString(3, r.getReplyWriter());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}
	
	//댓글 목록 조회
	public ArrayList<Reply> replyList(Connection conn, int refBno) {
		//SELECT 
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		ArrayList<Reply> list = new ArrayList<>(); //데이터 담아갈 저장소 준비
		
		String sql = prop.getProperty("replyList");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, refBno);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				list.add(new Reply(rset.getInt("REPLY_NO")
								  ,rset.getString("REPLY_CONTENT")
								  ,rset.getString("USER_ID")
								  ,rset.getDate("CREATE_DATE")));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
}
