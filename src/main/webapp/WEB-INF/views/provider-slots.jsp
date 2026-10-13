<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Parking Slots | Smart Parking</title>
    <style>
        *{box-sizing:border-box}body{margin:0;background:#080d18;color:#edf2ff;font-family:Arial,sans-serif}
        .page{width:min(1050px,94%);margin:auto;padding:30px 0 50px}
        header{display:flex;justify-content:space-between;gap:15px;margin-bottom:30px}
        .brand{font-size:24px;font-weight:800}.brand span{color:#4f8cff}
        a{color:#91b7ff;text-decoration:none}a:hover{color:white}
        h1{font-size:clamp(28px,5vw,40px)}h2{font-size:20px;margin-top:0}
        p{color:#97a7c4;line-height:1.7;font-size:14px}
        .grid{display:grid;grid-template-columns:minmax(0,.8fr) minmax(0,1.3fr);gap:22px;margin-top:25px}
        .panel{min-width:0;padding:24px;border:1px solid #25334b;border-radius:18px;background:#10192a}
        label{display:block;margin:15px 0 8px;color:#c8d3e7;font-size:13px}
        input,select{width:100%;padding:13px;border:1px solid #2b3953;border-radius:9px;background:#0a1120;color:white}
        button{margin-top:18px;padding:13px 18px;border:0;border-radius:9px;background:#3478ef;color:white;font-weight:bold;cursor:pointer}
        .message{margin-top:20px;padding:14px;border:1px solid #315b8e;border-radius:10px;background:#12213a;color:#c6dcff;overflow-wrap:anywhere}
        .item{padding:15px 0;border-bottom:1px solid #29354a}.item:last-child{border-bottom:0}
        .status{display:inline-block;padding:6px 9px;border-radius:20px;background:#1b2a43;color:#a9c7ff;font-size:11px;font-weight:bold}
        .table-wrap{overflow-x:auto}table{width:100%;border-collapse:collapse;min-width:450px}
        th,td{padding:13px 10px;text-align:left;border-bottom:1px solid #29354a;font-size:13px}
        th{color:#91a4c5;font-weight:600}td{color:#e1e9f8}
        @media(max-width:760px){.grid{grid-template-columns:1fr}.panel{padding:18px}}
    </style>
</head>
<body>
<div class="page">
    <header>
        <div class="brand">Smart<span>Parking</span></div>
        <a href="${pageContext.request.contextPath}/provider/parkings">← Parking Locations</a>
    </header>

    <h1>Parking Slots</h1>
    <p>Location: <strong><c:out value="${parking.name}"/></strong></p>
    <p>Listing status: <span class="status"><c:out value="${parking.status}"/></span></p>

    <c:if test="${not empty sessionScope.slotMessage}">
        <div class="message" role="status">
            <c:out value="${sessionScope.slotMessage}"/>
        </div>
        <c:remove var="slotMessage" scope="session"/>
    </c:if>

    <main class="grid">
        <section class="panel">
            <h2>Add a Slot</h2>

            <c:choose>
                <c:when test="${parking.status == 'ACTIVE'}">
                    <form method="post" action="${pageContext.request.contextPath}/provider/slots">
                        <input type="hidden" name="parkingLocationId" value="${parking.id}">

                        <label for="slotNumber">Slot Number *</label>
                        <input id="slotNumber" name="slotNumber" maxlength="30"
                               placeholder="e.g. A-01" required>

                        <label for="vehicleType">Vehicle Type *</label>
                        <select id="vehicleType" name="vehicleType" required>
                            <option value="">Select vehicle type</option>
                            <option value="CAR">Car</option>
                            <option value="BIKE">Bike</option>
                            <option value="SCOOTER">Scooter</option>
                            <option value="SUV">SUV</option>
                            <option value="VAN">Van</option>
                            <option value="OTHER">Other</option>
                        </select>

                        <label for="hourlyRate">Slot Hourly Rate (₹)</label>
                        <input id="hourlyRate" name="hourlyRate" type="number"
                               min="0.01" step="0.01"
                               placeholder="Leave blank to use the base rate">

                        <button type="submit">Add Slot</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <p>Slots can only be added after this parking location is approved and active.</p>
                </c:otherwise>
            </c:choose>
        </section>

        <section class="panel">
            <h2>Existing Slots</h2>

            <c:choose>
                <c:when test="${not empty slots}">
                    <div class="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>Slot</th>
                                <th>Vehicle Type</th>
                                <th>Hourly Rate</th>
                                <th>Status</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="slot" items="${slots}">
                                <tr>
                                    <td><c:out value="${slot.slotNumber}"/></td>
                                    <td><c:out value="${slot.vehicleType}"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty slot.hourlyRate}">
                                                ₹<c:out value="${slot.hourlyRate}"/>
                                            </c:when>
                                            <c:otherwise>
                                                ₹<c:out value="${parking.hourlyRate}"/> base rate
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><span class="status"><c:out value="${slot.status}"/></span></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <p>No slots have been added to this location yet.</p>
                </c:otherwise>
            </c:choose>
        </section>
    </main>
</div>
</body>
</html>