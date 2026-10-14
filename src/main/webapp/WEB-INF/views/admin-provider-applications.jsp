<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Provider Approvals | Smart Parking</title>
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
        textarea { width: 100%; padding: 12px; margin: 8px 0 14px; border-radius: 8px; border: 1px solid #334155; background: #0b1120; color: #fff; }
        select { padding: 11px; border-radius: 8px; border: 1px solid #334155; background: #0b1120; color: #fff; margin: 8px 8px 14px 0; }
        button { border: 0; border-radius: 8px; padding: 11px 16px; background: #0284c7; color: white; cursor: pointer; font-weight: bold; }
        label { display: block; }
    </style>
</head>
<body>
<header>
    <h2>Provider Application Approvals</h2>
    <a href="${pageContext.request.contextPath}/dashboard">Back to dashboard</a>
</header>
<main>
    <p>
        <a href="${pageContext.request.contextPath}/admin/document-review">Review documents</a>
        ·
        <a href="${pageContext.request.contextPath}/admin/parking-approvals">Review parking listings</a>
    </p>

    <c:if test="${not empty sessionScope.adminMessage}">
        <div class="notice"><c:out value="${sessionScope.adminMessage}"/></div>
        <c:remove var="adminMessage" scope="session"/>
    </c:if>

    <h3>Pending applications</h3>
    <c:choose>
        <c:when test="${empty applications}">
            <div class="empty">There are no pending provider applications.</div>
        </c:when>
        <c:otherwise>
            <div class="list">
                <c:forEach var="app" items="${applications}">
                    <section class="card">
                        <h3><c:out value="${app.businessName}"/></h3>
                        <p>Application ID: <c:out value="${app.id}"/></p>
                        <p>Provider ID: <c:out value="${app.providerId}"/></p>
                        <p>Address: <c:out value="${app.address}"/></p>
                        <p>Description: <c:out value="${app.description}"/></p>
                        <p class="muted">Submitted: <c:out value="${app.submittedAt}"/></p>

                        <p>
                            <a href="${pageContext.request.contextPath}/admin/document-review">Review submitted documents before approval</a>
                        </p>

                        <form method="post" action="${pageContext.request.contextPath}/admin/provider-applications">
                            <input type="hidden" name="applicationId" value="${app.id}">

                            <label>Decision</label>
                            <select name="status" required>
                                <option value="APPROVED">Approve provider</option>
                                <option value="REJECTED">Reject provider</option>
                            </select>

                            <label>Review note (required for rejection)</label>
                            <textarea name="reviewNote" rows="3" maxlength="1000"></textarea>

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