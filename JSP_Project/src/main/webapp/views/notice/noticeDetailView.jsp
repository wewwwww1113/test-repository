<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.kh.notice.model.vo.Notice"%>
<%
	Notice n = (Notice)request.getAttribute("n");

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%@include file="/views/common/menubar.jsp" %>
	<div class="outer">
		<br>
		<h2 align="center">공지사항 상세보기 페이지</h2>
		<br>
		
		<table id="detail-area" align="center" border="1">
			<tr>
				<th width="70">제목</th>
				<td width="350" colspan="5"><%=n.getNoticeTitle() %></td>
			</tr>
			<tr>
				<th>작성자</th>
				<td><%=n.getNoticeWriter() %></td>
				<th>조회수</th>
				<td><%=n.getCount() %></td>
				<th>작성일</th>
				<td><%=n.getCreateDate() %></td>
			</tr>
			<tr>
				<th>내용</th>
				<td colspan="5">
					<p style="height:150px"><%=n.getNoticeContent() %></p>
				</td>
			</tr>
		</table>
		<br><br>
		
		<div align="center">
			<a href="<%=contextPath%>/list.no" class="btn btn-secondary">목록으로 이동</a>
			<!-- 수정 삭제버튼은 관리자만 사용할수있도록 조건처리 -->
			<%if(loginUser!=null && loginUser.getUserId().equals("admin")) {%>
				<a href="<%=contextPath %>/update.no?nno=<%=n.getNoticeNo() %>" class="btn btn-primary">수정하기</a>
				<a href="" class="btn btn-danger">삭제하기</a>
			<%} %>
		</div>
		<br><br>
	</div>
	
</body>
</html>