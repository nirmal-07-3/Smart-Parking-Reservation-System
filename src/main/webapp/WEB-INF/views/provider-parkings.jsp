<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Parking | Smart Parking</title>
    <style>
        *{box-sizing:border-box}body{margin:0;background:#080d18;color:#edf2ff;font-family:Arial,sans-serif}
        .page{width:min(1100px,94%);margin:auto;padding:30px 0 50px}
        header{display:flex;justify-content:space-between;gap:15px;margin-bottom:30px}
        .brand{font-size:24px;font-weight:800}.brand span{color:#4f8cff}
        a{color:#91b7ff;text-decoration:none}a:hover{color:white}
        h1{font-size:clamp(28px,5vw,40px)}h2{font-size:20px;margin-top:0}
        p{color:#97a7c4;line-height:1.7;font-size:14px}
        .grid{display:grid;grid-template-columns:minmax(0,.9fr) minmax(0,1.2fr);gap:22px;margin-top:25px}
        .panel{min-width:0;padding:24px;border:1px solid #25334b;border-radius:18px;background:#10192a}
        label{display:block;margin:15px 0 8px;color:#c8d3e7;font-size:13px}
        input,textarea{width:100%;padding:13px;border:1px solid #2b3953;border-radius:9px;background:#0a1120;color:white;font:inherit}
        textarea{min-height:85px;resize:vertical}
        button{margin-top:18px;padding:13px 18px;border:0;border-radius:9px;background:#3478ef;color:white;font-weight:bold;cursor:pointer}
        .message{margin-top:20px;padding:14px;border:1px solid #315b8e;border-radius:10px;background:#12213a;color:#c6dcff;overflow-wrap:anywhere}
        .item{padding:17px 0;border-bottom:1px solid #29354a}.item:last-child{border-bottom:0}
        .status{display:inline-block;padding:6px 9px;border-radius:20px;background:#1b2a43;color:#a9c7ff;font-size:11px;font-weight:bold}
        .actions{display:flex;flex-wrap:wrap;gap:14px;margin-top:12px;font-size:13px}
        @media(max-width:780px){.grid{grid-template-columns:1fr}.panel{padding:18px}}
    </style>
</head>
<body>
<div class="page">
    <header>
        <div class="brand">Smart<span>Parking</span></div>
        <a href="${pageContext.request.contextPath}/dashboard">← Dashboard</a>
    </header>

    <h1>Manage Parking Locations</h1>
    <p>Create parking listings, update their details, and manage their slots after approval.</p>

    <c:if test="${not empty sessionScope.parkingMessage}">
        <div class="message" role="status">
            <c:out value="${sessionScope.parkingMessage}"/>
        </div>
        <c:remove var="parkingMessage" scope="session"/>
    </c:if>

    <main class="grid">
        <section class="panel">
            <h2>
                <c:choose>
                    <c:when test="${not empty editParking}">Edit Parking Location</c:when>
                    <c:otherwise>Create Parking Location</c:otherwise>
                </c:choose>
            </h2>

            <form method="post" action="${pageContext.request.contextPath}/provider/parkings">
                <input type="hidden" name="action"
                       value="${empty editParking ? 'create' : 'update'}">

                <c:if test="${not empty editParking}">
                    <input type="hidden" name="parkingId" value="${editParking.id}">
                </c:if>

                <label for="name">Parking Name *</label>
                <input id="name" name="name" maxlength="150"
                       value="<c:out value='${editParking.name}'/>" required>

                <label for="address">Address *</label>
                <input id="address" name="address" maxlength="255"
                       value="<c:out value='${editParking.address}'/>" required>

                <label for="description">Description</label>
                <textarea id="description" name="description" maxlength="1000"><c:out value="${editParking.description}"/></textarea>

                <label for="hourlyRate">Base Hourly Rate (₹) *</label>
                <input id="hourlyRate" name="hourlyRate" type="number"
                       min="0.01" step="0.01"
                       value="<c:out value='${editParking.hourlyRate}'/>" required>

                <button type="submit">
                    <c:choose>
                        <c:when test="${not empty editParking}">Update Parking</c:when>
                        <c:otherwise>Submit Parking</c:otherwise>
                    </c:choose>
                </button>

                <c:if test="${not empty editParking}">
                    <a href="${pageContext.request.contextPath}/provider/parkings">Cancel edit</a>
                </c:if>
            </form>
        </section>

        <section class="panel">
            <h2>Your Parking Locations</h2>

            <c:choose>
                <c:when test="${not empty parkings}">
                    <c:forEach var="parking" items="${parkings}">
                        <article class="item">
                            <h3><c:out value="${parking.name}"/></h3>
                            <p><c:out value="${parking.address}"/></p>
                            <p>Rate: ₹<c:out value="${parking.hourlyRate}"/> / hour</p>
                            <p>Status: <span class="status"><c:out value="${parking.status}"/></span></p>

                            <c:if test="${not empty parking.description}">
                                <p><c:out value="${parking.description}"/></p>
                            </c:if>

                            <div class="actions">
                                <c:if test="${parking.status != 'SUSPENDED' and parking.status != 'REJECTED'}">
                                    <a href="${pageContext.request.contextPath}/provider/parkings?edit=${parking.id}">Edit Details</a>
                                </c:if>
                                <c:if test="${parking.status == 'ACTIVE'}">
                                    <a href="${pageContext.request.contextPath}/provider/slots?parkingLocationId=${parking.id}">Manage Slots</a>
                                </c:if>
                            </div>
                        </article>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p>You have not submitted a parking location yet.</p>
                </c:otherwise>
            </c:choose>
        </section>
    </main>
</div>
</body>
</html>