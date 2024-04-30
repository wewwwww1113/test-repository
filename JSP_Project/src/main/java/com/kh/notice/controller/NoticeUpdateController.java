package com.kh.notice.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.notice.model.service.NoticeService;
import com.kh.notice.model.vo.Notice;

/**
 * Servlet implementation class NoticeUpdateController
 */
@WebServlet("/update.no")
public class NoticeUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeUpdateController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int nno = Integer.parseInt(request.getParameter("nno"));
		
		//글번호를 이용하여 해당 글정보를 조회해서 수정하기 페이지로 전달 및 이동 
		
		Notice n = new NoticeService().selectNotice(nno);
		
		request.setAttribute("n", n);
		
		request.getRequestDispatcher("views/notice/noticeUpdateView.jsp")
			   .forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//수정작업하기 (글제목,글내용,글번호 필요)
		request.setCharacterEncoding("UTF-8");
		
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		int nno = Integer.parseInt(request.getParameter("nno"));
		
		Notice n = new Notice();
		n.setNoticeTitle(title);
		n.setNoticeContent(content);
		n.setNoticeNo(nno);
		
		//dml update 니까 처리된 행수 돌려받기 
		int result = new NoticeService().updateNotice(n);
		
		//updateNotice()
		//해당 글번호를 이용하여 글 수정작업후
		//성공시 수정성공 메세지와 함께 해당글 상세보기 페이지로 이동 (재요청)
		HttpSession session = request.getSession();
		
		if(result>0) {
			session.setAttribute("alertMsg", "공지사항 수정 성공");
			response.sendRedirect(request.getContextPath()+"/detail.no?nno="+nno);
		}else {
			//실패시 수정실패 메세지와 함께 해당글 상세보기 페이지로 이동 (재요청)
			session.setAttribute("alertMsg", "공지사항 수정 실패");
			response.sendRedirect(request.getContextPath()+"/detail.no?nno="+nno);
		}
		
		
		
		
	}

}
