package com.kh.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;

/**
 * Servlet implementation class MemberInsertController
 */
@WebServlet("/insert.me")
public class MemberInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberInsertController() {
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
		
		request.setCharacterEncoding("UTF-8");
		
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		String userName = request.getParameter("userName");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		String[] interest = request.getParameterValues("interest");
		
//		System.out.println(userId);
//		System.out.println(userPwd);
//		System.out.println(userName);
//		System.out.println(phone);
//		System.out.println(email);
//		System.out.println(address);
//		System.out.println(Arrays.toString(interest));
		
		//배열에 대한 처리하기 (문자열로 바꾸기) 
		String interestStr = "";
		
		if(interest!=null) {
			//String.join(구분자,배열); : 배열의 각요소를 구분자로 구분하여 문자열로 합쳐주는 메소드
			interestStr = String.join(",", interest);
		}
		
		//요청받은 데이터를 서비스에 전달하기위해 객체에 담아주기
		//매개변수 생성자를 이용해서 Member객체 생성하기
		Member m = new Member(userId,userPwd,userName,phone,email,address,interestStr);
		
		//서비스에 전달하며 회원가입 요청하여 반환값 받기 
		int result = new MemberService().insertMember(m);
		
		//성공 실패 여부에 따라서 사용자에게 보여줄 화면(응답뷰) 선택하기
		
		if(result>0) {//성공시 화면 
			//성공 메세지와 함께 메인페이지로 재요청 
			//메인페이지에서 알림메세지를 확인해야하니 session영역에 담아주기 
			HttpSession session = request.getSession();
			session.setAttribute("alertMsg", "회원가입에 성공했습니다.");
			
			//메인페이지로 재요청( /jsp ) 
			response.sendRedirect(request.getContextPath());
			
		}else { //실패시 화면 
			//실패 메세지와 함께 에러페이지로 포워딩(위임)
			request.setAttribute("errorMsg", "회원가입 실패");
			//에러페이지 경로를 넣어 dispatcher 객체 생성후 위임하기
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
			
		}
		
		
		
	}

}
