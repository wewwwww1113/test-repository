package com.kh.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LoginFilter
 */
//만약 @WebFilter 어노테이션에서 여러 경로를 매핑하고 싶다면 {} 객체 형태로 각 매핑주소를 , 로 구분하여 작성하면 된다.
//@WebFilter({"/myPage.me","/insert.bo","/update.bo","/delete.bo"})
public class LoginFilter extends HttpFilter implements Filter {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpFilter#HttpFilter()
     */
    public LoginFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//HttpServletRequest로 다운캐스팅 후 진행해야한다. 
		HttpServletRequest htRequest = (HttpServletRequest)request;
		
		HttpSession session = htRequest.getSession();
		
		//만약 loginUser 라는 키값이 null이라면 (로그인 되어있지 않다) 
		
		//로그인이 되어있지 않다면
		if(session.getAttribute("loginUser")== null) {
			//잘못된 접근이기 때문에 기존 페이지를 보여주면 안된다.
			session.setAttribute("alertMsg", "로그인 후 이용해주세요.");
			//메인페이지로 재요청보내기 
			//response도 HttpServletResponse로 다운캐스팅하기
			((HttpServletResponse)response).sendRedirect(htRequest.getContextPath());
		}else {
			//요청흐름을 유지시키기
			chain.doFilter(request, response);
		}

		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
