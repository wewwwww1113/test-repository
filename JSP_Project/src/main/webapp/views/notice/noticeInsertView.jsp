<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	
	#enroll-form input,#enroll-form textarea{
		width:100%; /*가로길이 부모요소 100%로 */
		box-sizing : border-box; /*content 영역을 기준으로 100%로 설정된것을 테두리까지 포함해서 100%로*/
	}

</style>
</head>
<body>
	<%@ include file="/views/common/menubar.jsp" %>
	
	<div class="outer">
	<br>
		<h2 align="center">공지사항 작성</h2>
		
		<form action="<%=contextPath%>/insert.no" method="post" id="enroll-form">
			
			<!-- 작성자 회원번호 서버에 전달하는 방법 1  -->
			<input type="hidden" name="userNo" value="<%=loginUser.getUserNo() %>">
			
			<table align="center" border="1">
				<tr>
					<td width="50">제목</td>
					<td width="350"><input type="text" name="title" required> </td>
				</tr>
				<tr>
					<td>내용</td>
					<td></td>
				</tr>
				<tr>
					<td colspan="2">
						<textarea name="content" rows="10" style="resize:none" required></textarea>
					</td>
				</tr>
			</table>
			<br><br>
			<div align="center">
				<button type="submit">등록</button>
				<button type="button" onclick="history.back();">뒤로가기</button>
				<!-- history.back(); :이전페이지로 이동시키는 함수 -->
			</div>
		</form>
		 <br><br>
	</div>
	
	
	
	
	
</body>
</html>