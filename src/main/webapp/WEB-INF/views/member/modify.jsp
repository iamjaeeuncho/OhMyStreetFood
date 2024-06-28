<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
	var isNickNameDuplicateChecked = false;
	
	$(document).ready(function(){
		$("#withdrawal").click(function(){
			if (confirm("정말로 탈퇴하시겠습니까?") == true){
				$.ajax({
                    url: './withdrawal',
                    type: 'POST',
                    success: function(response) {
                        window.location.href = response.redirectUrl;
                    },
                    error: function(xhr, status, error) {
                        alert("서버 오류입니다. 다시 시도해 주세요.");
                    }
                });
			}
				
		});
		
		$("#nickNameDuplicateConfirm").click(function() {
	          
        	var nickName = $("#nickName").val();
          
            if(nickName == '' || nickName.length == 0) {
            	$("#nickNameAlertLabel").css("color", "red").text("공백은 닉네임으로 사용할 수 없습니다.");
             	return false;
          	}
          
            //Ajax로 전송
            $.ajax({
            	url : './modifyMember/confirmNickName',
                data : {
                	nickName : nickName
              	},
                type : 'POST',
                dataType : 'json',
                success : function(result) {
                	if (result == true) {
                    	$("#nickNameAlertLabel").css("color", "black").text("사용 가능한 닉네임 입니다.");
                    	isNickNameDuplicateChecked = true;
                 	} else{
                 		$("#nickNameAlertLabel").css("color", "red").text("사용 불가능한 닉네임 입니다.");
                    	$("#nickName").val('');
                    	isNickNameDuplicateChecked = false;
                 	}
              	},
	            error: function(xhr, status, error) {
	            	console.error("AJAX Error: " + status + error);
	                isNickNameDuplicateChecked = false;
	            }
           });
            
           $("#nickName").on('input', function() {
        	   $("#nickNameAlertLabel").empty();
        	   isNickNameDuplicateChecked = false;
           });
		});
	});
</script>
</head>
<body>
	<form:form modelAttribute="member" action="${pageContext.request.contextPath}/modifyMember/${member.memberType}" method="post">
		<label>비밀번호 변경</label>
		<form:password path="password" />
		<form:errors path="password" cssClass="text-danger"/>
		
		<label>비밀번호 확인</label>
		<form:password path="passwordConfirm"/>
		<form:errors path="passwordConfirm" cssClass="text-danger"/>
		
		<sec:authorize access="hasRole('ROLE_USER')">
			<label>닉네임 변경</label>
			<form:input path="nickName" value="${member.nickName }"/>
			<input type="button" id="nickNameDuplicateConfirm" value="중복 확인">
			<label id = nickNameAlertLabel></label>
		</sec:authorize>
		
		<input type="submit" value="변경">
		<input type="hidden" name="username" value="${member.username }">
		<input type="hidden" name="memberType" value="${member.memberType }">
	</form:form>
	<a href="#" id="withdrawal">회원 탈퇴</a>
</body>
</html>