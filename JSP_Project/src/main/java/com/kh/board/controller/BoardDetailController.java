package com.kh.board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;

/**
 * Servlet implementation class BoardDetailController
 */
@WebServlet("/detail.bo")
public class BoardDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardDetailController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//게시글 번호 추출
		int bno = Integer.parseInt(request.getParameter("bno"));
		
		//해당 게시글의 조회수 증가 메소드 작성
		int result = new BoardService().increaseCount(bno);
		
		//Board b = null; //게시글 상세정보 담을 객체변수 준비
		
		//조회수 증가 결과에 따라 상세조회 결과 페이지 지정하기 
		if(result>0) { //조회수 증가가 성공이라면 
			
			Board b = new BoardService().selectBoard(bno);
			//첨부파일정보도 조회하기 
			Attachment at = new BoardService().selectAttachment(bno);
			
			//데이터 전달해주기
			request.setAttribute("b", b);//게시글정보
			request.setAttribute("at", at);//첨부파일정보
			
			request.getRequestDispatcher("views/board/boardDetailView.jsp").forward(request, response);
			
			
			
		}else {
			//조회 실패시 목록으로 돌려주고 알림메세지로 조회 실패 알려주기 
			request.getSession().setAttribute("alertMsg", "조회실패");
			response.sendRedirect(request.getHeader("referer"));//이전주소 반환
		}
		
		
		
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
