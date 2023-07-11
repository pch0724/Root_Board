<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>비밀번호 확인</title>
<script src = "https://code.jquery.com/jquery-1.7.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
	    $("#delete-form").on("submit", function(event) {
	        event.preventDefault(); // 폼의 기본 제출 동작을 막습니다.
	        validateForm(); // 비밀번호 확인 함수를 호출합니다.
	    });
	});

    // 문자열의 바이트 길이를 계산하는 함수입니다.
    function getByteLength(str) {
        var byteLength = 0;
        for (var i = 0; i < str.length; i++) {
            var charCode = str.charCodeAt(i);
            if (charCode <= 0x7F) {
                byteLength += 1;
            } else {
                byteLength += 2;
            }
        }
        return byteLength;
    }	
    
    function validateForm() {
        var pass = document.forms["delete-form"]["pass"].value;
        
        var errorMessage = "";

        // 스페이스바로만 이루어진 입력을 제거합니다.
        pass = pass.trim();


        if (pass == "") {
            errorMessage = "비밀번호를 입력해주세요.";
        } else if (getByteLength(pass) > 10) {
            errorMessage = "비밀번호는 최대 10바이트(영문 10자, 한글 5자)까지 입력 가능합니다.";
        }

        if (errorMessage) {
            alert(errorMessage);
            return false;
        } else {
        	checkPassword();
        }
    }
    
	function checkPassword() {
	    $.ajax({
	        type: "POST",
	        url: "checkPassword.do",
	        data: {"seq" : "${delete.seq}", "pass" : $("#pass").val()},
	        success: function(response) {
	            if (response == 1) {
	            	$("#delete-form").attr("action", "delete_pro.do");
	                $("#delete-form").off("submit"); // 이벤트 핸들러를 해제합니다.
	                $("#delete-form").submit(); // 폼을 제출합니다.
	            } else {
	                alert("비밀번호가 일치하지 않습니다.");
	            }
	        },
	        error: function() {
	            console.error("통신 실패");
	        }
	    });
	}
</script>
</head>
<body>
	<h1>비밀번호 확인</h1>
	
	<form action="delete_pro.do" method="post" id ="delete-form" >
		<input type ="hidden" name ="seq" value = "${delete.seq }">
		<table width="500" cellpadding="0" cellspacing="0" align="left" style = "border: none;">
		
		<tr>
			<th>
				<label for="pass">비밀번호</label>
			</th>
			<td>
				<input type="password" name="pass" id = "pass">			
			</td>
		</tr>
		<tr>
			<th align="center">
				<input type="submit" value = "삭제" id="submit-btn"> 
				<input type="button" value = "취소" onclick="location.href='list.do'">
			</th>
		</tr>
		</table>
	</form>
</body>
</html>