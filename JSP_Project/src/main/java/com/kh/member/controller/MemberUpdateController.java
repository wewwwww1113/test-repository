package com.kh.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;

/**
 * Servlet implementation class MemberUpdateController
 */
@WebServlet("/update.me")
public class MemberUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberUpdateController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//전달받은 데이터로 해당 사용자의 회원정보를 변경하는 작업을 하시오
		//한글이 포함되니 인코딩 처리하기 
		request.setCharacterEncoding("UTF-8");
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		String[] interest = request.getParameterValues("interest");
		
		//배열 문자열로 합치기
		String interestStr = "";
		if(interest != null) {
			interestStr = String.join(",",interest);
		}
		
		//회원을 특정할수있도록 식별자 필요 
		//전달받은 데이터를 객체에 담아서 service에 전달 및 요청하기
		Member m = new Member(userId,userName,phone,email,address,interestStr);
		
		/* 
		 * -로그인 정보 갱신 처리 컨트롤러부터 진행 
		//서비스에 요청하기 
		int result = new MemberService().updateMember(m);
		
		//메소드명은 updateMember
		//정보수정 실패시 - 에러페이지로 정보수정실패 메세지와 함께 이동(위임)
		if(result==0) {
			request.setAttribute("errorMsg", "정보수정실패");
			RequestDispatcher view = request.getRequestDispatcher("views/common/errorPage.jsp");
			//포워딩하기 (위임)
			view.forward(request, response);
		}else {
			//정보수정 성공시 - 마이페이지로 재요청 
			//정보수정된 회원정보를 로그인정보에 갱신시켜주기 위해 재조회해오기 
			HttpSession session = request.getSession();
			Member loginUser = (Member)session.getAttribute("loginUser");
			//로그인회원의 아이디와 비밀번호로 로그인 메소드 사용해서 재조회
			Member updateMem = new MemberService().loginMember(userId,loginUser.getUserPwd());
			//조회해온 갱신데이터 session에 갱신시켜주기
			session.setAttribute("alertMsg", "정보수정성공");
			session.setAttribute("loginUser",updateMem);
			//마이페이지 요청매핑을 이용해서 페이지 재요청하기
			response.sendRedirect(request.getContextPath()+"/myPage.me");
			
		}
		*/
		
		//서비스에 요청하기 (갱신처리 된 객체 받아오기)
		Member updateMem = new MemberService().updateMember(m);
		
		//메소드명은 updateMember
		//정보수정 실패시 - 에러페이지로 정보수정실패 메세지와 함께 이동(위임)
		if(updateMem == null) {
			request.setAttribute("errorMsg", "정보수정실패");
			RequestDispatcher view = request.getRequestDispatcher("views/common/errorPage.jsp");
			//포워딩하기 (위임)
			view.forward(request, response);
		}else {
			//정보수정 성공시 - 마이페이지로 재요청 
			//정보수정된 회원정보를 로그인정보에 갱신시켜주기 위해 재조회해오기 
			HttpSession session = request.getSession();
			
			//조회해온 갱신데이터 session에 갱신시켜주기
			session.setAttribute("alertMsg", "정보수정성공");
			session.setAttribute("loginUser",updateMem);
			//마이페이지 요청매핑을 이용해서 페이지 재요청하기
			response.sendRedirect(request.getContextPath()+"/myPage.me");
			
		}
	}

}
