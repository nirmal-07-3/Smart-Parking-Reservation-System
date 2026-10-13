<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Vehicles | Smart Parking</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            min-height: 100vh;
            font-family: Arial, Helvetica, sans-serif;
            background: #080d18;
            color: #edf2ff;
        }

        .page {
            width: min(1150px, 92%);
            margin: 0 auto;
            padding: 35px 0 55px;
        }

        .topbar {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 20px;
            margin-bottom: 35px;
        }

        .brand {
            color: #ffffff;
            font-size: 25px;
            font-weight: 800;
            letter-spacing: -0.5px;
        }

        .brand span {
            color: #4f8cff;
        }

        .back-link {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            color: #b9c8e8;
            text-decoration: none;
            font-size: 14px;
            transition: color 0.2s ease;
        }

        .back-link:hover {
            color: #ffffff;
        }

        .hero {
            margin-bottom: 28px;
        }

        .eyebrow {
            display: inline-block;
            margin-bottom: 12px;
            padding: 7px 12px;
            border: 1px solid rgba(79, 140, 255, 0.3);
            border-radius: 30px;
            background: rgba(79, 140, 255, 0.1);
            color: #80adff;
            font-size: 12px;
            font-weight: 700;
            letter-spacing: 1px;
            text-transform: uppercase;
        }

        .hero h1 {
            margin-bottom: 12px;
            font-size: clamp(28px, 5vw, 42px);
            line-height: 1.2;
            letter-spacing: -1px;
        }

        .hero p {
            max-width: 650px;
            color: #9baac7;
            font-size: 15px;
            line-height: 1.7;
        }

        .message {
            margin-bottom: 22px;
            padding: 15px 18px;
            border: 1px solid #315b8e;
            border-radius: 12px;
            background: rgba(49, 91, 142, 0.16);
            color: #c6dcff;
            line-height: 1.5;
            overflow-wrap: anywhere;
        }

        .content-grid {
            display: grid;
            grid-template-columns: minmax(300px, 0.85fr) minmax(0, 1.4fr);
            align-items: start;
            gap: 25px;
        }

        .panel {
            min-width: 0;
            padding: 25px;
            border: 1px solid #202c43;
            border-radius: 20px;
            background: linear-gradient(145deg, #111a2b, #0d1422);
            box-shadow: 0 15px 40px rgba(0, 0, 0, 0.18);
        }

        .panel-header {
            margin-bottom: 23px;
        }

        .panel-header h2 {
            margin-bottom: 8px;
            font-size: 21px;
            letter-spacing: -0.3px;
        }

        .panel-header p {
            color: #8f9fbc;
            font-size: 13px;
            line-height: 1.6;
        }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 17px;
        }

        .field {
            min-width: 0;
        }

        .field.full {
            grid-column: 1 / -1;
        }

        .field label {
            display: block;
            margin-bottom: 8px;
            color: #c4cee1;
            font-size: 13px;
            font-weight: 600;
        }

        input,
        select {
            width: 100%;
            min-width: 0;
            padding: 13px 14px;
            border: 1px solid #2b3852;
            border-radius: 10px;
            outline: none;
            background: #0a1120;
            color: #f1f5ff;
            font-family: inherit;
            font-size: 14px;
            transition: border-color 0.2s ease, box-shadow 0.2s ease;
        }

        input::placeholder {
            color: #65748e;
        }

        select {
            cursor: pointer;
        }

        input:focus,
        select:focus {
            border-color: #4f8cff;
            box-shadow: 0 0 0 3px rgba(79, 140, 255, 0.12);
        }

        .form-actions {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            gap: 12px;
            margin-top: 22px;
        }

        .primary-button {
            display: inline-flex;
            justify-content: center;
            align-items: center;
            min-height: 44px;
            padding: 12px 19px;
            border: none;
            border-radius: 10px;
            background: linear-gradient(135deg, #4f8cff, #2864db);
            color: #ffffff;
            font-size: 14px;
            font-weight: 700;
            text-decoration: none;
            cursor: pointer;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .primary-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 22px rgba(45, 105, 224, 0.3);
        }

        .secondary-button {
            display: inline-flex;
            justify-content: center;
            align-items: center;
            min-height: 42px;
            padding: 11px 15px;
            border: 1px solid #34415a;
            border-radius: 10px;
            background: #172136;
            color: #c7d3e9;
            font-size: 13px;
            font-weight: 600;
            text-decoration: none;
            cursor: pointer;
            transition: background 0.2s ease;
        }

        .secondary-button:hover {
            background: #202d45;
        }

        .vehicle-list {
            display: grid;
            gap: 16px;
        }

        .vehicle-card {
            padding: 20px;
            border: 1px solid #26344d;
            border-radius: 15px;
            background: rgba(8, 15, 29, 0.65);
            transition: border-color 0.2s ease, transform 0.2s ease;
        }

        .vehicle-card:hover {
            transform: translateY(-2px);
            border-color: #42649a;
        }

        .vehicle-card-top {
            display: flex;
            align-items: flex-start;
            justify-content: space-between;
            gap: 12px;
            margin-bottom: 18px;
        }

        .vehicle-title {
            min-width: 0;
        }

        .vehicle-title h3 {
            margin-bottom: 7px;
            color: #f2f6ff;
            font-size: 18px;
            overflow-wrap: anywhere;
        }

        .vehicle-title p {
            color: #8e9db7;
            font-size: 13px;
        }

        .vehicle-type {
            flex-shrink: 0;
            padding: 7px 10px;
            border: 1px solid rgba(79, 140, 255, 0.25);
            border-radius: 8px;
            background: rgba(79, 140, 255, 0.1);
            color: #91b7ff;
            font-size: 11px;
            font-weight: 700;
            text-transform: uppercase;
        }

        .vehicle-details {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 15px;
            padding: 16px 0;
            border-top: 1px solid #202b40;
            border-bottom: 1px solid #202b40;
        }

        .detail-label {
            display: block;
            margin-bottom: 6px;
            color: #7f8eaa;
            font-size: 11px;
            text-transform: uppercase;
            letter-spacing: 0.7px;
        }

        .detail-value {
            color: #dce5f7;
            font-size: 13px;
            overflow-wrap: anywhere;
        }

        .vehicle-actions {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            gap: 10px;
            margin-top: 17px;
        }

        .edit-link {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-height: 37px;
            padding: 9px 14px;
            border: 1px solid #34558a;
            border-radius: 9px;
            background: rgba(79, 140, 255, 0.1);
            color: #a9c7ff;
            font-size: 13px;
            font-weight: 600;
            text-decoration: none;
            transition: background 0.2s ease;
        }

        .edit-link:hover {
            background: rgba(79, 140, 255, 0.2);
        }

        .delete-form {
            margin-left: auto;
        }

        .delete-button {
            min-height: 37px;
            padding: 9px 14px;
            border: 1px solid #733541;
            border-radius: 9px;
            background: rgba(239, 68, 68, 0.08);
            color: #ff9eaa;
            font-family: inherit;
            font-size: 13px;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.2s ease;
        }

        .delete-button:hover {
            background: rgba(239, 68, 68, 0.18);
        }

        .empty-state {
            padding: 35px 20px;
            border: 1px dashed #34415a;
            border-radius: 14px;
            text-align: center;
        }

        .empty-icon {
            display: flex;
            align-items: center;
            justify-content: center;
            width: 58px;
            height: 58px;
            margin: 0 auto 16px;
            border-radius: 16px;
            background: #172640;
            color: #80adff;
            font-size: 25px;
        }

        .empty-state h3 {
            margin-bottom: 9px;
            font-size: 17px;
        }

        .empty-state p {
            color: #8f9fbc;
            font-size: 13px;
            line-height: 1.7;
        }

        .list-heading {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 12px;
            margin-bottom: 22px;
        }

        .count {
            flex-shrink: 0;
            padding: 6px 10px;
            border-radius: 20px;
            background: #1a2a45;
            color: #a9c7ff;
            font-size: 12px;
            font-weight: 700;
        }

        .footer-note {
            margin-top: 28px;
            color: #7585a1;
            font-size: 12px;
            line-height: 1.7;
            text-align: center;
        }

        @media (max-width: 850px) {
            .content-grid {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 520px) {
            .page {
                width: 94%;
                padding-top: 22px;
            }

            .topbar {
                margin-bottom: 28px;
            }

            .brand {
                font-size: 21px;
            }

            .panel {
                padding: 18px;
                border-radius: 15px;
            }

            .form-grid {
                grid-template-columns: 1fr;
            }

            .field.full {
                grid-column: auto;
            }

            .vehicle-card {
                padding: 16px;
            }

            .vehicle-card-top {
                flex-direction: column;
            }

            .delete-form {
                margin-left: 0;
            }
        }
    </style>
</head>

<body>
<div class="page">

    <header class="topbar">
        <div class="brand">Smart<span>Parking</span></div>
        <a class="back-link"
           href="${pageContext.request.contextPath}/dashboard">
            &#8592; Back to Dashboard
        </a>
    </header>

    <section class="hero">
        <span class="eyebrow">Customer Workspace</span>
        <h1>My Vehicles</h1>
        <p>
            Manage your registered vehicles in one place. Keep your vehicle
            information updated to make your parking reservation process easier.
        </p>
    </section>

    <c:if test="${not empty sessionScope.vehicleMessage}">
        <div class="message" role="status">
            <c:out value="${sessionScope.vehicleMessage}"/>
        </div>
        <c:remove var="vehicleMessage" scope="session"/>
    </c:if>

    <main class="content-grid">

        <section class="panel">
            <div class="panel-header">
                <h2>
                    <c:choose>
                        <c:when test="${not empty editVehicle}">
                            Edit Vehicle
                        </c:when>
                        <c:otherwise>
                            Add a Vehicle
                        </c:otherwise>
                    </c:choose>
                </h2>
                <p>
                    <c:choose>
                        <c:when test="${not empty editVehicle}">
                            Update the information for your selected vehicle.
                        </c:when>
                        <c:otherwise>
                            Enter your vehicle details to register it to your account.
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>

            <form method="post"
                  action="${pageContext.request.contextPath}/vehicles">

                <input type="hidden" name="action"
                       value="${empty editVehicle ? 'add' : 'update'}">

                <c:if test="${not empty editVehicle}">
                    <input type="hidden" name="vehicleId"
                           value="<c:out value='${editVehicle.id}'/>">
                </c:if>

                <div class="form-grid">

                    <div class="field full">
                        <label for="vehicleNumber">Registration Number *</label>
                        <input type="text"
                               id="vehicleNumber"
                               name="vehicleNumber"
                               placeholder="e.g. OD02AB1234"
                               maxlength="20"
                               autocomplete="off"
                               value="<c:out value='${editVehicle.vehicleNumber}'/>"
                               required>
                    </div>

                    <div class="field full">
                        <label for="vehicleType">Vehicle Type *</label>
                        <select id="vehicleType" name="vehicleType" required>
                            <option value="">Select vehicle type</option>
                            <option value="CAR" ${editVehicle.vehicleType == 'CAR' ? 'selected' : ''}>Car</option>
                            <option value="BIKE" ${editVehicle.vehicleType == 'BIKE' ? 'selected' : ''}>Bike</option>
                            <option value="SCOOTER" ${editVehicle.vehicleType == 'SCOOTER' ? 'selected' : ''}>Scooter</option>
                            <option value="SUV" ${editVehicle.vehicleType == 'SUV' ? 'selected' : ''}>SUV</option>
                            <option value="VAN" ${editVehicle.vehicleType == 'VAN' ? 'selected' : ''}>Van</option>
                            <option value="OTHER" ${editVehicle.vehicleType == 'OTHER' ? 'selected' : ''}>Other</option>
                        </select>
                    </div>

                    <div class="field">
                        <label for="brand">Brand</label>
                        <input type="text"
                               id="brand"
                               name="brand"
                               placeholder="e.g. Honda"
                               maxlength="60"
                               value="<c:out value='${editVehicle.brand}'/>">
                    </div>

                    <div class="field">
                        <label for="model">Model</label>
                        <input type="text"
                               id="model"
                               name="model"
                               placeholder="e.g. City"
                               maxlength="60"
                               value="<c:out value='${editVehicle.model}'/>">
                    </div>

                    <div class="field full">
                        <label for="color">Color</label>
                        <input type="text"
                               id="color"
                               name="color"
                               placeholder="e.g. White"
                               maxlength="30"
                               value="<c:out value='${editVehicle.color}'/>">
                    </div>

                </div>

                <div class="form-actions">
                    <button class="primary-button" type="submit">
                        <c:choose>
                            <c:when test="${not empty editVehicle}">
                                Update Vehicle
                            </c:when>
                            <c:otherwise>
                                Add Vehicle
                            </c:otherwise>
                        </c:choose>
                    </button>

                    <c:if test="${not empty editVehicle}">
                        <a class="secondary-button"
                           href="${pageContext.request.contextPath}/vehicles">
                            Cancel Edit
                        </a>
                    </c:if>
                </div>
            </form>
        </section>

        <section class="panel">
            <div class="list-heading">
                <div class="panel-header" style="margin-bottom: 0;">
                    <h2>Registered Vehicles</h2>
                    <p>Your vehicles associated with this account.</p>
                </div>

                <span class="count">
                    <c:out value="${vehicles.size()}"/> vehicle(s)
                </span>
            </div>

            <c:choose>
                <c:when test="${not empty vehicles}">
                    <div class="vehicle-list">

                        <c:forEach var="vehicle" items="${vehicles}">
                            <article class="vehicle-card">

                                <div class="vehicle-card-top">
                                    <div class="vehicle-title">
                                        <h3>
                                            <c:out value="${vehicle.vehicleNumber}"/>
                                        </h3>
                                        <p>
                                            <c:choose>
                                                <c:when test="${not empty vehicle.brand and not empty vehicle.model}">
                                                    <c:out value="${vehicle.brand}"/>
                                                    <c:out value="${vehicle.model}"/>
                                                </c:when>
                                                <c:when test="${not empty vehicle.brand}">
                                                    <c:out value="${vehicle.brand}"/>
                                                </c:when>
                                                <c:when test="${not empty vehicle.model}">
                                                    <c:out value="${vehicle.model}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    Vehicle details
                                                </c:otherwise>
                                            </c:choose>
                                        </p>
                                    </div>

                                    <span class="vehicle-type">
                                        <c:out value="${vehicle.vehicleType}"/>
                                    </span>
                                </div>

                                <div class="vehicle-details">
                                    <div>
                                        <span class="detail-label">Brand</span>
                                        <span class="detail-value">
                                            <c:choose>
                                                <c:when test="${not empty vehicle.brand}">
                                                    <c:out value="${vehicle.brand}"/>
                                                </c:when>
                                                <c:otherwise>Not specified</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>

                                    <div>
                                        <span class="detail-label">Model</span>
                                        <span class="detail-value">
                                            <c:choose>
                                                <c:when test="${not empty vehicle.model}">
                                                    <c:out value="${vehicle.model}"/>
                                                </c:when>
                                                <c:otherwise>Not specified</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>

                                    <div>
                                        <span class="detail-label">Color</span>
                                        <span class="detail-value">
                                            <c:choose>
                                                <c:when test="${not empty vehicle.color}">
                                                    <c:out value="${vehicle.color}"/>
                                                </c:when>
                                                <c:otherwise>Not specified</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>

                                    <div>
                                        <span class="detail-label">Vehicle ID</span>
                                        <span class="detail-value">
                                            #<c:out value="${vehicle.id}"/>
                                        </span>
                                    </div>
                                </div>

                                <div class="vehicle-actions">
                                    <a class="edit-link"
                                       href="${pageContext.request.contextPath}/vehicles?action=edit&amp;vehicleId=${vehicle.id}">
                                        Edit
                                    </a>

                                    <form class="delete-form"
                                          method="post"
                                          action="${pageContext.request.contextPath}/vehicles"
                                          onsubmit="return confirm('Are you sure you want to delete this vehicle?');">

                                        <input type="hidden"
                                               name="action"
                                               value="delete">

                                        <input type="hidden"
                                               name="vehicleId"
                                               value="<c:out value='${vehicle.id}'/>">

                                        <button class="delete-button" type="submit">
                                            Delete
                                        </button>
                                    </form>
                                </div>

                            </article>
                        </c:forEach>

                    </div>
                </c:when>

                <c:otherwise>
                    <div class="empty-state">
                        <div class="empty-icon">&#128663;</div>
                        <h3>No vehicles registered yet</h3>
                        <p>
                            Add your first vehicle using the form.
                            Your registered vehicles will appear here.
                        </p>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>

    </main>

    <p class="footer-note">
        Smart Parking Reservation &amp; Management System
    </p>

</div>
</body>
</html>