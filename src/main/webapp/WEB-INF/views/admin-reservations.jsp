<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Reservation Monitoring | Smart Parking</title>
    <style>
        * { box-sizing: border-box; }
        body { margin: 0; background: #0b1120; color: #e5e7eb; font-family: Arial, sans-serif; }
        header { padding: 22px 5%; background: #111b30; border-bottom: 1px solid #26344d; }
        main { max-width: 1250px; margin: 30px auto; padding: 0 20px; }
        a { color: #7dd3fc; text-decoration: none; }
        .table-wrap { overflow-x: auto; border: 1px solid #26344d; border-radius: 12px; background: #111b30; }
        table { width: 100%; min-width: 900px; border-collapse: collapse; }
        th, td { padding: 14px; text-align: left; border-bottom: 1px solid #26344d; }
        th { color: #7dd3fc; }
        .empty { padding: 18px; background: #17243a; border-radius: 8px; }
        .muted { color: #9ca3af; }
    </style>
</head>
<body>
<header>
    <h2>Reservation Monitoring</h2>
    <a href="${pageContext.request.contextPath}/dashboard">Back to dashboard</a>
</header>
<main>
    <p class="muted">Read-only overview of customer reservations and their statuses.</p>

    <c:choose>
        <c:when test="${empty reservations}">
            <div class="empty">No reservations have been created yet.</div>
        </c:when>
        <c:otherwise>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>Reservation ID</th>
                        <th>Customer ID</th>
                        <th>Slot ID</th>
                        <th>Vehicle ID</th>
                        <th>Start</th>
                        <th>End</th>
                        <th>Amount</th>
                        <th>Status</th>
                        <th>Created</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="r" items="${reservations}">
                        <tr>
                            <td><c:out value="${r.id}"/></td>
                            <td><c:out value="${r.customerId}"/></td>
                            <td><c:out value="${r.parkingSlotId}"/></td>
                            <td><c:out value="${r.vehicleId}"/></td>
                            <td><c:out value="${r.startTime}"/></td>
                            <td><c:out value="${r.endTime}"/></td>
                            <td>₹<c:out value="${r.totalAmount}"/></td>
                            <td><c:out value="${r.status}"/></td>
                            <td><c:out value="${r.createdAt}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>