package com.kh.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogoutController
 */
@WebServlet("/logout.me")
public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//로그아웃 처리하기 - a태그 요청은 url요청이기때문에 get방식요청이다 
		//로그아웃 처리하는 방법 2가지 
		//1번 세션자체 만료시키기
		HttpSession session = request.getSession();
//		session.invalidate(); // 세션 만료시키는 메소드(세션초기화)
		//request.getSession().invalidate(); //한줄처리 
		
		//2번 해당 키를 이용해서 로그인 정보만 지워주기 
		session.removeAttribute("loginUser"); //로그인정보 지우기
		
		
		//로그아웃처리 후 원하는 페이지로 재요청 보내기(redirect) 
		//메인페이지로 보내기 - /jsp 직접작성하지 않고 contextPath 사용하기 
		response.sendRedirect(request.getContextPath());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
