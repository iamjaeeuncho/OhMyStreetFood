<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>OhMyStreetFood!</title>
<!-- Bootstrap CSS -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
<!-- Font Awesome CSS -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<!-- Google Fonts -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
<!-- Jquery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<!-- CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
<%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/css/review.css"> --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/reviewModal.css">
<!-- 리뷰 수정 및 삭제 -->
<script src="${pageContext.request.contextPath}/js/reviewEdit.js"></script>
</head>
<body>			
	<div id="review-detail" class="review-detail">
        <div class="review-modal-content">
			<div class="review-container">
				<span class="review-close-button">&times;</span>
				<h1><spring:message code="review" /></h1>
				<form:form id="reviewForm" modelAttribute="review" method="post" action="${pageContext.request.contextPath}/review/command">
					<p>
						<form:hidden path="storeStoreNo" />
						<form:errors path="storeStoreNo" id="error"/>
					</p>
					<label for="username"><spring:message code="user" /></label>
					<form:input path="memberUsername" disabled="true"/>
					<form:hidden path="memberUsername" />
					
					<label for="rating"><spring:message code="rating" /></label>
					<div class="rating">
						<form:radiobutton path="rating" value="1" id="star1" disabled="true"/>
						<label for="star1" title="1 stars"><i class="fas fa-star"></i></label>
						<form:radiobutton path="rating" value="2" id="star2" disabled="true"/>
						<label for="star2" title="2 stars"><i class="fas fa-star"></i></label>
						<form:radiobutton path="rating" value="3" id="star3" disabled="true"/>
						<label for="star3" title="3 stars"><i class="fas fa-star"></i></label>
						<form:radiobutton path="rating" value="4" id="star4" disabled="true"/>
						<label for="star4" title="4 stars"><i class="fas fa-star"></i></label>
						<form:radiobutton path="rating" value="5" id="star5" disabled="true"/>
						<label for="star5" title="5 stars"><i class="fas fa-star"></i></label>
					</div>
					<label for="content"><spring:message code="review.content" /></label>
					<form:textarea path="content" rows="5" disabled="true"/>
					<c:if test="${not empty errors}">
				        <ul>
				            <c:forEach var="error" items="${errors}">
				                <div style="text-align: center;">
									<span id="error">${error.defaultMessage}</span>
								</div>
				            </c:forEach>
				        </ul>
				    </c:if>
						<input type="hidden" name="command" id="command" value="" />
						<input type="hidden" name="reviewNo" id="reviewNo" value="${reviewNo}" />
						<input type="hidden" name="requestPage" id="requestPage" value="${requestPage}"/>
						<sec:authorize access="authentication.name == '${memberUsername}'">
							<button id="delete-submit" onclick="deleteCommand()"><spring:message code="delete.btn" /></button>
							<button id="update-submit" onclick="updateCommand()"><spring:message code="update.btn" /></button>
						</sec:authorize>
				</form:form>
				<sec:authorize access="authentication.name == '${memberUsername}'">
					<button id="update-btn" onclick="enableEditing()" ><spring:message code="update.enable" /></button>
				</sec:authorize>
			</div>
        </div>
    </div>


	<!-- 리뷰 모달 -->
	<script src="${pageContext.request.contextPath}/js/reviewDetail.js"></script>

</body>
</html>
