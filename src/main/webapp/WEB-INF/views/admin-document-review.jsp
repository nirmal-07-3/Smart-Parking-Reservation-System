<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Document Review | Smart Parking</title>
    <style>
        * { box-sizing: border-box; }
        body { margin: 0; font-family: Arial, sans-serif; background: #0b1120; color: #e5e7eb; }
        header { padding: 22px 5%; background: #111b30; border-bottom: 1px solid #26344d; }
        main { max-width: 1150px; margin: 32px auto; padding: 0 20px; }
        a { color: #7dd3fc; text-decoration: none; }
        .notice, .empty { padding: 16px; background: #17243a; border-radius: 10px; margin: 18px 0; }
        .list { display: grid; gap: 18px; }
        .card { background: #111b30; border: 1px solid #26344d; border-radius: 14px; padding: 22px; }
        .muted { color: #9ca3af; overflow-wrap: anywhere; }
        textarea, select { width: 100%; padding: 11px; margin: 8px 0 14px; border-radius: 8px; border: 1px solid #334155; background: #0b1120; color: #fff; }
        button { border: 0; border-radius: 8px; padding: 11px 16px; background: #0284c7; color: white; cursor: pointer; font-weight: bold; }
        label { display: block; color: #cbd5e1; }
    </style>
</head>
<body>
<header>
    <h2>Provider Document Review</h2>
    <a href="${pageContext.request.contextPath}/dashboard">Back to dashboard</a>
</header>
<main>
    <p><a href="${pageContext.request.contextPath}/admin/provider-applications">Provider applications</a></p>

    <c:if test="${not empty sessionScope.adminMessage}">
        <div class="notice"><c:out value="${sessionScope.adminMessage}"/></div>
        <c:remove var="adminMessage" scope="session"/>
    </c:if>

    <h3>Pending documents</h3>
    <c:choose>
        <c:when test="${empty documents}">
            <div class="empty">There are no pending documents to review.</div>
        </c:when>
        <c:otherwise>
            <div class="list">
                <c:forEach var="doc" items="${documents}">
                    <section class="card">
                        <h3><c:out value="${doc.documentType}"/></h3>
                        <p>Document ID: <c:out value="${doc.id}"/></p>
                        <p>Application ID: <c:out value="${doc.applicationId}"/></p>
                        <p>Uploaded: <c:out value="${doc.uploadedAt}"/></p>
                        <p class="muted">File reference: <c:out value="${doc.filePath}"/></p>

                        <form method="post" action="${pageContext.request.contextPath}/admin/document-review">
                            <input type="hidden" name="documentId" value="${doc.id}">

                            <label for="status-${doc.id}">Decision</label>
                            <select id="status-${doc.id}" name="status" required>
                                <option value="APPROVED">Approve document</option>
                                <option value="REJECTED">Reject document</option>
                            </select>

                            <label for="note-${doc.id}">Review note (required for rejection)</label>
                            <textarea id="note-${doc.id}" name="reviewNote" rows="3" maxlength="1000"></textarea>

                            <button type="submit">Save review</button>
                        </form>
                    </section>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>