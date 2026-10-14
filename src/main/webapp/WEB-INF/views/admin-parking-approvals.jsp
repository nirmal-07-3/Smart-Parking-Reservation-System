<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Parking Approvals | Smart Parking</title>
    <style>
        * { box-sizing: border-box; }
        body { margin: 0; font-family: Arial, sans-serif; background: #0b1120; color: #e5e7eb; }
        header { padding: 22px 5%; background: #111b30; border-bottom: 1px solid #26344d; }
        main { max-width: 1150px; margin: 32px auto; padding: 0 20px; }
        a { color: #7dd3fc; text-decoration: none; }
        .notice, .empty { padding: 16px; background: #17243a; border-radius: 10px; margin: 18px 0; }
        .list { display: grid; gap: 18px; }
        .card { background: #111b30; border: 1px solid #26344d; border-radius: 14px; padding: 22px; }
        .muted { color: #9ca3af; }
        select { padding: 11px; border-radius: 8px; border: 1px solid #334155; background: #0b1120; color: #fff; margin: 8px 8px 14px 0; }
        button { border: 0; border-radius: 8px; padding: 11px 16px; background: #0284c7; color: white; cursor: pointer; font-weight: bold; }
    </style>
</head>
<body>
<header>
    <h2>Parking Location Approvals</h2>
    <a href="${pageContext.request.contextPath}/dashboard">Back to dashboard</a>
</header>
<main>
    <p>
        <a href="${pageContext.request.contextPath}/admin/provider-applications">Provider applications</a>
        ·
        <a href="${pageContext.request.contextPath}/admin/document-review">Document review</a>
    </p>

    <c:if test="${not empty sessionScope.adminMessage}">
        <div class="notice"><c:out value="${sessionScope.adminMessage}"/></div>
        <c:remove var="adminMessage" scope="session"/>
    </c:if>

    <h3>Pending parking listings</h3>
    <c:choose>
        <c:when test="${empty parkings}">
            <div class="empty">There are no pending parking locations.</div>
        </c:when>
        <c:otherwise>
            <div class="list">
                <c:forEach var="parking" items="${parkings}">
                    <section class="card">
                        <h3><c:out value="${parking.name}"/></h3>
                        <p>Parking ID: <c:out value="${parking.id}"/></p>
                        <p>Provider ID: <c:out value="${parking.providerId}"/></p>
                        <p>Address: <c:out value="${parking.address}"/></p>
                        <p>Description: <c:out value="${parking.description}"/></p>
                        <p>Hourly rate: ₹<c:out value="${parking.hourlyRate}"/></p>
                        <p class="muted">Created: <c:out value="${parking.createdAt}"/></p>

                        <form method="post" action="${pageContext.request.contextPath}/admin/parking-approvals">
                            <input type="hidden" name="parkingId" value="${parking.id}">
                            <label for="status-${parking.id}">Decision</label>
                            <select id="status-${parking.id}" name="status" required>
                                <option value="ACTIVE">Approve and publish</option>
                                <option value="REJECTED">Reject listing</option>
                            </select>
                            <button type="submit">Save decision</button>
                        </form>
                    </section>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>