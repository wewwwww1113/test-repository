package com.kh.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MypageController
 */
@WebServlet("/myPage.me")
public class MypageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MypageController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//마이페이지는 로그인정보가 있어야만 접근 가능한 페이지이다.
		//url에 매핑주소를 그대로 입력해도 접근이 가능하기 때문에 
		//만약 로그인정보가 없다면 접속할 수 없도록 처리하기.
		/*
		HttpSession session = request.getSession();
		
		//만약 loginUser 라는 키값이 null이라면 (로그인 되어있지 않다) 
		
		//로그인이 되어있지 않다면
		if(session.getAttribute("loginUser")== null) {
			//잘못된 접근이기 때문에 기존 페이지를 보여주면 안된다.
			session.setAttribute("alertMsg", "로그인 후 이용해주세요.");
			//메인페이지로 재요청보내기
			response.sendRedirect(request.getContextPath());
		}else {
			//마이페이지로 이동 처리 
			//요청흐름을 유지한 채로 마이페이지를 띄워야한다.
			//마이페이지로 포워딩
			request.getRequestDispatcher("views/member/myPage.jsp").forward(request, response);
		}
		*/
		request.getRequestDispatcher("views/member/myPage.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
