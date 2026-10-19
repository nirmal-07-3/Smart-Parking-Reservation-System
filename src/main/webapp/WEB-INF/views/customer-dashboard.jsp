<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Dashboard | Smart Parking</title>
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

        .card a {
            display: inline-block;
            background: #2563eb;
            color: white;
            text-decoration: none;
            padding: 11px 15px;
            border-radius: 7px;
        }

        .card a:hover { background: #1d4ed8; }
    </style>
</head>
<body>
<header>
    <h1>Smart Parking | Customer Portal</h1>
    <form class="logout-form"
          action="${pageContext.request.contextPath}/logout"
          method="post">
        <button class="logout-button" type="submit">Logout</button>
    </form>
</header>

<main>
    <section class="welcome">
        <h2>Welcome, <c:out value="${sessionScope.userName}" default="Customer"/>!</h2>
        <p>Manage your vehicles and access your parking reservations.</p>
    </section>

    <section class="grid">
        <article class="card">
            <h3>My Vehicles</h3>
            <p>Add and manage your vehicles for parking reservations.</p>
            <a href="${pageContext.request.contextPath}/vehicles">Manage Vehicles</a>
        </article>

        <article class="card">
            <h3>Reservation History</h3>
            <p>Review your reservations and their current status.</p>
            <a href="${pageContext.request.contextPath}/reservation/history">View History</a>
        </article>


        <article class="card">
            <h3>Find Parking</h3>
            <p>
                Search active parking locations, view available slots,
                choose your registered vehicle, and create a reservation.
            </p>
            <a href="${pageContext.request.contextPath}/reservation/search">
                Search Parking
            </a>
        </article>

    </section>
</main>
</body>
</html>