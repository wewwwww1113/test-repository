package com.kh.board.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;

/**
 * Servlet implementation class ThumbnailDetailController
 */
@WebServlet("/detail.th")
public class ThumbnailDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ThumbnailDetailController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		int boardNo = Integer.parseInt(request.getParameter("bno"));
		
		//상세보기
		BoardService bs = new BoardService();
		
		//조회수 증가시키기 
		int result = bs.increaseCount(boardNo);
		
		//조회수 증가처리가 성공적으로 되었다면 조회하기 
		if(result>0) {
			
			Board b = bs.selectBoard(boardNo);
			
			ArrayList<Attachment> atList = bs.selectAttachmentList(boardNo);
			request.setAttribute("b", b);
			request.setAttribute("atList", atList);
			
			request.getRequestDispatcher("views/board/thumbnailDetailView.jsp").forward(request, response);
		}else {
			request.getSession().setAttribute("alertMsg", "조회실패 관리자에게 문의하세요");
			response.sendRedirect(request.getContextPath()+"/list.th");
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
