package com.kh.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.service.MemberService;

/**
 * Servlet implementation class PwdUpdateController
 */
@WebServlet("/updatePwd.me")
public class PwdUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PwdUpdateController() {
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
		String updatePwd = request.getParameter("updatePwd");
		
//		HashMap<String,String> map = new HashMap<>();
//		
//		map.put("userId", userId);
//		map.put("userPwd", userPwd);
//		map.put("updatePwd", updatePwd);
		
		int result = new MemberService().updatePwd(userId,userPwd,updatePwd);
		
		HttpSession session = request.getSession();
		//결과에따라서 사용자에게 응답뷰 지정하기 
		if(result>0) {//성공시 비밀번호가 변경되었으니 재로그인을 하라는 안내와함께 로그아웃시키기
			session.setAttribute("alertMsg", "비밀번호가 변경되었습니다. 다시 로그인해주세요");
			session.removeAttribute("loginUser"); //로그인정보 지우기
			
			//메인페이지로 보내기
			response.sendRedirect(request.getContextPath());
			
		}else {//실패
			session.setAttribute("alertMsg", "비밀번호 변경실패");
			
			//마이페이지로 보내기
			response.sendRedirect(request.getContextPath()+"/myPage.me");
			
		}
		
	
	}

}
