package com.kh.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Category;
import com.kh.common.MyFileRenamePolicy;
import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class BoardUpdateController
 */
@WebServlet("/update.bo")
public class BoardUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardUpdateController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int bno = Integer.parseInt(request.getParameter("bno"));
		
		BoardService bs = new BoardService();
		
		//수정페이지로 전달하기 
		//게시글 정보
		Board b = bs.selectBoard(bno);
		//카테고리 목록
		ArrayList<Category> cList = bs.selectCategoryList();
		//첨부파일 정보
		Attachment at = bs.selectAttachment(bno);
	
		//수정페이지에 위에 데이터들 담아서 전달하기 
		request.setAttribute("b", b);
		request.setAttribute("cList", cList);
		request.setAttribute("at", at);
		
		request.getRequestDispatcher("views/board/boardUpdateView.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8"); 
		
		//multipart 형식으로 전달되었는지 판별
		if(ServletFileUpload.isMultipartContent(request)) {
			
			//multipartRequest 생성해서 제목 카테고리 글내용 출력해보기
			
			//최대용량 크기 지정
			int maxSize = 10 * 1024 * 1024;
			
			//파일 저장 경로 찾기 
			String savePath = request.getSession().getServletContext().getRealPath("/resources/uploadFiles/");
			
			//request를 MultipartRequest로 변환 (파일업로드)
			MultipartRequest multiRequest 
				= new MultipartRequest(request, savePath,maxSize,"UTF-8",new MyFileRenamePolicy());
			
			//게시글 정보 추출하기 
			int boardNo = Integer.parseInt(multiRequest.getParameter("boardNo"));
			String title = multiRequest.getParameter("title");
			String content = multiRequest.getParameter("content");
			String category = multiRequest.getParameter("category");
			
			//첨부파일이 변경되던지 안되던지 상관없이 게시글 정보 수정은 이루어져야한다.
			
			Board b = new Board();
			b.setBoardNo(boardNo);
			b.setBoardTitle(title);
			b.setBoardContent(content);
			b.setCategory(category);
			
			//새로운 첨부파일이 전달되었다면 
			Attachment at = null; 
			//서버에 전달된 파일의 이름이 있을때 (새로운 파일이 업로드되었을때)
			if(multiRequest.getOriginalFileName("reUploadFile")!=null) {
				
				at = new Attachment(); //새파일정보 담을 객체 준비 
				//getOriginalFileName : 사용자가 올린 이름 
				at.setOriginName(multiRequest.getOriginalFileName("reUploadFile"));
				//getFilesystemName : 서버에 업로드된 이름(저장되어있는)
				at.setChangeName(multiRequest.getFilesystemName("reUploadFile"));
				at.setFilePath("/resources/uploadFiles/");
				
				//해당 게시글에 기존에도 첨부파일이 있었던경우(넘겨준 기존파일정보가 있는지 없는지 판별하기)
				if(multiRequest.getParameter("originFileNo")!= null) {
					//기존에도 첨부파일이 있었고 새로운 첨부파일도 있는경우 
					//식별자 용도인 fileNo 넣어주기 
					//Update Attachment 
					at.setFileNo(Integer.parseInt(multiRequest.getParameter("originFileNo")));
					
				}else { //기존에는 첨부파일이 없었고 새로운 첨부파일이 있는 경우 
					//insert Attachment
					at.setRefBno(boardNo); //게시글에 새로운 첨부파일 정보가 들어가야하니 참조번호 넣어주기
				}
				 
			}//해당 게시글에는 기존에 첨부파일이 없었을 경우
			
			//서비스에 요청
			int result = new BoardService().updateBoard(b,at);
			
			//새로운 첨부파일이 없는 경우 : Board - Update
			//새로운 첨부파일 있고 기존 첨부파일 있는경우 : Board - Update / Attachment - Update
			//새로운 첨부파일 있고 기존 첨부파일 없는경우 : Board - Update / Attachment - Insert
			
			HttpSession session = request.getSession();
			//결과에 따라 응답뷰 지정
			if(result>0) {//성공시
				session.setAttribute("alertMsg", "게시글 수정완료");
				response.sendRedirect(request.getContextPath()+"/detail.bo?bno="+boardNo);
				//기존 첨부파일 있었으면 서버에서 파일삭제하기
				if(at!=null && at.getFileNo()!=0) {
					//기존파일 삭제(파일경로+원본파일(업로드된이름)).delete(); 
					new File(savePath+multiRequest.getParameter("originFileName")).delete();
				}
				
			}else {
				session.setAttribute("alertMsg", "게시글 수정실패");
				response.sendRedirect(request.getContextPath()+"/detail.bo?bno="+boardNo);
			}
			
			
		}
	}

	
	
	
	
	
	
	
	
	
	
}
