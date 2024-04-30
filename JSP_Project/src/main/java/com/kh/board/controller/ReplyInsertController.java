package com.kh.board.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Reply;

/**
 * Servlet implementation class ReplyInsertController
 */
@WebServlet("/insertReply.bo")
public class ReplyInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReplyInsertController() {
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
	
//		request.setCharacterEncoding("UTF-8");
		String userNo = request.getParameter("userNo");
		int bno = Integer.parseInt(request.getParameter("bno"));
		String content = request.getParameter("content");
		
		Reply r = new Reply();
		r.setRefBno(bno);
		r.setReplyWriter(userNo); //조회할땐 작성자 아이디로 조회하기 위해
		r.setReplyContent(content);
		
		//DML(INSERT)
		int result = new BoardService().insertReply(r);
		
		//응답데이터 반환하기
		response.getWriter().print(result);
		//메소드는 insertReply
		//dml 실행 결과에 따라서 응답화면은 view에서 지정하기 
		//view페이지 success 에서 alert 메세지로 성공시 댓글 작성성공  
		//추가후 작성란 비워두기 
		//실패시 alert 메세지로 작성실패 띄워주고 작성란 비워주기
		
		//댓글작성 다 끝나면 댓글 목록 조회 하기
		//테이블에 입력한 데이터 형식에 맞춰서 목록 조회하여 추가하기 
		//컨트롤러 : ReplyListController
		//메소드,함수명 : replyList() 
		
		
		
		
		
	}

}
