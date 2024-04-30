<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%@ include file="/views/common/menubar.jsp" %>
	
	<div class="outer">
		<br>
		<h2 align="center">게시글 상세보기</h2>
		<br>
			<table border="1" align="center">
				<tr>
					<th width="70">카테고리</th>
					<td width="70">
						${b.category }
					</td>
					<th width="70">제목</th>
					<td width="350">${b.boardTitle} </td>
				</tr>			
				<tr>
					<th>내용</th>
					<td colspan="3">
						<p style="height:200px; white-space:pre;">${b.boardContent }</p>
					</td>
				</tr>
				<tr>
					<th>첨부파일</th> <!-- 첨부파일이 있는 경우와 없는 경우 처리하기 -->
					<td colspan="3">
						
						<c:choose>
							<c:when test="${empty at}">
								첨부파일이 없습니다.
							</c:when>
							<c:otherwise>
								<!-- 다운로드 속성 부여 -->
								<a download href="${contextPath}${at.filePath}${at.changeName}">${at.originName }</a>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
			<br><br>
			
			<c:if test="${loginUser.userId eq b.boardWriter or loginUser.userId eq 'admin'}"> <!-- 로그인한 회원이 작성자라면 활성화 -->
				<div align="center">
					<button type="button" onclick="location.href='${contextPath}/update.bo?bno=${b.boardNo}'">수정하기</button>
					<!-- 삭제하기 delete.bo 메소드명 deleteBoard() 
						 컨트롤러 BoardDeleteController 
						 버튼눌렀을때 정말 삭제하시겠습니까? 확인 취소 로 확인눌렀을때 삭제 처리 
						 				
						 -->
					<button type="button" onclick="deleteBoard();">삭제하기</button>
				</div>
			</c:if>			
			<script>
				function deleteBoard(){
					var flag = confirm("정말 삭제하시겠습니까?");
					
					if(flag){
						location.href="${contextPath}/delete.bo?bno=${b.boardNo}";
					}
					
				}
			
			</script>
	
		<br><br>
		
		<div id="reply-area">
			<table border="1" align="center">
				<thead>
					<c:choose>
						<c:when test="${not empty loginUser }">
							<tr>
								<th>댓글작성</th>
								 <td>
								 	<textarea id="replyContent" rows="3" cols="50" style="resize:none;"></textarea>
								 </td>
								 <td><button onclick="insertReply();">댓글작성</button> </td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr>
								<th>댓글작성</th>
								 <td>
								 	<textarea readonly rows="3" cols="50" style="resize:none;">로그인 후 이용 가능한 서비스입니다.</textarea>
								 </td>
								 <td><button disabled>댓글작성</button> </td>
							</tr>
						
						</c:otherwise>
						
						
						
					</c:choose>
				</thead>
				<tbody>
					<tr>
						<td>작성자</td>
						<td>내용</td>
						<td>작성일</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<br><br>
	</div>
	
	<script>
		function insertReply(){
			
			$.ajax({
				url : "insertReply.bo",
				type : "post",
				data : {
					content : $("#replyContent").val(),
					bno : ${b.boardNo},
					userNo : "${loginUser.userNo}"
				},
				success : function(result){
					if(result>0){ //성공
						alert("댓글 작성 성공");
						//작성란 비워주기
						$("#replyContent").val("");
						replyList(); //댓글목록 갱신
					}else{
						alert("댓글 작성 실패");
					}
					
				},
				error : function(){
					console.log("통신 실패");
				}
			});
		}
		
		//댓글 목록 조회 함수
		
		function replyList(){
			
			$.ajax({
				url : "replyList.bo",
				data : {
					bno : ${b.boardNo}
				},
				success : function(list){
					
					//tbody에 tr만들어서 추가하기 
					var tr = "";
					console.log(list);
					//전부 추가하기
					for(var i in list){
						tr +="<tr>"
							+"<td>"+ list[i].replyWriter +"</td>"
							+"<td>"+ list[i].replyContent +"</td>"
							+"<td>"+ list[i].createDate +"</td>"
							+"</tr>";
					}
					
					$("#reply-area tbody").html(tr);
					
				},
				error : function(){
					console.log("통신오류");
				}
				
			});
		}
		
		$(function(){ //DOM 로드 이후 
			replyList(); //댓글목록 조회 
		});
		
	
	</script>
	
	
	
	
	
	

</body>
</html>