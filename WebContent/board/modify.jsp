<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>수정</title>
<script src = "https://code.jquery.com/jquery-1.7.1.min.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        $("#modify-form").on("submit", function(event) {
            event.preventDefault(); // 폼의 기본 제출 동작을 막습니다.
            validateForm();
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
        var title = document.forms["modify-form"]["title"].value;
        var name = document.forms["modify-form"]["name"].value;
        var pass = document.forms["modify-form"]["pass"].value;
        var content = document.forms["modify-form"]["content"].value;
        
        var errorMessage = "";

        // 스페이스바로만 이루어진 입력을 제거합니다.
        title = title.trim();
        name = name.trim();
        pass = pass.trim();
        content = content.trim();

        if (title == "") {
            errorMessage = "제목을 입력해주세요.";
        } else if (name == "") {
            errorMessage = "이름을 입력해주세요.";
        } else if (pass == "") {
            errorMessage = "비밀번호를 입력해주세요.";
        } else if (content == "") {
            errorMessage = "내용을 입력해주세요.";
        } else if (getByteLength(name) > 50) {
            errorMessage = "이름은 최대 50바이트(영문 50자, 한글 25자)까지 입력 가능합니다.";
        } else if (getByteLength(title) > 100) {
            errorMessage = "제목은 최대 100바이트(영문 100자, 한글 50자)까지 입력 가능합니다.";
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
            data: {"seq" : "${modify.seq}", "pass" : $("#pass").val()},
            success: function(response) {
                if (response == 1) {
                    $("#modify-form").attr("action", "modify_pro.do");
                    $("#modify-form").off("submit"); // 이벤트 핸들러를 해제합니다.
                    $("#modify-form").submit(); // 폼을 제출합니다.
                } else {
                    alert("비밀번호가 일치하지 않습니다.");
                }
            },
            error: function() {
                console.error("통신 실패");
            }
        });
    }

    function deleteFile() {
        document.getElementsByName("file-remove")[0].value = "true";
        document.getElementsByName("originalFileName")[0].value = "";
    }
</script>
</head>
<body>
<!--ajax 통신을 통해 seq, pass 값을 보낸 뒤 다시 뿌려주기  -->
	<h1>수정</h1>
	<form action="modify_pro.do" method="post" id = "modify-form" enctype="multipart/form-data">
		<input type ="hidden" name ="seq" value = "${modify.seq }">
		<table width="500" cellpadding="0" cellspacing="0" align="left" style = "border: none;">
		<tr>
			<th>
				<label for="title">제목</label>
			</th>
			<td>		
				<input type="text" name="title" value = "${modify.title }">
			</td> 
		</tr>
		<tr>
			<th>
				<label for="name">이름</label>
			</th>
			<td>
				<input type="text" name="name" value = "${modify.name }">
			</td>		
		</tr>
		<tr>
			<th>
				<label for="pass">비밀번호</label>
			</th>
			<td>
				<input type="password" name="pass" id="pass">			
			</td>
		</tr>
		<tr>
			<th>
				<label for ="content">내용</label>
			</th>
			<td>
				<textarea name="content" cols = "30" rows = "5">${modify.content}</textarea>
			</td>			
		</tr>
		<tr>
			<th>
				<label for ="file">첨부파일</label>
			</th>
			<td>
				<input type="text" name="originalFileName" value="${modify.originalFileName}">
				<input type="hidden" name="fileName" value="${modify.fileName }">
                <input type="hidden" name="file-remove" value="false" />
                <button type = "button" onclick="deleteFile()">삭제</button><br>
                <input type="file" name ="uploadFile">
			</td>		
		</tr>
		<tr>
			<th align="center">
				<input type="submit" value = "수정" id="submit-btn"> 
				<input type="button" value = "취소" onclick="location.href='read.do?seq=${modify.seq}'">
				<input type="button" value = "목록" onclick="location.href='list.do'">
			</th>
		</tr>
		</table>
	</form>
</body>
</html>