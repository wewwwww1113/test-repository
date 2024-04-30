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
 * Servlet implementation class NoticeInsertController
 */
@WebServlet("/insert.no")
public class NoticeInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeInsertController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		request.getRequestDispatcher("views/notice/noticeInsertView.jsp").forward(request, response);
	}

	/**@
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//인코딩
//		request.setCharacterEncoding("UTF-8");
		//글 제목
		String title = request.getParameter("title");
		//글 내용 
		String content = request.getParameter("content");
		
		//작성자 회원번호 추출하기
		//1.form태그내에 input hidden 으로 전달한 데이터 추출
		String noticeWriter = request.getParameter("userNo");
		//2.세션에있는 로그인정보에서 꺼내기 
		HttpSession session = request.getSession();
//		Member loginUser = (Member)session.getAttribute("loginUser");
//		//로그인유저 정보에서 번호만 추출하기 
//		int userNo = loginUser.getUserNo();
//		
//		String noticeWriter = String.valueOf(userNo);
//		
//		String no = String.valueOf(((Member)session.getAttribute("loginUser")).getUserNo());
		
		//추출한 정보 vo에 담아서 전달하기 
		Notice n = new Notice();
		n.setNoticeTitle(title);
		n.setNoticeContent(content);
		n.setNoticeWriter(noticeWriter);
		
		//서비스에 전달하기 
		int result = new NoticeService().insertNotice(n);
		
		
		//결과값에 따라 응답뷰 지정하기 
		if(result>0) { //성공
			//알림메세지와 함께 글목록 페이지 이동
			session.setAttribute("alertMsg", "공지글 작성 성공");
			
			//글목록페이지로 재요청
			response.sendRedirect(request.getContextPath()+"/list.no");
		}else {//실패
			//에러페이지로 포워딩 
			
			request.setAttribute("errorMsg", "공지글 작성 실패");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
			
		}
		
		
	}
	
}
