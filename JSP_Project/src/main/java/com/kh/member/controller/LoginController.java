package com.kh.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/login.me")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
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
		
		//POST 방식의 경우 전달값에 한글이 포함되어있다면 인코딩 처리를 해야한다.
		request.setCharacterEncoding("UTF-8");
		
		//전달받은 데이터는 request에 parameter 영역에 있다
		//사용자가 전달한 데이터는 input요소의 name값을 key로 이용해서 추출한다.
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		
		//별도로 value를 지정하지 않았기때문에 체크가 된 상태로 전달된다면 on이라는 데이터가 전달됨
		//아이디저장 체크시 on / 체크해제시 null 
		//만약 체크가 되어서 넘어온다면 해당 아이디값을 쿠키에 담아서 사용자가 로그아웃을 해도 
		//해당 아이디값이 입력되어 있도록 처리하기.
		/*
		 *  쿠키 
		 *  서버에 저장되지 않고 클라이언트에 저장되는 문자열 기반 저장소 
		 *  키와 벨류로 이루어진 데이터 파일 
		 *  사용자가 요청하지 않아도 브라우저가 request시 header에 추가하여 서버에 전송된다.
		 *  서버와 뷰에서 모두 접근 가능함
		 *  만료시간을 설정 가능하다
		 *  문자열만 저장가능하다.
		 * 
		 * */
		
		//쿠키 준비
		Cookie cookie = null;
		//아이디 저장여부 
		String saveId = request.getParameter("saveId");
		
		//만약 체크가 되어서 넘어왔다면 
		if(saveId != null) {
			//쿠키의 이름과 값을 넣어서 생성해주기 
			cookie = new Cookie("userId",userId);
			//쿠키의 수명을 지정 
			cookie.setMaxAge(60*60*24); //초단위 (60*60*24 하루)
			//response객체에 추가하여 응답되도록 하기 
			response.addCookie(cookie);
		}else { //체크가 되지 않았다면
			//쿠키 지워주기
			cookie = new Cookie("userId",null);
			cookie.setMaxAge(0); //수명 0으로 변경
			//응답객체에 담아주기
			response.addCookie(cookie);
			
		}
		
		
		
		
		
		
		//사용자가 입력한 데이터를 service-dao-db 에 순차적으로 전달하며 해당 요청에 대한 처리를 한다.
		//해당 요청을 처리할 service의 메소드 호출하기(데이터 전달).
		Member loginUser = new MemberService().loginMember(userId,userPwd);
		
		//사용자의 회원정보를 응답페이지에 보여줄 준비 
		/*
		 * 응답페이지에 전달할 값이 있을 경우 값을 담을 수 있는 공간 (Servlet Scope 내장 객체 4종류)
		 * 1)application : application에 담은 데이터는 웹 어플리케이션 전역에서 다 접근할 수 있다.
		 * 				   한번 담은 데이터는 직접 지우기 전까지,서버가 멈추기 전까지 유지 
		 * 
		 * 2)session : session에 담은 데이터는 모든 jsp와 servlet에서 접근할 수 있다.
		 * 			   한번 담은 데이터는 직접 지우기 전까지,서버가 멈추기 전까지,브라우저가 종료되기 전까지 접근가능
		 * 3)request : request에 담은 데이터는 해당 request를 포워딩한 응답 jsp에서만 접근가능
		 * 4)page : 해당 jsp 페이지 내에서만 접근 가능
		 * 
		 *  -주로 session과 request영역을 활용하게 됨
		 *  공통적으로 데이터를 담고자 한다면 내장객체.setAttribute("키","값");
		 *  	   데이터를 추출하고자 한다면 내장객체.getAttribute("키");
		 *  	   데이터를 지우고자 한다면 내장객체.removeAttribute("키");
		 * */
		
		//session객체를 얻어오기 (로그인 정보 담아두기 위함)
		//request객체로부터 session객체 얻어오기 
		HttpSession session = request.getSession();
		
		//로그인 실패시 
		if(loginUser==null) {//조회결과가 없을때 
			//에러페이지로 보내버리기 또는 실패알림을 띄워주며 메인으로 보내기 
			//에러페이지로 포워딩 하기 
			request.setAttribute("errorMsg", "로그인 실패");
			//응답페이지 jsp를 경로로한 Dispatcher 객체 생성하기 
			RequestDispatcher view = request.getRequestDispatcher("views/common/errorPage.jsp");
			//해당 경로로 위임하기(포워딩)
			view.forward(request, response);
			
		}else {//로그인 성공시
			//로그인 정보를 로그아웃전까지 유지한채로 사용할것이기 때문에 session에 담아주기 
			session.setAttribute("loginUser",loginUser); //로그인정보 담기
			session.setAttribute("alertMsg", "로그인 성공"); //알림메세지 담아주기
			
			//재요청 방식 (sendRedirect방식) 
			//기존 요청은 끝내고 새로운 페이지를 요청하는 방식 
			//응답객체.sendRedirect("경로");
			
			//response.sendRedirect("/jsp");
			//request.getContextPath() : 컨텍스트 루트를 반환하는 메소드
			response.sendRedirect(request.getContextPath());// /jsp
			
		}
		
		
		
	}

}
