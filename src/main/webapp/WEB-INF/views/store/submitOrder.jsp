<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
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
    <link href="https://fonts.googleapis.com/css2?family=Chakra+Petch:ital,wght@0,300;0,400;0,500;0,600;0,700&family=Noto+Sans+KR:wght@100..900&display=swap" rel="stylesheet">
    <!-- CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.3.4/assets/owl.carousel.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.3.4/assets/owl.theme.default.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="container">
        <h3 style="text-align: center;">주문진행상황</h3>
        <div style="text-align: center">
	        해당 가게 사장님에게 주문이 요청되었습니다<br>
		    사장님 승인이 완료되어야 진짜 주문이 완료됩니다
	    </div>

주문번호: ${empty order.orderno ? 'Unknown' : order.orderno}<br>
가게번호: ${empty order.storeno ? 'Unknown' : order.storeno}<br>
${empty order.username ? 'Unknown' : order.username}<br>
${empty order.totalprice ? 'Unknown' : order.totalprice}<br>
${empty order.approval ? 'Unknown' : order.approval}<br>
${empty order.paymentstatus ? 'Unknown' : order.paymentstatus}<br>
${empty order.paymentmethod ? 'Unknown' : order.paymentmethod}<br>
${empty order.createdat ? 'Unknown' : order.createdat}<br>
${empty order.pickupat ? 'Unknown' : order.pickupat}<br>
${empty order.approvedat ? 'Unknown' : order.approvedat}<br>
${empty order.paidat ? 'Unknown' : order.paidat}<br>
    </div>
    
    <!-- Menu -->
    <jsp:include page="../menu.jsp" />

</body>
</html>