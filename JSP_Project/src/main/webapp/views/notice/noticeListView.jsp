<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.ArrayList,com.kh.notice.model.vo.Notice"%>
    
<%
	ArrayList<Notice> list = (ArrayList<Notice>)request.getAttribute("noticeList");

%>   
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<style>
	.list-area{
		border : 1px solid white;
		text-align : center;
	}
	.list-area>tbody>tr:hover{
		background : gray;
		cursor : pointer;
	}
</style>

</head>
<body>
	
<%-- 	<%@ include file="../common/menubar.jsp" %> --%>
	<%@ include file="/views/common/menubar.jsp" %>
	
	<div class="outer">
	
	
		<br>
		<h1 align="center">공지사항</h1>
		<br>
		
		<!-- 공지사항은 관리자만 작성할 수 있음 
			-조건처리를 통해서 글 작성버튼을 관리자일때만 보일수있도록 작업하기 
		 -->
		 <!-- 로그인이 되어있고 로그인된 사용자의 아이디가 admin과 일치할때 (관리자일때) -->		 
<%-- 		  <%if(loginUser.getUserId().equals("admin")&&(loginUser != null)){ %> --%>
		 <%if((loginUser != null) && loginUser.getUserId().equals("admin")){ %>
			 <div align="center">
	<!-- 		 	<button onclick="location.href='oooo'">글작성</button> -->
			 	<a href="<%=contextPath%>/insert.no" class="btn btn-info">글작성</a>
			 
			 </div>
		 <%} %>
		 
		 <br>
		
		
		<table border="1" align="center" class="list-area">
			<thead>
				<tr>
					<th>글번호</th>
					<th width="400">글제목</th>
					<th width="100">작성자</th>
					<th>조회수</th>
					<th width="100">작성일</th>
				</tr>
			</thead>
			<tbody>
	<!-- 			<tr> -->
	<!-- 				<td>1</td> -->
	<!-- 				<td>오늘부터 해당 사이트는 접속불가입니다.</td> -->
	<!-- 				<td>관리자</td> -->
	<!-- 				<td>10</td> -->
	<!-- 				<td>2020/10/10</td> -->
	<!-- 			</tr> -->
			
			<%if(list.isEmpty()){ %> <!-- 리스트가 비어있다면  -->
				<tr>
					<td>조회된 공지사항이 없습니다.</td>
				</tr>
			<%}else{ %>
				<!-- 게시글이 있다면 반복해서 전부 뽑아주기 -->
				<%for(Notice n : list) {%>
					<tr>
						<td><%=n.getNoticeNo() %></td>
						<td><%=n.getNoticeTitle() %></td>
						<td><%=n.getNoticeWriter() %></td>
						<td><%=n.getCount() %></td>
						<td><%=n.getCreateDate() %></td>
					</tr>
				<%} %>
			<%} %>
			</tbody>
		</table>
		<br><br>
		
		<script>
			//글을 클릭했을때 해당 글번호를 console.log로 출력해보세요
			
			$(".list-area>tbody>tr").click(function(){
					
				//console.log($(this).children().eq(0).text());
				var nno = $(this).children().eq(0).text();
				
				//상세보기 요청시 추출한 글번호를 전달하며 요청하기 
				//get방식 요청에선 url에 요청매핑 뒤 ?를 기준으로 key=value 형태로 값이 전달된다.
				// 요청 경로 : /jsp/detail.no?nno=글번호 
				// 매핑주소 뒤 물음표를 기준으로 뒤에 내용들은 쿼리스트링이라고 한다 
				//표현되는 방식은 ?키=값&키=값&.... 이며 요청을 받은 servlet에서 parameter영역으로 접근할 수 있다.
				location.href='<%=contextPath%>/detail.no?nno='+nno;
					
			});
			
		
		
		</script>
		
		
		
		
		
		
	</div>
	
</body>
</html>