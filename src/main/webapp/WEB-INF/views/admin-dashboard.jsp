<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard | Smart Parking</title>
    <style>
        * { box-sizing: border-box; }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f1f5f9;
            color: #1e293b;
        }

        header {
            background: #0f172a;
            color: white;
            padding: 20px 6%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 16px;
        }

        header h1 { margin: 0; font-size: 23px; }

        .logout-form { margin: 0; }

        .logout-button {
            padding: 11px 20px;
            background: #dc2626;
            color: white;
            border: none;
            border-radius: 7px;
            font-size: 15px;
            font-weight: bold;
            cursor: pointer;
        }

        .logout-button:hover { background: #b91c1c; }

        main {
            max-width: 1100px;
            margin: 40px auto;
            padding: 0 20px;
        }

        .welcome, .card {
            background: white;
            padding: 26px;
            border-radius: 12px;
            border: 1px solid #e2e8f0;
        }

        .welcome h2 { margin-top: 0; }

        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(230px, 1fr));
            gap: 20px;
            margin-top: 24px;
        }

        .card p {
            color: #64748b;
            line-height: 1.6;
        }
    </style>
</head>
<body>
<header>
    <h1>Smart Parking | Administration</h1>
    <form class="logout-form"
          action="${pageContext.request.contextPath}/logout"
          method="post">
        <button class="logout-button" type="submit">Logout</button>
    </form>
</header>

<main>
    <section class="welcome">
        <h2>Welcome, <c:out value="${sessionScope.userName}" default="Admin"/>!</h2>
        <p>Admin dashboard for overseeing the Smart Parking system.</p>
        <p>Account role: <strong>ADMIN</strong></p>
    </section>

    <section class="grid">
        <article class="card">
            <h3>Provider Approvals</h3>
            <p>Provider verification and approval workflows are pending integration.</p>
        </article>

        <article class="card">
            <h3>Parking Approvals</h3>
            <p>Parking location review will be available after the admin module is integrated.</p>
        </article>

        <article class="card">
            <h3>Reports</h3>
            <p>System reports and booking statistics are pending integration.</p>
        </article>
    </section>
</main>
</body>
</html>