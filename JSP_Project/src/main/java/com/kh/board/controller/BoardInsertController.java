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
 * Servlet implementation class BoardInsertController
 */
@WebServlet("/insert.bo")
public class BoardInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardInsertController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//카테고리 정보 불러와서 글작성페이지에 전달하기 
		//메소드명은 selectCategoryList 
		ArrayList<Category> cList = new BoardService().selectCategoryList();
		
		//카테고리 목록정보를 등록페이지에 전달하기
		request.setAttribute("cList", cList);
		
		
		//글 등록 페이지로 이동
		request.getRequestDispatcher("views/board/boardInsertView.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
//		request.setCharacterEncoding("UTF-8");
		
		//카테고리 번호지만 VO에 String으로 정의했기때문에 문자열로 받아주기 
		/*
		String category = request.getParameter("category");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String uploadFile = request.getParameter("uploadFile");
		
		System.out.println(category);
		System.out.println(title);
		System.out.println(content);
		System.out.println(uploadFile);
		*/
		
		//form에서 multipart 형식으로 전송을 하면 일반 request요청객체에서 데이터를 전달받을수 없다.
		//multipart형식으로 변경하여 전달받아야한다.
		
		//지금 요청이 multipart 형식인지 판별하여 작업하기 
		//ServletFileUpload.isMultipartContent(request) : 요청객체가 multipart형식인지 판별 메소드 맞으면 true 아니면 false
		if(ServletFileUpload.isMultipartContent(request)) {
			//판별되었으니 multipartRequest 객체 이용하여  작업하기 
			//파일 업로드를 위한 라이브러리 cos.jar
			
			//1.전송되는 파일을 처리할 작업 내용(전송되는 파일의 용량제한,전달된 파일을 저장할 경로)
			//1-1. 전송 파일 용량 제한 (byte)
			//byte-kbyte-mbyte-gbyte-tbyte 
			//1kbyte == 1024byte
			int maxSize = 10 * 1024 * 1024; //10mb 
			
			//1_2. 전달될 파일을 저장할 서버의 경로를 알아내기 
			//세션 객체에서 애플리케이션 객체 얻어오고 해당객체에 있는 getRealPath() 메소드를 이용하여 경로 알아내기
			//request.getSession().getServletContext() : 웹 애플리케이션 객체
			//현재 구동되고있는 웹애플리케이션을 기준으로 경로를 잡아줘야한다.
			//request.getSession().getServletContext().getRealPath("/");
			//System.out.println(request.getSession().getServletContext().getRealPath("/resources/uploadFiles/"));
			
			String savePath = request.getSession().getServletContext().getRealPath("/resources/uploadFiles/");
			
			//1_3. 기존 request객체로는 파일전달을 받을 수 없으니 MultipartRequest객체로 변환하는 작업을 수행한다.
			//[표현법]MultipartRequest multiRequest = new MultipartRequest(request,저장경로,파일최대사이즈,인코딩,파일명객체);
			//서버에 업로드되는 파일명이 같다면 덮어쓰기가 되기때문에 파일명을 중복되지 않도록 작업해야한다.
			//사용자는 알수없으니 서버에서 직접 파일명 변경 처리해주기
			// new DefaultFileRenamePolicy() : cos.jar에서 제공하는 기본 파일명 변경객체 (파일명에 숫자를 부여해준다)
			MultipartRequest multiRequest = new MultipartRequest(request, savePath,maxSize,"UTF-8",new MyFileRenamePolicy());
		
			//2.DB에 저장할 데이터 추출하기 
			//게시글 정보는 Board에 첨부파일에 대한 정보는 Attachment에 담아서 전달하기 
			//기존 request객체에서는 전달데이터 추출 불가 
			//mulitRequest에서 추출해야한다. 
			String category = multiRequest.getParameter("category");
			String title = multiRequest.getParameter("title");
			String content = multiRequest.getParameter("content");
			String boardWriter = multiRequest.getParameter("userNo");
			
			Board b = new Board();
			b.setCategory(category);
			b.setBoardContent(content);
			b.setBoardTitle(title);
			b.setBoardWriter(boardWriter);
			//게시글 정보는 Board 테이블에 insert 
			//첨부파일 정보는 Attachment 테이블에 insert
			
			//첨부파일이 없을경우에 대비하여 있을때 생성해서 전달하기 
			Attachment at = null;
			//첨부파일이 있는지 없는지 판별하기 
			//multiRequest.getOriginalFileName("키값(name)") : 있으면 원본파일명 없으면 null을 반환하는 메소드 
//			System.out.println(multiRequest.getOriginalFileName("uploadFile"));
			
			if(multiRequest.getOriginalFileName("uploadFile") != null) {
				//첨부파일이 있는경우 
				
				at = new Attachment();
				at.setOriginName(multiRequest.getOriginalFileName("uploadFile")); //원본파일명
				at.setChangeName(multiRequest.getFilesystemName("uploadFile")); //서버에 업로드되어있는 파일명
				at.setFilePath("/resources/uploadFiles/");
			}
			
			//게시글정보와 첨부파일 정보를 담았으니 서비스 요청하기 (게시글정보와 첨부파일정보 전달)
			int result = new BoardService().insertBoard(b,at);
			
			HttpSession session = request.getSession();
			//결과값에 따라서 응답뷰 지정하기 
			if(result>0) { //성공
				//세션에 게시글 등록성공 메세지 담고 
				//게시판 목록으로 이동시키기 
				session.setAttribute("alertMsg", "게시글 등록 성공");
				response.sendRedirect(request.getContextPath()+"/list.bo?currentPage=1");
			}else { //실패
				//세션에 게시글 등록실패 메세지 담고 
				//게시판 목록으로 이동시키기
				//만약 첨부파일이 있다면 해당 파일 서버에서 삭제하기 
				if(at!=null) {
					//삭제하고자하는 파일 경로로 파일객체연결한뒤 삭제메소드 실행
					new File(savePath+at.getChangeName()).delete();
				}
				session.setAttribute("alertMsg", "게시글 등록 실패");
				response.sendRedirect(request.getContextPath()+"/list.bo?currentPage=1");
				
			}
		}
	}

	
	
	
	
	
}
