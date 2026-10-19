<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Change Password | Smart Parking</title>
    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px;
            background: #0b1120;
            color: #e5edf8;
            font-family: Arial, sans-serif;
        }

        .card {
            width: 100%;
            max-width: 460px;
            padding: 32px;
            background: #111b30;
            border: 1px solid #26344d;
            border-radius: 18px;
        }

        h1 {
            margin-top: 0;
        }

        p {
            color: #94a3b8;
            line-height: 1.6;
        }

        label {
            display: block;
            margin: 18px 0 8px;
        }

        input {
            width: 100%;
            padding: 13px;
            border: 1px solid #34445f;
            border-radius: 9px;
            background: #0b1120;
            color: #f8fafc;
        }

        button, .back {
            display: block;
            width: 100%;
            margin-top: 20px;
            padding: 13px;
            border: 0;
            border-radius: 9px;
            text-align: center;
            text-decoration: none;
            font-weight: bold;
            cursor: pointer;
        }

        button {
            background: #0284c7;
            color: white;
        }

        .back {
            background: #1e293b;
            color: #bae6fd;
        }

        .message {
            margin-top: 15px;
            padding: 12px;
            border-radius: 8px;
            overflow-wrap: anywhere;
        }

        .error {
            color: #fecaca;
            background: #451a24;
        }

        .success {
            color: #bbf7d0;
            background: #123426;
        }
    </style>
</head>
<body>
<main class="card">
    <h1>Change Password</h1>
    <p>Update your password to help protect your account.</p>

    <c:if test="${not empty requestScope.errorMessage}">
        <div class="message error" role="alert">
            <c:out value="${requestScope.errorMessage}"/>
        </div>
    </c:if>

    <c:if test="${not empty sessionScope.passwordMessage}">
        <div class="message success" role="status">
            <c:out value="${sessionScope.passwordMessage}"/>
        </div>
        <c:remove var="passwordMessage" scope="session"/>
    </c:if>

    <form method="post"
          action="${pageContext.request.contextPath}/change-password">

        <label for="currentPassword">Current Password</label>
        <input type="password" id="currentPassword"
               name="currentPassword"
               autocomplete="current-password" required>

        <label for="newPassword">New Password</label>
        <input type="password" id="newPassword"
               name="newPassword"
               minlength="8"
               autocomplete="new-password" required>

        <label for="confirmPassword">Confirm New Password</label>
        <input type="password" id="confirmPassword"
               name="confirmPassword"
               minlength="8"
               autocomplete="new-password" required>

        <button type="submit">Update Password</button>
    </form>

    <a class="back"
       href="${pageContext.request.contextPath}/dashboard">
        Back to Dashboard
    </a>
</main>
</body>
</html>