<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	
	#enroll-form input,#enroll-form textarea{
		width :100%;
		box-sizing : border-box;
	}
	#enroll-form td>img{
		width : 100%;
		height : 100%;
	}

</style>


</head>
<body>
	<%@ include file="/views/common/menubar.jsp" %>
	<div class="outer">
		<br>
		<h2 align="center">사진 게시글 작성</h2>
		<br>
		
		<form action="${contextPath }/insert.th" method="post" enctype="multipart/form-data" id="enroll-form">
			<input type="hidden" name="userNo" value="${loginUser.userNo }">
			<table align="center" border="1">
				<tr>
					<th width="100">제목</th>
					<td colspan="3">
						<input type="text" name="title" required>
					</td>
				</tr>
				<tr>
					<th>내용</th>
					<td colspan="3">
						<textarea name="content" rows="10" style="resize:none;" required></textarea>
					</td>
				</tr>
				<tr>
					<th>대표이미지</th>
					<td colspan="3" algin="center"  width="250" height="170">
						<img id="titleImg">
					</td>
				</tr>
				<tr>
					<th>상세이미지</th>
					<td width="150" height="120">
						<img id="contentImg1" >
					</td>
					<td width="150" height="120">
						<img id="contentImg2">
					</td>
					<td width="150" height="120">
						<img id="contentImg3">
					</td>
				</tr>
			</table>
			<br><br>
			
			<div id="file-area">
				<!-- onchange : input요소 상태가 변경되면 동작하는 이벤트 -->
			
				<input type="file" id="file1" name="file1" onchange="loadImg(this,1);" required> 
				<input type="file" id="file2" name="file2" onchange="loadImg(this,2);">
				<input type="file" id="file3" name="file3" onchange="loadImg(this,3);">
				<input type="file" id="file4" name="file4" onchange="loadImg(this,4);">
			</div>
			
			<div align="center">
				<button type="submit">글작성</button>
			</div>
		</form>
		
		<script>
			$(function(){
				//이미지영역 클릭했을때 input file 태그가 동작하도록 처리 
				$("#titleImg").click(function(){
					//대표이미지영역이 클릭되었을때 input file1 태그 클릭시키기
					$("#file1").click();
				});
				$("#contentImg1").click(function(){
					//상세이미지영역이 클릭되었을때 input file 태그 클릭시키기
					$("#file2").click();
				});
				$("#contentImg2").click(function(){
					//상세이미지영역이 클릭되었을때 input file 태그 클릭시키기
					$("#file3").click();
				});
				$("#contentImg3").click(function(){
					//상세이미지영역이 클릭되었을때 input file 태그 클릭시키기
					$("#file4").click();
				});
				
				
				//input file 숨기기
				$("#file-area").hide();
				
				
			});
			
			
			//onchange에서 동작할 함수 
			function loadImg(inputFile,num){
				//inputFile : 현재 이벤트가 발생한 요소 (input type="file")
				//num : 해당 요소가 몇번째인지
				//inputFile.files : 업로드된 파일정보를 확인할수있는 속성(배열형식으로 담음 fileList)
				//파일을 선택하면 0번 인덱스에 파일정보가 담긴다. 이때 length는 1이 된다
				//파일을 선택했을땐 length가 1 선택하지 않았을땐 0 이기때문에 
				//해당 조건으로 파일에대한 처리하기.
				
				if(inputFile.files.length == 1){//파일이 존재할때
					//해당파일 정보를 읽어 미리보기 영역에 보여주기 
					//파일 정보를 읽어줄 객체 FileReader() 준비 
					var reader = new FileReader();
					
					//파일을 읽어 해당 파일의 고유한 url을 생성해주는 메소드 
					//readAsDataURL(파일정보) 이라는 메소드 
					//우리가 넣은 파일정보는 inputFile.files[0] 에 담겨있다
					reader.readAsDataURL(inputFile.files[0]);
					
					//reader 객체가 해당파일 정보를 읽어오는 시점 onload
					//reader 객체의 파일 읽기가 완료되었을때
					reader.onload = function(e){
						//처리된 이벤트 결과에 대해 알아오기 
						//이벤트 정보 받아오기 : e 
						//해당이벤트가 발생한 대상의 결과 : e.target.result (생성된 고유 url이 담긴다)
						console.log(e.target.result);
						//읽어온 url을 각 미리보기 영역에 부여하기 
						
						switch(num){
							case 1 : $("#titleImg").attr("src",e.target.result); break;
							case 2 : $("#contentImg1").attr("src",e.target.result); break;
							case 3 : $("#contentImg2").attr("src",e.target.result); break;
							case 4 : $("#contentImg3").attr("src",e.target.result); break;
						
						}
					}
				}else{
					//선택된 파일이 사라졌을때 src 비워주기
					switch(num){
						case 1 : $("#titleImg").attr("src",null); break;
						case 2 : $("#contentImg1").attr("src",null); break;
						case 3 : $("#contentImg2").attr("src",null); break;
						case 4 : $("#contentImg3").attr("src",null); break;
					}
				}
			}
		
		</script>
		
		
		
		
	
	
	</div>
</body>
</html>