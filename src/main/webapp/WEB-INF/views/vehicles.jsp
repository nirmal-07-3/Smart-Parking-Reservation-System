<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Vehicles | Smart Parking</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f3f6fb;
            margin: 0;
            color: #172033;
        }
        header {
            background: #10213d;
            color: white;
            padding: 20px 7%;
        }
        main {
            max-width: 1000px;
            margin: 30px auto;
            padding: 0 20px;
        }
        .panel {
            background: white;
            padding: 24px;
            border-radius: 12px;
            margin-bottom: 24px;
            box-shadow: 0 4px 18px rgba(16, 33, 61, 0.08);
        }
        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
            gap: 14px;
        }
        input, select, button {
            box-sizing: border-box;
            width: 100%;
            padding: 11px;
            border-radius: 7px;
            border: 1px solid #ccd5e2;
            font-size: 14px;
        }
        button {
            border: 0;
            background: #1765d1;
            color: white;
            font-weight: bold;
            cursor: pointer;
        }
        .delete-button {
            background: #b42318;
        }
        .message {
            background: #eaf2ff;
            padding: 12px;
            border-radius: 7px;
            margin-bottom: 18px;
        }
        .vehicle {
            border: 1px solid #e1e7f0;
            border-radius: 9px;
            padding: 16px;
            margin-top: 12px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 18px;
        }
        .vehicle form {
            min-width: 100px;
        }
        .vehicle p {
            margin: 7px 0;
            color: #586579;
        }
        a {
            color: #1765d1;
        }
        @media (max-width: 550px) {
            .vehicle {
                align-items: stretch;
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
<header>
    <h1>Smart Parking</h1>
    <p>Manage your registered vehicles</p>
</header>

<main>
    <p><a href="${pageContext.request.contextPath}/home">Back to Home</a></p>

    <c:if test="${not empty sessionScope.vehicleMessage}">
        <div class="message">
            <c:out value="${sessionScope.vehicleMessage}"/>
        </div>
        <c:remove var="vehicleMessage" scope="session"/>
    </c:if>

    <section class="panel">
        <h2>Add a Vehicle</h2>

        <form method="post" action="${pageContext.request.contextPath}/vehicles">
            <div class="form-grid">
                <input type="text" name="vehicleNumber"
                       placeholder="Registration number" maxlength="20" required>

                <select name="vehicleType" required>
                    <option value="">Select vehicle type</option>
                    <option value="CAR">Car</option>
                    <option value="BIKE">Bike</option>
                    <option value="SCOOTER">Scooter</option>
                    <option value="SUV">SUV</option>
                    <option value="VAN">Van</option>
                    <option value="OTHER">Other</option>
                </select>

                <input type="text" name="brand" placeholder="Brand" maxlength="60">
                <input type="text" name="model" placeholder="Model" maxlength="60">
                <input type="text" name="color" placeholder="Color" maxlength="30">
            </div>

            <p><button type="submit">Add Vehicle</button></p>
        </form>
    </section>

    <section class="panel">
        <h2>My Vehicles</h2>

        <c:choose>
            <c:when test="${empty vehicles}">
                <p>You have not registered any vehicles yet.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="vehicle" items="${vehicles}">
                    <div class="vehicle">
                        <div>
                            <strong><c:out value="${vehicle.vehicleNumber}"/></strong>
                            <p>
                                Type: <c:out value="${vehicle.vehicleType}"/>
                            </p>
                            <p>
                                Brand: <c:out value="${vehicle.brand}"/>
                                | Model: <c:out value="${vehicle.model}"/>
                                | Color: <c:out value="${vehicle.color}"/>
                            </p>
                        </div>

                        <form method="post"
                              action="${pageContext.request.contextPath}/vehicles"
                              onsubmit="return confirm('Delete this vehicle?');">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="vehicleId" value="${vehicle.id}">
                            <button class="delete-button" type="submit">Delete</button>
                        </form>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </section>
</main>
</body>
</html>