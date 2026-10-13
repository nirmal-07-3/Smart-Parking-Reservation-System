<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Provider Applications | Smart Parking</title>
    <style>
        *{box-sizing:border-box}body{margin:0;background:#080d18;color:#edf2ff;font-family:Arial,sans-serif}
        .page{width:min(1050px,94%);margin:auto;padding:30px 0 50px}
        header{display:flex;justify-content:space-between;align-items:center;gap:15px;margin-bottom:30px}
        .brand{font-size:24px;font-weight:800}.brand span{color:#4f8cff}
        a{color:#91b7ff;text-decoration:none}a:hover{color:white}
        h1{font-size:clamp(28px,5vw,40px);margin-bottom:10px}
        .muted{color:#97a7c4;line-height:1.7;font-size:14px}
        .grid{display:grid;grid-template-columns:minmax(0,1fr) minmax(0,1fr);gap:22px;margin-top:25px}
        .panel{min-width:0;padding:24px;border:1px solid #25334b;border-radius:18px;background:#10192a}
        h2{font-size:20px;margin-top:0}
        label{display:block;margin:15px 0 8px;color:#c8d3e7;font-size:13px}
        input,textarea{width:100%;padding:13px;border:1px solid #2b3953;border-radius:9px;background:#0a1120;color:white;font:inherit}
        textarea{min-height:95px;resize:vertical}
        button{margin-top:18px;padding:13px 18px;border:0;border-radius:9px;background:#3478ef;color:white;font-weight:bold;cursor:pointer}
        .message{margin-top:20px;padding:14px;border:1px solid #315b8e;border-radius:10px;background:#12213a;color:#c6dcff;overflow-wrap:anywhere}
        .item{padding:16px 0;border-bottom:1px solid #29354a}.item:last-child{border-bottom:0}
        .status{display:inline-block;margin:8px 0;padding:6px 9px;border-radius:20px;background:#1b2a43;color:#a9c7ff;font-size:11px;font-weight:bold}
        .actions{display:flex;flex-wrap:wrap;gap:14px;margin-top:12px;font-size:13px}
        @media(max-width:720px){.grid{grid-template-columns:1fr}.panel{padding:18px}header{align-items:flex-start}}
    </style>
</head>
<body>
<div class="page">
    <header>
        <div class="brand">Smart<span>Parking</span></div>
        <a href="${pageContext.request.contextPath}/dashboard">← Dashboard</a>
    </header>

    <h1>Provider Applications</h1>
    <p class="muted">Submit your parking business details and track the review status.</p>

    <c:if test="${not empty sessionScope.providerApplicationMessage}">
        <div class="message" role="status">
            <c:out value="${sessionScope.providerApplicationMessage}"/>
        </div>
        <c:remove var="providerApplicationMessage" scope="session"/>
    </c:if>

    <main class="grid">
        <section class="panel">
            <h2>Business Application</h2>
            <p class="muted">Provide accurate business and address information.</p>

            <form method="post" action="${pageContext.request.contextPath}/provider/applications">
                <label for="businessName">Business Name *</label>
                <input id="businessName" name="businessName" maxlength="150" required>

                <label for="address">Business Address *</label>
                <input id="address" name="address" maxlength="255" required>

                <label for="description">Description</label>
                <textarea id="description" name="description" maxlength="1000"></textarea>

                <button type="submit">Submit Application</button>
            </form>
        </section>

        <section class="panel">
            <h2>Your Applications</h2>

            <c:choose>
                <c:when test="${not empty applications}">
                    <c:forEach var="application" items="${applications}">
                        <article class="item">
                            <h3><c:out value="${application.businessName}"/></h3>
                            <p class="muted"><c:out value="${application.address}"/></p>
                            <span class="status"><c:out value="${application.status}"/></span>

                            <c:if test="${not empty application.reviewNote}">
                                <p class="muted">
                                    Review note: <c:out value="${application.reviewNote}"/>
                                </p>
                            </c:if>

                            <div class="actions">
                                <a href="${pageContext.request.contextPath}/provider/documents?applicationId=${application.id}">
                                    Manage Documents
                                </a>
                                <a href="${pageContext.request.contextPath}/provider/parkings">
                                    Manage Parking
                                </a>
                            </div>
                        </article>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="muted">You have not submitted an application yet.</p>
                </c:otherwise>
            </c:choose>
        </section>
    </main>
</div>
</body>
</html>