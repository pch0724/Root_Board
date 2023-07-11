<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>글쓰기</title>
<script>
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
        var title = document.forms["writeForm"]["title"].value;
        var name = document.forms["writeForm"]["name"].value;
        var pass = document.forms["writeForm"]["pass"].value;
        var content = document.forms["writeForm"]["content"].value;
        
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
            document.getElementById("writeForm").submit(); //유효성 검사를 통과하면 폼을 전송합니다.
        }
    }
</script>
</head>
<body>
	<h1>글쓰기</h1>
	<form action="write_pro.do" id = "writeForm" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
		<table width="500" cellpadding="0" cellspacing="0" align="left" style = "border: none;">
		<tr>
			<th>
				<label for="title">제목</label>
			</th>
			<td>		
				<input type="text" name="title">
			</td> 
		</tr>
		<tr>
			<th>
				<label for="name">이름</label>
			</th>
			<td>
				<input type="text" name="name">
			</td>		
		</tr>
		<tr>
			<th>
				<label for="pass">비밀번호</label>
			</th>
			<td>
				<input type="password" name="pass">			
			</td>
		</tr>
		<tr>
			<th>
				<label for ="content">내용</label>
			</th>
			<td>
				<textarea name="content" cols = "30" rows = "5"></textarea>
			</td>			
		</tr>
		<tr>
			<th>
				<label for ="uploadFile">첨부파일</label>
			</th>
			<td>
				<input type="text" name ="fileName" value="">
				<input type="file" name ="uploadFile" value="">
			</td>		
		</tr>
		<tr>
			<th align="center">
				<input type="submit" id = "btnWrite" value = "등록"> 
				<input type="button" value = "취소" onclick="location.href='list.do'">
			</th>
		</tr>
		</table>
	</form>
</body>
</html>