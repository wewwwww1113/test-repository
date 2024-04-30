<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	
	#detail-form input,#detail-form textarea{
		width :100%;
		box-sizing : border-box;
	}
	#detail-form td>img{
		width : 100%;
		height : 100%;
	}

</style>


</head>
<body>
	<%@ include file="/views/common/menubar.jsp" %>
	<div class="outer">
		<br>
		<h2 align="center">사진 게시글 상세보기</h2>
		<br>
		
		<form action="${contextPath }" method="post" enctype="multipart/form-data" id="detail-form">
			<table align="center" border="1">
				<tr>
					<th width="100">제목</th>
					<td colspan="3">
						${b.boardTitle }
					</td>
				</tr>
				<tr>
					<th>내용</th>
					<td colspan="3">
						<p>${b.boardContent }</p>
					</td>
				</tr>
				
				<!-- 조회해온 첨부파일 리스트에서 대표이미지와 상세이미지 나눠서 보여주기 -->
				<c:forEach var="at" items="${atList }" varStatus="vs">
					<c:choose>
						<c:when test="${vs.index eq 0 }">
							<tr>
								<th>대표이미지</th>
								<td colspan="3" algin="center"  width="250" height="170">
									<img id="titleImg" src="${contextPath }${at.filePath}${at.changeName}">
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<th>상세이미지</th>
								<td width="150" height="120">
									<img id="contentImg${vs.index }" src="${contextPath }${at.filePath}${at.changeName}">
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				
				
			</table>
			<br><br>
			<script>
				$(function(){
					console.log("${b.boardTitle}");
				})
			
			</script>
			
			<div align="center">
				<button type="submit">글수정</button>
			</div>
		</form>
	
	</div>
</body>
</html>