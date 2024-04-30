<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	#update-area input,#update-area textarea{
		width :100%;
		box-sizing : border-box;
	}
</style>
</head>
<body>
	<%@ include file="/views/common/menubar.jsp"%>

	<div class="outer">
		<br>
		<h2 align="center">게시글 수정</h2>
		<br>

		<form action="${contextPath }/update.bo" method="post"
			id="update-area" enctype="multipart/form-data">
			<!-- 어떤 게시글을 수정하려고 하는지 알아야하기 때문에 게시글번호(식별자) 같이 전달하기 -->
			<input type="hidden" name="boardNo" value="${b.boardNo}">
			<table border="1" align="center">
				<tr>
					<th width="70">카테고리</th>
					<td width="70"><select name="category">
							<!-- 카테고리 정보 db에서 조회해와서 넣어주기(반복문) -->
							<c:forEach items="${cList}" var="c">
								<option value="${c.categoryNo}">${c.categoryName}</option>
							</c:forEach>
					</select></td>
					<th width="70">제목</th>
					<td width="350"><input type="text" name="title" value="${b.boardTitle }" required>
					</td>
				</tr>
				<tr>
					<th>내용</th>
					<td colspan="3"><textarea name="content" rows="10"
							style="resize: none" required>${b.boardContent }</textarea></td>
				</tr>
				<tr>
					<th>첨부파일</th>
											
					<td colspan="3">
						<c:if test="${at!=null }">
						<!--첨부파일 정보 보여주기 -->
							${at.originName }
						<!-- 게시글에 첨부파일이 있었던 경우 해당 첨부파일 정보를 등록한 DB에 있는 정보에서 수정이 일어나야한다.
							 때문에 해당 데이터 식별자용으로 fileNo 를 전달해야하고 또한 서버에 업로드된 파일이 필요없어졌으니
							 삭제를 위해서 해당파일명이 필요하다(서버에 업로드된 파일명)
						 -->
						 <input type="hidden" name="originFileNo" value="${at.fileNo }">
						 <input type="hidden" name="originFileName" value="${at.changeName}">
						
						</c:if>
						<input type="file" name="reUploadFile">
					
					</td>
				</tr>
			</table>
			<br>
			<br>
			<div align="center">
				<button type="submit">수정하기</button>
				<button type="reset">취소</button>
			</div>
		</form>
		
		<script>
			$(function(){
				//게시글정보인 b 에서 가지고온 카테고리 이름과 지금 select에 option으로 등록되어있는
				//목록이 일치한것을 찾아서 선택시켜놓기 
				
				var choosed = "${b.category}"; 
				
				console.log(choosed);
				
				//$("#update-area option") option요소들 선택
				//각 요소를 순차적으로 순회하며 비교해야한다.
				//each()메소드를 사용하여 순차적접근
				$("#update-area option").each(function(){
					console.log($(this));
					if($(this).text() == choosed){ //선택했던 요소랑 일치하다면
						$(this).attr("selected",true); //해당옵션을 선택되어있게 변경
						return false; //break의 역할로 사용(단순 반복문이 아니기때문에 break 구문이 동작하지 않음)
					}
				});
				
			});
		
		</script>

		<br>
		<br>
	</div>
</body>
</html>