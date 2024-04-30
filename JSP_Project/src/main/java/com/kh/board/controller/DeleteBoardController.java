package com.kh.board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.board.model.service.BoardService;

/**
 * Servlet implementation class DeleteBoardController
 */
@WebServlet("/delete.bo")
public class DeleteBoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteBoardController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int boardNo = Integer.parseInt(request.getParameter("bno"));
		
		int result = new BoardService().deleteBoard(boardNo);
		
		HttpSession session = request.getSession();
		if(result>0) {//성공시 글목록페이지로 이동
			session.setAttribute("alertMsg", "게시글 삭제 성공");
			response.sendRedirect(request.getContextPath()+"/list.bo?currentPage=1");
		}else {
			session.setAttribute("alertMsg", "게시글 삭제 실패");
			//실패시 게시글 상세보기 페이지로 이동
			response.sendRedirect(request.getContextPath()+"/detail.bo?bno="+boardNo);
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
