<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<!-- 	상대경로작성법 : ../ - 상위폴더로 한단계이동  -->
	<%-- 	<%@ include file = "../common/menubar.jsp" %> --%>
	<!-- 	절대경로 작성법 : 처음 / : webapp을 가르키게 된다 -->
	<%@ include file="/views/common/menubar.jsp"%>
	<div class="outer">
		<br>

		<h2 align="center">회원가입</h2>

		<form id="enroll-form" action="<%=contextPath %>/insert.me" method="post">
			<!-- 아이디,비밀번호,이름,전화번호,주소,취미,이메일 -->
			<table>
				<!-- (tr>td*3)*8 -->
				<tr>
					<td>* 아이디</td>
					<td><input type="text" id="userId" name="userId" required></td>
					<td><button type="button" onclick="idCheck();">중복확인</button></td>
					<!-- 비동기통신을 이용하여 버튼을 눌렀을때 아이디 입력란에 작성된 아이디를 
						서버에 전달 및 요청하여 아이디 중복확인을 하고오기 
						매핑주소 : idCheck.me
						컨트롤러 : MemberIdCheckController
						메소드명 : idCheck()
						버튼을 눌렀을때 요청후 응답데이터를 통해 사용가능하면 
						사용가능한 아이디입니다 사용하시겠습니까? 메세지로 확인 취소 작업 (confirm)
						확인(사용하겠다)시 input id 란을 readonly로 변경하여 아이디 확정짓기 
						또한 기존에 아이디 중복체크가 되지 않은 상황에선 회원가입 버튼 비활성화.
						중복체크 후 활성화하여 회원가입 처리 가능하도록 구현하기.
					 -->
				</tr>
				<tr>
					<td>* 비밀번호</td>
					<td><input type="password" name="userPwd" required></td>
					<td></td>
				</tr>
				<tr>
					<td>* 비밀번호확인</td>
					<td><input type="password" id="pwChk" required></td>
					<td></td>
				</tr>
				<tr>
					<td>* 이름</td>
					<td><input type="text" name="userName" required></td>
					<td></td>
				</tr>
				<tr>
					<td>전화번호</td>
					<td><input type="text" name="phone"></td>
					<td></td>
				</tr>
				<tr>
					<td>이메일</td>
					<td><input type="email" name="email"></td>
					<td></td>
				</tr>
				<tr>
					<td>주소</td>
					<td><input type="text" name="address"></td>
					<td></td>
				</tr>
				<tr>
					<td>관심분야</td>
					<td colspan="2">
						<!-- (input[type=checkbox name=interest id= value=]+label)*6 --> <input
						type="checkbox" name="interest" id="sports" value="운동"><label
						for="sports">운동</label> <input type="checkbox" name="interest"
						id="hiking" value="등산"><label for="hiking">등산</label> <input
						type="checkbox" name="interest" id="fising" value="낚시"><label
						for="fising">낚시</label> <br> <input type="checkbox"
						name="interest" id="cooking" value="요리"><label
						for="cooking">요리</label> <input type="checkbox" name="interest"
						id="game" value="게임"><label for="game">게임</label> <input
						type="checkbox" name="interest" id="movie" value="영화"><label
						for="movie">영화</label>
					</td>
				</tr>
			</table>

			<br>
			<br>

			<div align="center">
				<button type="submit" disabled>회원가입</button>
				<button type="reset">초기화</button>
			</div>
		</form>
		
		<script>
			function idCheck(){
				
				var inputId= $("#userId").val();
				
				
				$.ajax({
					url : "${contextPath}/idCheck.me",
					data : {
						inputId : inputId	
					},
					success : function(result){
						//result가 NNNNN 또는 NNNNY로 반환됨 
						if(result=="NNNNN"){ //사용불가
							alert("이미 존재하는 아이디입니다.");
						}else{ //사용가능
							if(confirm("정말 사용하시겠습니까?")){ //사용
								$("#enroll-form :submit").removeAttr("disabled"); //비활성화제거
								$("#userId").attr("readonly",true); //아이디 수정못하도록 변경
								
							}else{ //사용안함 
								//다시 입력유도
								$("#userId").focus();
							}
						}
					},
					error : function(){
						console.log("통신실패");
					}
				});
				
				
				
			}
		
		</script>



		<br>
		<br>
	</div>




</body>
</html>