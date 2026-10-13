
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Simulated Payment | Smart Parking</title>
    <style>
        body { margin: 0; font-family: Arial, sans-serif; background: #f3f6fb; color: #172033; }
        header { background: #10213d; color: white; padding: 20px 7%; }
        main { max-width: 650px; margin: 35px auto; padding: 0 20px; }
        .panel { background: white; padding: 26px; border-radius: 12px; box-shadow: 0 4px 18px rgba(16,33,61,.08); }
        .amount { font-size: 28px; font-weight: bold; margin: 12px 0 24px; }
        label { display: block; margin: 18px 0 8px; }
        select, button { box-sizing: border-box; width: 100%; padding: 12px; border-radius: 7px; font-size: 15px; }
        select { border: 1px solid #ccd5e2; background: white; }
        button { margin-top: 20px; border: 0; background: #1765d1; color: white; font-weight: bold; cursor: pointer; }
        .notice { padding: 12px; background: #fff4d6; border-radius: 7px; margin: 15px 0; }
        a { color: #1765d1; }
    </style>
</head>
<body>
<header>
    <h1>Smart Parking</h1>
    <p>Reservation Payment</p>
</header>
<main>
    <p><a href="${pageContext.request.contextPath}/reservation/history">Back to My Reservations</a></p>

    <section class="panel">
        <h2>Complete Your Booking</h2>
        <p>Reservation ID: <strong><c:out value="${reservation.id}"/></strong></p>
        <p>Parking Slot: <strong><c:out value="${reservation.parkingSlotId}"/></strong></p>
        <p>Start: <c:out value="${reservation.startTime}"/></p>
        <p>End: <c:out value="${reservation.endTime}"/></p>
        <div class="amount">₹<c:out value="${reservation.totalAmount}"/></div>

        <div class="notice">
            College project demonstration only. No real money will be charged.
        </div>

        <c:if test="${not empty paymentMessage}">
            <p><c:out value="${paymentMessage}"/></p>
        </c:if>

        <c:if test="${reservation.status eq 'PENDING'}">
            <form method="post" action="${pageContext.request.contextPath}/reservation/payment">
                <input type="hidden" name="reservationId" value="${reservation.id}">

                <label for="method">Payment Method</label>
                <select id="method" name="method" required>
                    <option value="UPI">UPI (simulated)</option>
                    <option value="CARD">Card (simulated)</option>
                    <option value="CASH">Cash</option>
                </select>

                <label for="outcome">Simulation Result</label>
                <select id="outcome" name="outcome" required>
                    <option value="SUCCESS">Simulate Successful Payment</option>
                    <option value="FAILED">Simulate Failed Payment</option>
                </select>

                <button type="submit">Run Payment Simulation</button>
            </form>
        </c:if>
    </section>
</main>
</body>
</html>
