<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile | Smart Parking</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            min-height: 100vh;
            padding: 35px 16px;
            background: #080d18;
            color: #edf2ff;
            font-family: Arial, Helvetica, sans-serif;
        }

        .container {
            width: min(760px, 100%);
            margin: auto;
        }

        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            margin-bottom: 35px;
        }

        .brand {
            font-size: 25px;
            font-weight: 800;
        }

        .brand span {
            color: #4f8cff;
        }

        a {
            color: #a9c7ff;
            text-decoration: none;
        }

        a:hover {
            color: white;
        }

        .hero {
            margin-bottom: 25px;
        }

        .eyebrow {
            display: inline-block;
            margin-bottom: 13px;
            padding: 7px 12px;
            border: 1px solid #294875;
            border-radius: 30px;
            background: #12213a;
            color: #91b7ff;
            font-size: 12px;
            letter-spacing: 1px;
            text-transform: uppercase;
        }

        h1 {
            margin-bottom: 10px;
            font-size: clamp(29px, 5vw, 40px);
        }

        .hero p,
        .muted {
            color: #97a7c4;
            font-size: 14px;
            line-height: 1.7;
        }

        .panel {
            padding: 28px;
            border: 1px solid #25334b;
            border-radius: 20px;
            background: linear-gradient(145deg, #111a2b, #0d1422);
            box-shadow: 0 18px 45px rgba(0, 0, 0, .2);
        }

        .message {
            margin-bottom: 20px;
            padding: 14px 16px;
            border: 1px solid #315b8e;
            border-radius: 10px;
            background: #12213a;
            color: #c6dcff;
            overflow-wrap: anywhere;
        }

        .account {
            display: flex;
            align-items: center;
            gap: 16px;
            padding-bottom: 24px;
            margin-bottom: 24px;
            border-bottom: 1px solid #26334a;
        }

        .avatar {
            display: flex;
            flex-shrink: 0;
            align-items: center;
            justify-content: center;
            width: 58px;
            height: 58px;
            border-radius: 17px;
            background: linear-gradient(135deg, #4f8cff, #2857bb);
            color: white;
            font-size: 25px;
            font-weight: 800;
        }

        .account h2 {
            margin-bottom: 6px;
            font-size: 19px;
            overflow-wrap: anywhere;
        }

        .role {
            display: inline-block;
            margin-top: 9px;
            padding: 5px 9px;
            border-radius: 20px;
            background: #1b2a43;
            color: #a9c7ff;
            font-size: 11px;
            font-weight: 700;
        }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .field {
            min-width: 0;
        }

        .field.full {
            grid-column: 1 / -1;
        }

        label {
            display: block;
            margin-bottom: 9px;
            color: #c8d3e7;
            font-size: 13px;
            font-weight: 600;
        }

        input {
            width: 100%;
            padding: 14px;
            border: 1px solid #2b3953;
            border-radius: 10px;
            outline: none;
            background: #0a1120;
            color: #f1f5ff;
            font-size: 14px;
        }

        input:focus {
            border-color: #4f8cff;
            box-shadow: 0 0 0 3px rgba(79, 140, 255, .12);
        }

        input[readonly] {
            background: #121b2b;
            color: #8d9cb7;
            cursor: not-allowed;
        }

        .help {
            display: block;
            margin-top: 8px;
            color: #7f8eaa;
            font-size: 12px;
        }

        .actions {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            margin-top: 25px;
        }

        button,
        .secondary {
            display: inline-flex;
            justify-content: center;
            align-items: center;
            min-height: 45px;
            padding: 12px 18px;
            border-radius: 10px;
            font-size: 14px;
            font-weight: 700;
            cursor: pointer;
        }

        button {
            border: none;
            background: linear-gradient(135deg, #4f8cff, #2864db);
            color: white;
        }

        button:hover {
            box-shadow: 0 8px 20px rgba(45, 105, 224, .25);
        }

        .secondary {
            border: 1px solid #34415a;
            background: #172136;
            color: #c7d3e9;
        }

        @media (max-width: 540px) {
            body {
                padding: 22px 12px;
            }

            .panel {
                padding: 20px;
            }

            .form-grid {
                grid-template-columns: 1fr;
            }

            .field.full {
                grid-column: auto;
            }

            .brand {
                font-size: 21px;
            }
        }
    </style>
</head>
<body>
<div class="container">

    <header class="topbar">
        <div class="brand">Smart<span>Parking</span></div>
        <a href="${pageContext.request.contextPath}/dashboard">
            &#8592; Dashboard
        </a>
    </header>

    <section class="hero">
        <span class="eyebrow">Account Settings</span>
        <h1>My Profile</h1>
        <p>Keep your personal information accurate and up to date.</p>
    </section>

    <c:if test="${not empty sessionScope.profileMessage}">
        <div class="message" role="status">
            <c:out value="${sessionScope.profileMessage}"/>
        </div>
        <c:remove var="profileMessage" scope="session"/>
    </c:if>

    <section class="panel">

        <div class="account">
            <div class="avatar">
                <c:out value="${empty profileUser.name ? 'U' : profileUser.name.substring(0, 1).toUpperCase()}"/>
            </div>

            <div>
                <h2><c:out value="${profileUser.name}"/></h2>
                <p class="muted"><c:out value="${profileUser.email}"/></p>
                <span class="role"><c:out value="${profileUser.role}"/></span>
            </div>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/profile">
            <div class="form-grid">

                <div class="field full">
                    <label for="name">Full Name</label>
                    <input type="text"
                           id="name"
                           name="name"
                           maxlength="100"
                           value="<c:out value='${profileUser.name}'/>"
                           autocomplete="name"
                           required>
                </div>

                <div class="field full">
                    <label for="email">Email Address</label>
                    <input type="email"
                           id="email"
                           value="<c:out value='${profileUser.email}'/>"
                           readonly>
                    <span class="help">Your registered email cannot be changed here.</span>
                </div>

                <div class="field full">
                    <label for="phone">Phone Number</label>
                    <input type="tel"
                           id="phone"
                           name="phone"
                           maxlength="15"
                           pattern="[0-9+() -]{7,15}"
                           value="<c:out value='${profileUser.phone}'/>"
                           autocomplete="tel"
                           required>
                </div>

            </div>

            <div class="actions">
                <button type="submit">Save Changes</button>
                <a class="secondary"
                   href="${pageContext.request.contextPath}/dashboard">
                    Cancel
                </a>
            </div>
        </form>
    </section>
</div>
</body>
</html>