<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.list-area{
		width: 760px;
		margin : auto;
	}
	.thumbnail{
		border : 1px solid white;
		width : 220px;
		display : inline-block;
		margin : 14px;
	}
	
	.thumbnail:hover {
		cursor : pointer;
		opacity : 0.7;
	}
	

</style>


</head>
<body>
	<%@ include file="/views/common/menubar.jsp" %>
	<div class="outer">
		<h2 align="center">사진 게시판</h2>
		<br>
		
		
		<c:if test="${not empty loginUser }">
			<div align="center">
				<button class="btn btn-info" onclick="location.href='${contextPath}/insert.th'">글작성</button>
			</div>
		</c:if>
		
		<!-- 글 클릭했을때 상세보기 페이지 작성하기 
			jsp : thumbnailDetailView.jsp 
			controller : ThumbnailDetailController 
			method : thumbnailDetail();
			참고 페이지 : 사진게시글 작성페이지 
		 -->
		
		<div class="list-area">
			<!-- 반복문으로 썸네일뽑아주기 -->
			<c:forEach var="th" items="${list}">
				<div class="thumbnail" align="center" onclick="location.href='${contextPath}/detail.th?bno=${th.boardNo}'">
					<img src="${contextPath }${th.thumbnailImg}" width="200px" height="150px">
					<p>
						No.${th.boardNo } ${th.boardTitle }<br>
						조회수 : ${th.count }
					</p>
				</div>
			</c:forEach>
			
		</div>
	
	</div>
	
</body>
</html>