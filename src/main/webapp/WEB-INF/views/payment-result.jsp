
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Result | Smart Parking</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f3f6fb; color: #172033; margin: 0; }
        main { max-width: 600px; margin: 60px auto; padding: 0 20px; }
        .panel { background: white; padding: 30px; border-radius: 12px; text-align: center; box-shadow: 0 4px 18px rgba(16,33,61,.08); }
        .success { color: #16803c; }
        .failed { color: #b42318; }
        .reference { overflow-wrap: anywhere; }
        a { display: inline-block; margin: 12px; color: #1765d1; }
    </style>
</head>
<body>
<main>
    <section class="panel">
        <c:choose>
            <c:when test="${payment.status eq 'SUCCESS'}">
                <h1 class="success">Payment Successful</h1>
                <p>Your reservation is confirmed.</p>
            </c:when>
            <c:otherwise>
                <h1 class="failed">Payment Failed</h1>
                <p>Your reservation remains pending. You may retry payment.</p>
            </c:otherwise>
        </c:choose>

        <p>Reservation ID: <strong><c:out value="${reservation.id}"/></strong></p>
        <p>Amount: ₹<c:out value="${payment.amount}"/></p>
        <p>Method: <c:out value="${payment.method}"/></p>
        <p>Status: <strong><c:out value="${payment.status}"/></strong></p>
        <p class="reference">
            Reference: <c:out value="${payment.transactionReference}"/>
        </p>

        <a href="${pageContext.request.contextPath}/reservation/history">View Booking History</a>

        <c:if test="${payment.status eq 'FAILED'}">
            <a href="${pageContext.request.contextPath}/reservation/payment?reservationId=${reservation.id}">Try Again</a>
        </c:if>
    </section>
</main>
</body>
</html>
