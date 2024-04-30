<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.list-area>tbody tr:hover{
		background: gray;
		cursor : pointer;
	}

</style>

</head>
<body>
	<%@include file ="/views/common/menubar.jsp" %>
	
	<div class="outer">
		<br>
		
		<h2 align="center">게시판</h2>
		<br><br>
		<!-- 글작성버튼은 로그인한 회원만 볼수있도록 조건처리 -->
		<c:if test="${not empty loginUser }">
			<div align="center">
				<a href="${contextPath }/insert.bo" class="btn btn-info">글작성</a>
			</div>
		</c:if>
		<br>
		
		<table class="list-area" align="center" border="1">
			<thead>
				<tr>
					<th width="70">글 번호</th>
					<th width="70">카테고리</th>
					<th width="300">제목</th>
					<th width="100">작성자</th>
					<th width="50">조회수</th>
					<th width="100">작성일</th>
				</tr>
			</thead>
			<tbody>
				<!-- 조회해온 list 정보 형식맞춰 출력하기 
					 조회된 데이터 없으면 게시글이 없습니다 메세지 나올 수 있도록 작성하기.
				 -->
				<c:choose>
					<c:when test="${empty list }">
					<!-- 조회된 데이터가 없을때 -->
						<tr>
							<td colspan="6">조회된 게시글이 없습니다.</td>
						</tr>
					</c:when>
					<c:otherwise>
					<!-- 조회된 데이터가 있을때 -->
						<c:forEach var="b" items="${list}">
							<tr>
								<td>${b.boardNo}</td>
								<td>${b.category }</td>
								<td>${b.boardTitle }</td>
								<td>${b.boardWriter }</td>
								<td>${b.count }</td>
								<td>${b.createDate }</td>
							</tr>
						</c:forEach>
							
					</c:otherwise>
				</c:choose>
				
				
			
			</tbody>
			
		
		</table>
		
		<!-- 글목록에 있는 글을 클릭했을때 해당 글의 상세보기를 할 수 있도록 처리하기 
			조회수 증가 메소드도 구현하기 (공지사항 상세보기 참조) 
			메소드명 increaseCount() 
			controller : BoardDetailController
			메소드명 : selectBoard 
			매핑주소 : detail.bo
			조회할 데이터 : 번호,카테고리명,글제목,글내용,작성자아이디,생성일 
			컨트롤러에서 출력문으로 출력해보기 
		
		 -->
		<script>
			$(function(){
				
				$(".list-area>tbody>tr").click(function(){
					//글번호 추출
					//console.log($(this).children().first().text());
					var bno = $(this).children().first().text();
					location.href="detail.bo?bno="+$(this).children().first().text();
					
				});
				
			});
		
		</script>
	
		<br><br>
		
		
		<div align="center" class="paging-area">
			<!-- 이전/다음 버튼은 1페이지일땐 이전버튼 비활성화 마지막페이지일땐 다음버튼 비활성화 
				1~10 은 반복문을 이용하여 출력하기 
				이때 pi에 담긴 정보를 활용하여 처리해보세요
				버튼을 눌렀을때 해당 페이지로 이동될수 있는 처리도 해보기
			-->
			
		<c:choose>
			<c:when test="${pi.currentPage eq 1}">
				<button disabled>이전</button>
			</c:when>
			<c:otherwise>
				<button onclick="location.href='list.bo?currentPage=${pi.currentPage-1}'">이전</button>
			</c:otherwise> 
		</c:choose>	
		
		<c:forEach var="i" begin="${pi.startPage }" end="${pi.endPage }">
			 <button onclick="location.href='list.bo?currentPage=${i}'">${i}</button>
		</c:forEach>
		<c:choose>
			<c:when test="${pi.currentPage eq pi.maxPage}">
				<button disabled>다음</button>
			</c:when>
			<c:otherwise>
				<button onclick="location.href='list.bo?currentPage=${pi.currentPage+1}'">다음</button>
			</c:otherwise> 
		</c:choose>	
		</div>
		
		<br><br>
		
	</div>
	
	
</body>
</html>
