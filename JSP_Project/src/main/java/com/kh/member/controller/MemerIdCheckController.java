package com.kh.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.member.model.service.MemberService;

/**
 * Servlet implementation class MemerIdCheckController
 */
@WebServlet("/idCheck.me")
public class MemerIdCheckController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemerIdCheckController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String inputId = request.getParameter("inputId");
		
		//서비스에 요청하기 (flag가 true인 경우는? 아이디 중복 (사용불가) false인 경우가 사용 가능
		Boolean flag = new MemberService().checkId(inputId);
		
		String responseStr = "";
		//NNNNN / NNNNY
		
		if(flag) {
			responseStr ="NNNNN";
		}else {
			responseStr ="NNNNY";
		}
		
		
		response.getWriter().print(responseStr);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
