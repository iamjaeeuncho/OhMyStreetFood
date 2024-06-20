<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>OhMyStreetFood!</title>
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
<!-- Font Awesome CSS -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<!-- Google Fonts -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap"
	rel="stylesheet">
<!-- Jquery -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<!-- CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/styles.css">
<!-- JavaScript -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/addStoreOwner.js"></script>
</head>
<body>
	<div class="main">
		<div class="row">
			<div>
				<a href="javascript:history.go(-1);"
					style="text-decoration: none; color: inherit;"> <i
					class="fas fa-arrow-left"></i>
				</a>
			</div>
			<div class="col-md-12 text-center" id="logo">
				<h3>가게 정보</h3>
			</div>
			<form>
				<div class="form-group">
					<label for="exampleInputEmail1">가게 이름</label> <input type="email"
						class="form-control" id="exampleInputEmail1"
						aria-describedby="emailHelp" placeholder="Email">
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1">가게 주소</label> <input type="email"
						class="form-control" id="exampleInputEmail1"
						aria-describedby="emailHelp" placeholder="Email"> 카카오 API
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1">가게 대표 사진</label> <input
						type="email" class="form-control" id="exampleInputEmail1"
						aria-describedby="emailHelp" placeholder="Email">
				</div>
				<div class="form-group">
					<label for="exampleInputEmail1">가게 운영일자</label>
					<div class="buttons" style="padding: 20px 0 0 0">
						<button type="button"
							class="btn btn-outline-primary rounded-circle">월</button>
						<button type="button"
							class="btn btn-outline-primary rounded-circle">화</button>
						<button type="button"
							class="btn btn-outline-primary rounded-circle">수</button>
						<button type="button"
							class="btn btn-outline-primary rounded-circle">목</button>
						<button type="button"
							class="btn btn-outline-primary rounded-circle">금</button>
						<button type="button"
							class="btn btn-outline-primary rounded-circle">토</button>
						<button type="button"
							class="btn btn-outline-primary rounded-circle">일</button>
					</div>
				</div>
				<div class="form-group" style="margin-bottom: 15px;">
					<label for="exampleInputEmail1">가게 운영시간</label> <span
						style="display: flex; align-items: center;"> <input
						type="time" class="form-control" id="inputStartTime"
						placeholder="시작 시간 선택" style="width: 280px; margin-right: 10px;">
						<span>&nbsp;부터&nbsp;</span> <input type="time"
						class="form-control" id="inputEndTime" placeholder="종료 시간 선택"
						style="width: 280px; margin-right: 10px;"> <span>&nbsp;까지&nbsp;</span>
					</span>
				</div>
				<div class="form-group">
					<label for="exampleTextarea">가게 소개</label>
					<textarea class="form-control" id="exampleTextarea" rows="5"></textarea>
				</div>
				<div class="form-group">
					<label for="buttonText">메뉴 추가</label> <span
						style="display: flex; align-items: center;"> <input
						type="text" id="menuInput" class="form-control"
						style="width: 300px; margin-right: 10px;" placeholder="메뉴를 입력하세요">
						<input type="number" id="priceInput" class="form-control"
						style="width: 300px; margin-right: 10px;" placeholder="가격을 입력하세요">
						<button type="button" class="btn btn-primary" onclick="addMenu()">등록</button>
					</span>

				</div>
				<div id="menuContainer" style="width: 100%;">
					<!-- 동적 생성 -->
				</div>

				<div class="col-md-12">
					<button type="button" class="btn btn-primary"
						style="height: 50px; width: 100%; margin-bottom: 10px;">등록하기</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>