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
 * Servlet implementation class NoticeDetailController
 */
@WebServlet("/detail.no")
public class NoticeDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeDetailController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int nno = Integer.parseInt(request.getParameter("nno"));

		//조회수 증가작업
		//추출한 글번호로 조회수를 증가시켜보자.
		//update구문사용 : 현재 조회수에 +1 하기
		//메소드명 increaseCount(); 
		int result = new NoticeService().increaseCount(nno);
		
		if(result>0) {
			//조회수 증가 성공 실패에 따라 응답뷰 지정할것
			Notice n = new NoticeService().selectNotice(nno);
			
			//조회된 공지글 정보를 담아주고 포워딩처리 
			request.setAttribute("n", n);
			request.getRequestDispatcher("views/notice/noticeDetailView.jsp")
				   .forward(request, response);
		}else {//조회수 증가실패 
			
			//세션에 알림메세지 담고 글목록페이지로 재요청하기 
			HttpSession session = request.getSession();
			session.setAttribute("alertMsg", "공지글 상세보기 실패");
			response.sendRedirect(request.getContextPath()+"/list.no");
			
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
