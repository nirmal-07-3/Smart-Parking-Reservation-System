
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Find Parking | Smart Parking</title>
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
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            flex-wrap: wrap;
        }

        header h1 {
            margin: 0;
            font-size: 22px;
        }

        header a {
            color: #ffffff;
            text-decoration: none;
        }

        main {
            width: min(850px, 92%);
            margin: 32px auto;
        }

        .panel {
            background: #ffffff;
            padding: 26px;
            border: 1px solid #dbe3ef;
            border-radius: 14px;
            box-shadow: 0 8px 28px rgba(15, 23, 42, 0.06);
            margin-bottom: 22px;
        }

        h2 {
            margin-top: 0;
            color: #0f172a;
        }

        p {
            color: #64748b;
            line-height: 1.6;
        }

        label {
            display: block;
            margin: 18px 0 7px;
            font-weight: 700;
            font-size: 14px;
        }

        input, select {
            width: 100%;
            min-height: 44px;
            padding: 11px 12px;
            border: 1px solid #cbd5e1;
            border-radius: 8px;
            background: #ffffff;
            color: #172033;
            font: inherit;
        }

        input:focus, select:focus {
            outline: 2px solid #93c5fd;
            border-color: #2563eb;
        }

        button, .button {
            display: inline-block;
            margin-top: 20px;
            padding: 12px 18px;
            border: 0;
            border-radius: 8px;
            background: #2563eb;
            color: #ffffff;
            font-weight: 700;
            text-decoration: none;
            cursor: pointer;
        }

        button:hover, .button:hover {
            background: #1d4ed8;
        }

        .error {
            padding: 13px 15px;
            border: 1px solid #fecaca;
            border-radius: 8px;
            background: #fef2f2;
            color: #b91c1c;
            margin-bottom: 18px;
        }

        .notice {
            padding: 13px 15px;
            border-radius: 8px;
            background: #eff6ff;
            color: #1e40af;
            line-height: 1.5;
        }

        .empty {
            padding: 16px;
            border: 1px dashed #cbd5e1;
            border-radius: 8px;
            color: #64748b;
        }

        .footer {
            text-align: center;
            color: #64748b;
            padding: 24px;
            font-size: 13px;
        }

        @media (max-width: 560px) {
            .panel {
                padding: 19px;
            }
        }
    </style>
</head>
<body>
<header>
    <h1>Smart Parking | Find Parking</h1>
    <a href="${pageContext.request.contextPath}/reservation/history">
        My Reservations
    </a>
</header>

<main>
    <section class="panel">
        <h2>Find an Active Parking Location</h2>
        <p>Choose a parking location to view its available slots.</p>

        <c:if test="${not empty errorMessage}">
            <div class="error"><c:out value="${errorMessage}"/></div>
        </c:if>

        <c:choose>
            <c:when test="${empty locations}">
                <div class="empty">
                    No active parking locations are available right now.
                </div>
            </c:when>
            <c:otherwise>
                <form method="get"
                      action="${pageContext.request.contextPath}/reservation/search">
                    <label for="parkingLocationId">Parking Location</label>
                    <select id="parkingLocationId"
                            name="parkingLocationId"
                            required>
                        <option value="">Choose a location</option>
                        <c:forEach var="location" items="${locations}">
                            <option value="${location.id}"
                                    <c:if test="${selectedLocationId eq location.id}">
                                        selected
                                    </c:if>>
                                <c:out value="${location.name}"/> —
                                <c:out value="${location.address}"/>
                            </option>
                        </c:forEach>
                    </select>
                    <button type="submit">Show Available Slots</button>
                </form>
            </c:otherwise>
        </c:choose>
    </section>

    <c:if test="${not empty selectedLocationId}">
        <section class="panel">
            <h2>Create a Reservation</h2>
            <p>
                Select a slot that supports your vehicle type and choose
                your parking start and end times.
            </p>

            <c:choose>
                <c:when test="${empty vehicles}">
                    <div class="empty">
                        You have no registered vehicles.
                        <a href="${pageContext.request.contextPath}/vehicles">
                            Add a vehicle first
                        </a>.
                    </div>
                </c:when>

                <c:when test="${empty slots}">
                    <div class="empty">
                        No available slots were found for this location.
                        Please choose another location or try again later.
                    </div>
                </c:when>

                <c:otherwise>
                    <form method="post"
                          action="${pageContext.request.contextPath}/reservation/create">

                        <input type="hidden"
                               name="parkingLocationId"
                               value="${selectedLocationId}">

                        <label for="parkingSlotId">Available Slot</label>
                        <select id="parkingSlotId"
                                name="parkingSlotId"
                                required>
                            <option value="">Choose a slot</option>
                            <c:forEach var="slot" items="${slots}">
                                <option value="${slot.id}">
                                    Slot <c:out value="${slot.slotNumber}"/> —
                                    Vehicle type: <c:out value="${slot.vehicleType}"/> —
                                    Rate:
                                    ₹<c:out value="${slot.hourlyRate}"/>
                                </option>
                            </c:forEach>
                        </select>

                        <label for="vehicleId">Your Vehicle</label>
                        <select id="vehicleId" name="vehicleId" required>
                            <option value="">Choose your vehicle</option>
                            <c:forEach var="vehicle" items="${vehicles}">
                                <option value="${vehicle.id}">
                                    <c:out value="${vehicle.vehicleNumber}"/> —
                                    <c:out value="${vehicle.vehicleType}"/>
                                </option>
                            </c:forEach>
                        </select>

                        <label for="startTime">Start Time</label>
                        <input id="startTime"
                               name="startTime"
                               type="datetime-local"
                               required>

                        <label for="endTime">End Time</label>
                        <input id="endTime"
                               name="endTime"
                               type="datetime-local"
                               required>

                        <div class="notice" style="margin-top:18px">
                            The final fee is calculated on the server using
                            the configured parking rate. Payment is simulated
                            for this college project; no real money is charged.
                        </div>

                        <button type="submit">Create Reservation</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </section>
    </c:if>
</main>

<footer class="footer">
    Smart Parking Space Reservation and Management System
</footer>

<script>
    const startInput = document.getElementById("startTime");
    const endInput = document.getElementById("endTime");

    function localDateTimeValue(date) {
        const pad = value => String(value).padStart(2, "0");
        return date.getFullYear() + "-" +
            pad(date.getMonth() + 1) + "-" +
            pad(date.getDate()) + "T" +
            pad(date.getHours()) + ":" +
            pad(date.getMinutes());
    }

    if (startInput && endInput) {
        const now = new Date();
        now.setMinutes(now.getMinutes() + 5);
        now.setSeconds(0, 0);

        startInput.min = localDateTimeValue(now);
        endInput.min = startInput.min;

        startInput.addEventListener("change", function () {
            endInput.min = startInput.value;

            if (endInput.value && endInput.value <= startInput.value) {
                endInput.value = "";
            }
        });
    }
</script>
</body>
</html>
