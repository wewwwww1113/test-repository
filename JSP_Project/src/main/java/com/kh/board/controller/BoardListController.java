package com.kh.board.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Board;
import com.kh.common.model.vo.PageInfo;

/**
 * Servlet implementation class BoardListController
 */
@WebServlet("/list.bo")
public class BoardListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardListController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//페이징 처리 (사용자에게 보여줄 게시글 개수)
		//준비물 
		int listCount; // 총 게시글 개수 
		int currentPage; //현재 페이지
		int pageLimit; //페이지 하단에 보여질 페이징바에 최대 개수
		int boardLimit; //한 페이지에 보여줄 게시글 개수
		
		int maxPage; //가장 마지막 페이징바가 몇번인지 (총 페이지 개수)
		int startPage; //페이지 하단에 보여질 페이징바의 시작수
		int endPage; //페이지 하단에 보여질 페이징바의 끝수
		
		//listCount 현재 게시글 개수 - DB에서 조회해오기
		listCount = new BoardService().listCount();

		//currentPage 현재 페이지정보 
		currentPage = Integer.parseInt(request.getParameter("currentPage"));
		
		//페이지 하단에 보여질 페이징바 최대 개수
		pageLimit = 10;
		//한 페이지에서 보여줄 게시글 개수
		boardLimit = 10;
		
		/*
		 *  maxPage : 가장 마지막 페이지가 몇번인지 (총 페이지수)
		 *  
		 *  listCount와 boardLimit 에 영향을 받는 수
		 *  
		 *  게시글 개수 107개일때 한페이지에 10개씩 보여준다라고 한다면 10페이지까지는 가득채워지고 
		 *  11페이지에서는 7개의 데이터만 보여지게 된다.
		 *  총개수/한페이지개수 처리 후 나머지 소수점을 올림처리하기
		 * 
		 * 	listCount와 boardLimit은 int자료형이기때문에 소수점이 절삭된다. 이중 하나의 항에 double자료형으로 바꿔주면
		 *  소수점 표현이 되고 이때 Math.ceil() 올림 메소드를 이용하여 소수점을 올림처리 한뒤 
		 *  int 자료형으로 형변환하여 소수점처리를 없애주면 된다.
		 * 
		 * */
		
		maxPage = (int)Math.ceil((double)listCount/boardLimit);
		
		
		/*
		 * startPage : 페이징바의 시작수 
		 * pageLimit이 10인 경우 
		 * 
		 * currentPage가 1~10 일땐 1 
		 * currentPage가 11~20 일땐 11
		 * currentPage가	21~30 일땐 21 
		 * 
		 * (currentPage-1)/pageLimit * pageLimit+1
		 * */
		startPage = (currentPage-1)/pageLimit * pageLimit+1;
		
		//pageLimit == 10 
		//endPage : startPage가 1일때 end == 10  11 == 20
		//startPage+pageLimit -1 
		endPage = startPage+pageLimit-1;
		
		//107개 데이터를 각페이지에서 10개씩 출력하니 11개 페이지가 나온다.
		//마지막 페이지는 11번 페이지
		//페이징 수를 1~10까지 처리하기때문에 11번페이지로 넘어가면 11번페이지가 startPage endPage 20
		//만약에 현재 페이징바 처리의 endPage가 maxPage(총페이지수)보다 클때
		//endPage값을 maxPage값으로 변경해주기 
		if(endPage>maxPage) {
			endPage = maxPage; //maxPage값을 endPage변수에 대입해주기
		}
		
		//페이징바 처리에 필요한 변수들 VO 에 담아주기
		PageInfo pi = new PageInfo(listCount,currentPage,pageLimit,boardLimit,maxPage,startPage,endPage);
		
		//게시글 목록 
		ArrayList<Board> list = new BoardService().selectList(pi);
		
		//위임하기 위해 데이터 담아주기
		
		request.setAttribute("pi", pi);
		request.setAttribute("list", list);
		
		request.getRequestDispatcher("views/board/boardListView.jsp").forward(request, response);
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
