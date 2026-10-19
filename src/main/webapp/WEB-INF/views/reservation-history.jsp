
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Reservations | Smart Parking</title>
    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: #f1f5f9;
            color: #172033;
        }

        header {
            background: #0f172a;
            color: #ffffff;
            padding: 20px 6%;
        }

        header h1 {
            margin: 0;
        }

        main {
            width: min(1050px, 92%);
            margin: 30px auto;
        }

        .panel, .booking {
            background: #ffffff;
            border: 1px solid #dbe3ef;
            border-radius: 12px;
        }

        .panel {
            padding: 24px;
        }

        .booking {
            padding: 20px;
            margin-top: 16px;
        }

        .details {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
            gap: 16px;
        }

        .label {
            color: #64748b;
            font-size: 13px;
        }

        .value {
            margin-top: 5px;
            font-weight: 700;
            overflow-wrap: anywhere;
        }

        .status {
            display: inline-block;
            padding: 6px 10px;
            border-radius: 20px;
            background: #dbeafe;
            color: #1e40af;
            font-size: 12px;
        }

        .actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            margin-top: 18px;
        }

        .button, button {
            display: inline-block;
            border: 0;
            border-radius: 7px;
            padding: 10px 14px;
            font-size: 14px;
            font-weight: 700;
            text-decoration: none;
            cursor: pointer;
        }

        .primary {
            background: #2563eb;
            color: #ffffff;
        }

        .danger {
            background: #dc2626;
            color: #ffffff;
        }

        .secondary {
            background: #e2e8f0;
            color: #172033;
        }

        .error {
            padding: 13px;
            border-radius: 8px;
            background: #fef2f2;
            border: 1px solid #fecaca;
            color: #b91c1c;
            margin-bottom: 16px;
        }

        a {
            color: #1d4ed8;
        }

        .empty {
            padding: 18px;
            background: #f8fafc;
            border-radius: 8px;
            color: #64748b;
        }
    </style>
</head>
<body>
<header>
    <h1>Smart Parking</h1>
    <p>My Reservations</p>
</header>

<main>
    <p>
        <a href="${pageContext.request.contextPath}/home">Home</a>
        |
        <a href="${pageContext.request.contextPath}/reservation/search">
            Find Parking
        </a>
    </p>

    <c:if test="${not empty sessionScope.reservationError}">
        <div class="error">
            <c:out value="${sessionScope.reservationError}"/>
        </div>
        <c:remove var="reservationError" scope="session"/>
    </c:if>

    <section class="panel">
        <h2>Booking History</h2>

        <c:choose>
            <c:when test="${empty reservations}">
                <div class="empty">
                    You have no reservations yet.
                    <a href="${pageContext.request.contextPath}/reservation/search">
                        Find parking
                    </a> to create one.
                </div>
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

                        <c:if test="${reservation.status eq 'PENDING'}">
                            <div class="actions">
                                <a class="button primary"
                                   href="${pageContext.request.contextPath}/reservation/payment?reservationId=${reservation.id}">
                                    Proceed to Payment
                                </a>

                                <form method="post"
                                      action="${pageContext.request.contextPath}/reservation/cancel"
                                      onsubmit="return confirm('Cancel this pending reservation?');">
                                    <input type="hidden"
                                           name="reservationId"
                                           value="${reservation.id}">
                                    <button class="danger" type="submit">
                                        Cancel Reservation
                                    </button>
                                </form>
                            </div>
                        </c:if>
                    </article>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </section>
</main>
</body>
</html>
