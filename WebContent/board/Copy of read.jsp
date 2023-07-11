<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>상세 보기</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<style>
body {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}
</style>
</head>
<body>
	<div class="container">
		<h1 class="text-center my-4">상세 보기</h1>
		<input type="hidden" name="seq" value="${read.seq}">
		<form>
			<div class="form-group">
				<label for="title">제목</label> 
				<input type="text" class="form-control" name="title" readonly="readonly" value="${read.title}">
			</div>
			<div class="form-group">
				<label for="name">이름</label> 
				<input type="text" class="form-control" name="name" readonly="readonly" value="${read.name}">
			</div>
			<div class="form-group">
				<label for="content">내용</label>
				<textarea class="form-control" name="content" cols="30" rows="5" readonly="readonly">${read.content}</textarea>
			</div>
			<div class="form-group">
				<label for="file">첨부파일</label>
				<c:set var="fileName" value="${read.fileName}" />
				<c:choose>
					<c:when test="${empty fileName}">
						<input type="text" class="form-control" id="fileName" value="${read.originalFileName}" name="fileName" readonly />
					</c:when>
					<c:otherwise>
						<a href="fileDownload.do?fileName=${read.fileName}&originalFileName=${read.originalFileName}">
							<input type="text" class="form-control" id="fileName" value="${read.originalFileName}" name="fileName" readonly />
						</a>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="text-center">
				<button type="button" class="btn btn-primary"
					onclick="location.href='reply.do?seq=${read.seq}'">답변</button>
				<button type="button" class="btn btn-warning"
					onclick="location.href='modify.do?seq=${read.seq}'">수정</button>
				<button type="button" class="btn btn-danger"
					onclick="location.href='delete.do?seq=${read.seq}'">삭제</button>
				<button type="button" class="btn btn-secondary"
					onclick="location.href='list.do'">목록</button>
			</div>
		</form>
	</div>
</body>
</html>