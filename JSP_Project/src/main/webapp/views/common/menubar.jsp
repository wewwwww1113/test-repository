<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.kh.member.model.vo.Member"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
 
<%
	//로그인 정보 추출하기 
	Member loginUser = (Member)session.getAttribute("loginUser");
	//로그인이 되어있지 않다면 loginUser라는 key값으로 데이터를 조회할 수 없으니 
	//null이 반환된다. 로그인이 되었다면 해당 로그인 정보가 반환된다. 
	
	//알림 메세지 추출하기 
	String alertMsg = (String)session.getAttribute("alertMsg");
	
	//contextPath 변수처리해서 사용하기 
	String contextPath = request.getContextPath();
	
	//스크립틀릿에서 쿠키 접근하기 
	Cookie[] cookies = request.getCookies();
	
	//해당 배열에서 원하는 쿠키데이터 추출하기 
	//반복처리하여 원하는 키값 찾기
	String saveId = "";
	
	if(cookies !=null){
		//순차적으로 반복하여 찾아내기 
		for(Cookie c : cookies){
			//쿠키의 이름을 반환하는 메소드 : 쿠키.getName();
			if(c.getName().equals("userId")){ //userId 키값을 가진 쿠키라면 
				saveId = c.getValue(); //쿠키.getValue(); 해당 쿠키에 담긴 값을 반환하는 메소드
				break;
			}
		}
	}
	
	
%>  
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>메뉴바</title>
    
    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
	
	<!-- Popper JS -->
	<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
	
	<!-- Latest compiled JavaScript -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    

    <style>
        #login-form,#user-info{
            float: right;
        }
        #user-info a{
            text-decoration: none;
            color: black;
            font-size: 12px;
        }

        .nav-area {
            background-color: black;
        }
        .menu {
            display:inline-block;
            height: 50px;
            width: 150px;
        }
        .menu a {
            text-decoration: none;
            color:white;
            font-size: 20px;
            font-weight: bold;
            display: block;
            width: 100%;
            height: 100%;
            line-height: 50px;
        }
        .menu a:hover{
            background-color: rgb(144, 142, 142);
        }
	
		.outer{
            background-color: black;
            color: white;
            width: 1000px;
            margin: auto; /*가운데 자동정렬*/
            margin-top: 50px; /*위로부터 50px 여백*/

        }
        #enroll-form table {
            margin: auto;
        }
        #enroll-form input {
            margin:5px;
        }
        
        #myPage-form table {
            margin: auto;
        }
        #myPage-form input {
            margin:5px;
        }

    </style>



</head>
<body>
	<!-- el로 contextPath 사용하기 위한 작업(page에 변수처리해주기) -->
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

	<script>
		//알림메세지 띄워주기 
		//로그인성공 또는 null - 값 자체로 반환되기때문에 문자열 " "처리 해야함
		var msg = "<%=alertMsg%>";
		
		if(msg!="null"){ //null이 아니면 알림메세지 띄워라
			alert(msg); 
			//알림 띄워주고 해당 메세지 지워주기
			<%session.removeAttribute("alertMsg");%>
		}
	</script>



    <h1 align="center">Welcome My Web Project</h1>

    <!-- 로그인 영역 -->
    <div class="login-area">
        <!--로그인 전 후 화면 나누기-->    

        <!--로그인 전 : 아이디 비밀번호 입력 input과 로그인 버튼 (회원가입/id/pwd 찾기)-->
		<%if(loginUser == null) {%>
        <form id="login-form" action="<%=contextPath%>/login.me" method="post">
            <table>
                <!-- tr>(th+td)*2 -->
                <tr>
                    <th>아이디</th>
                    <td><input type="text" id="loginId" name="userId" required></td>
                </tr>
                <tr>
                    <th>비밀번호</th>
                    <td><input type="password" name="userPwd" required></td>
                </tr>
                <tr>
                	<td colspan="2">
                		<label for="saveId">아이디 저장 :</label> <input type="checkbox" id="saveId" name="saveId">
                 	</td>
                </tr>
                <tr>
                    <th colspan="2">
                        <button type="submit">로그인</button>
                        <button type="button" onclick="enrollForm();">회원가입</button>
                    </th>
                </tr>
            </table>
        </form>
        
        <script>
        	$(function(){
        		//쿠키에 아이디가 담겨있다면 체크박스 체크해놓기 
        		
        		var saveId = "${cookie.userId.value}";
        		//해당 데이터가 존재하는지 여부는 "" 비교를 통해 진행
				if(saveId!=""){
					$("#saveId").attr("checked",true);
					//값 넣기
					$("#loginId").val(saveId);
				}        		
        		
        	});
        
        
        	function enrollForm(){
        		//회원가입 페이지로 이동시키는 함수 
        		//location.href = "이동시킬 경로"
        		//단순 페이지 이동 요청을 보내면 url에 프로젝트 디렉토리 구조가 노출되기 때문에 보안상 문제가 있을 수 있다.
        		//때문에 단순 페이지 이동요청도 servlet을 거쳐서 요청처리 해야한다.
        		//location.href = "<%=contextPath%>/views/member/memberEnrollForm.jsp";
        		
        		location.href = "<%=contextPath%>/enrollForm.me";
        		
        		
        	}
        
        </script>
        
        
        
	<%}else{ %>
        <!--로그인 후(loginUser가 null이 아닐때)-->
        <div id="user-info">
            <b><%=loginUser.getUserName()%>님 환영합니다.</b>
            <div align="center">
                <a href="<%=contextPath%>/myPage.me">마이페이지</a> 
                <a href="<%=contextPath %>/logout.me">로그아웃</a>
            </div>
        </div>
    </div>
    <%} %>
  	<!--float 해제하기-->
    <br clear="both">


    <!-- 메뉴바 -->
    <div class="nav-area" align="center">
        <div class="menu">
            <a href="<%=contextPath%>">HOME</a>
        </div>
        <div class="menu">
            <a href="<%=contextPath%>/list.no">공지사항</a>
        </div>

        <div class="menu">
            <a href="<%=contextPath%>/list.bo?currentPage=1">일반게시판</a>
        </div>
        <div class="menu">
            <a href="${contextPath }/list.th">사진게시판</a>
        </div>
    </div>

</body>
</html>