
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard | Smart Parking</title>
    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            font-family: Arial, Helvetica, sans-serif;
            background: #0b1120;
            color: #e5edf8;
        }

        .topbar {
            min-height: 72px;
            padding: 16px 5%;
            background: #111b30;
            border-bottom: 1px solid #26344d;
            display: flex;
            align-items: center;
            justify-content: space-between;
            flex-wrap: wrap;
            gap: 16px;
        }

        .brand {
            margin: 0;
            font-size: 22px;
            font-weight: 700;
        }

        .logout-form {
            margin: 0;
        }

        .logout-button {
            padding: 11px 20px;
            border: 1px solid #ef4444;
            border-radius: 8px;
            background: #dc2626;
            color: #ffffff;
            font-size: 14px;
            font-weight: 700;
            cursor: pointer;
        }

        .logout-button:hover {
            background: #b91c1c;
        }

        .container {
            width: min(1150px, 92%);
            margin: 32px auto;
        }

        .welcome {
            padding: 28px;
            background: #111b30;
            border: 1px solid #26344d;
            border-radius: 14px;
        }

        .welcome h2 {
            margin: 0 0 12px;
            font-size: 25px;
        }

        .welcome p {
            margin: 8px 0;
            color: #a5b4cc;
            line-height: 1.6;
        }

        .role {
            display: inline-block;
            margin-top: 8px;
            padding: 6px 10px;
            border-radius: 6px;
            background: #172d4d;
            color: #7dd3fc;
            font-size: 12px;
            font-weight: 700;
        }

        .section-title {
            margin: 30px 0 16px;
            font-size: 21px;
        }

        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 20px;
        }

        .card {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            padding: 24px;
            background: #111b30;
            border: 1px solid #26344d;
            border-radius: 12px;
            transition: transform 0.2s ease, border-color 0.2s ease;
        }

        .card:hover {
            transform: translateY(-3px);
            border-color: #38bdf8;
        }

        .card-icon {
            display: flex;
            align-items: center;
            justify-content: center;
            width: 46px;
            height: 46px;
            margin-bottom: 18px;
            border-radius: 10px;
            background: #172d4d;
            color: #7dd3fc;
            font-size: 22px;
            font-weight: 700;
        }

        .card h3 {
            margin: 0 0 12px;
            font-size: 18px;
        }

        .card p {
            flex: 1;
            margin: 0 0 20px;
            color: #a5b4cc;
            font-size: 14px;
            line-height: 1.6;
        }

        .action-link {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-height: 42px;
            padding: 11px 16px;
            border-radius: 8px;
            background: #0284c7;
            color: #ffffff;
            text-decoration: none;
            font-size: 14px;
            font-weight: 700;
        }

        .action-link:hover {
            background: #0369a1;
        }

        .action-link:focus-visible,
        .logout-button:focus-visible {
            outline: 3px solid #7dd3fc;
            outline-offset: 3px;
        }

        .footer {
            padding: 24px 16px;
            text-align: center;
            color: #94a3b8;
            font-size: 13px;
        }

        @media (max-width: 850px) {
            .dashboard-grid {
                grid-template-columns: repeat(2, minmax(0, 1fr));
            }
        }

        @media (max-width: 520px) {
            .dashboard-grid {
                grid-template-columns: 1fr;
            }

            .topbar {
                padding: 16px 4%;
            }

            .brand {
                font-size: 19px;
            }

            .container {
                margin-top: 22px;
            }

            .welcome {
                padding: 22px;
            }

            .welcome h2 {
                font-size: 22px;
            }
        }
    </style>
</head>
<body>
<header class="topbar">
    <h1 class="brand">Smart Parking | Admin Portal</h1>

    <form class="logout-form"
          action="${pageContext.request.contextPath}/logout"
          method="post">
        <button class="logout-button" type="submit">Logout</button>
    </form>
</header>

<main class="container">
    <section class="welcome">
        <h2>
            Welcome,
            <c:out value="${sessionScope.userName}" default="Admin"/>!
        </h2>
        <p>
            Manage users, review provider verification, approve parking
            facilities, and monitor reservations.
        </p>
        <span class="role">ADMIN ACCOUNT</span>
    </section>

    <h2 class="section-title">Administration Workspace</h2>

    <section class="dashboard-grid">
        <article class="card">
            <div class="card-icon" aria-hidden="true">U</div>
            <h3>User Management</h3>
            <p>
                View registered users and manage eligible customer
                and provider account statuses.
            </p>
            <a class="action-link"
               href="${pageContext.request.contextPath}/admin/users">
                Manage Users
            </a>
        </article>

        <article class="card">
            <div class="card-icon" aria-hidden="true">P</div>
            <h3>Provider Applications</h3>
            <p>
                Review provider business applications and record
                approval or rejection decisions.
            </p>
            <a class="action-link"
               href="${pageContext.request.contextPath}/admin/provider-applications">
                Review Applications
            </a>
        </article>

        <article class="card">
            <div class="card-icon" aria-hidden="true">D</div>
            <h3>Document Verification</h3>
            <p>
                Review submitted verification documents and record
                their review status.
            </p>
            <a class="action-link"
               href="${pageContext.request.contextPath}/admin/document-review">
                Review Documents
            </a>
        </article>

        <article class="card">
            <div class="card-icon" aria-hidden="true">L</div>
            <h3>Parking Approvals</h3>
            <p>
                Review pending parking locations before they become
                available to customers.
            </p>
            <a class="action-link"
               href="${pageContext.request.contextPath}/admin/parking-approvals">
                Review Parking
            </a>
        </article>

        <article class="card">
            <div class="card-icon" aria-hidden="true">R</div>
            <h3>Reservation Management</h3>
            <p>
                View reservation records and monitor their current
                statuses.
            </p>
            <a class="action-link"
               href="${pageContext.request.contextPath}/admin/reservations">
                View Reservations
            </a>
        </article>

        <article class="card">
            <div class="card-icon" aria-hidden="true">A</div>
            <h3>Reports and Analytics</h3>
            <p>
                Review user totals, reservation statistics, payment
                counts, and successful payment amounts.
            </p>
            <a class="action-link"
               href="${pageContext.request.contextPath}/admin/reports">
                View Reports
            </a>
        </article>
    </section>
</main>

<footer class="footer">
    Smart Parking Space Reservation and Management System
</footer>
</body>
</html>
