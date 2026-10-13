
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Reservations | Smart Parking</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f3f6fb;
            color: #172033;
        }
        header {
            background: #10213d;
            color: white;
            padding: 20px 7%;
        }
        main {
            max-width: 1050px;
            margin: 30px auto;
            padding: 0 20px;
        }
        .panel {
            background: white;
            padding: 24px;
            border-radius: 12px;
            box-shadow: 0 4px 18px rgba(16, 33, 61, 0.08);
        }
        .booking {
            padding: 18px 0;
            border-bottom: 1px solid #e1e7f0;
        }
        .booking:last-child {
            border-bottom: 0;
        }
        .details {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(190px, 1fr));
            gap: 14px;
        }
        .label {
            color: #64748b;
            font-size: 13px;
        }
        .value {
            margin-top: 5px;
            font-weight: bold;
            overflow-wrap: anywhere;
        }
        .status {
            display: inline-block;
            padding: 6px 10px;
            background: #eaf2ff;
            border-radius: 20px;
            font-size: 12px;
        }
        a {
            color: #1765d1;
        }
    </style>
</head>
<body>
<header>
    <h1>Smart Parking</h1>
    <p>My Reservations</p>
</header>

<main>
    <p><a href="${pageContext.request.contextPath}/home">Back to Home</a></p>

    <section class="panel">
        <h2>Booking History</h2>

        <c:choose>
            <c:when test="${empty reservations}">
                <p>You have no reservations yet.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="reservation" items="${reservations}">
                    <article class="booking">
                        <div class="details">
                            <div>
                                <div class="label">Reservation ID</div>
                                <div class="value">
                                    <c:out value="${reservation.id}"/>
                                </div>
                            </div>

                            <div>
                                <div class="label">Parking Slot ID</div>
                                <div class="value">
                                    <c:out value="${reservation.parkingSlotId}"/>
                                </div>
                            </div>

                            <div>
                                <div class="label">Vehicle ID</div>
                                <div class="value">
                                    <c:out value="${reservation.vehicleId}"/>
                                </div>
                            </div>

                            <div>
                                <div class="label">Start Time</div>
                                <div class="value">
                                    <c:out value="${reservation.startTime}"/>
                                </div>
                            </div>

                            <div>
                                <div class="label">End Time</div>
                                <div class="value">
                                    <c:out value="${reservation.endTime}"/>
                                </div>
                            </div>

                            <div>
                                <div class="label">Total Amount</div>
                                <div class="value">
                                    ₹<c:out value="${reservation.totalAmount}"/>
                                </div>
                            </div>

                            <div>
                                <div class="label">Status</div>
                                <div class="value">
                                    <span class="status">
                                        <c:out value="${reservation.status}"/>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </article>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </section>
</main>
</body>
</html>
