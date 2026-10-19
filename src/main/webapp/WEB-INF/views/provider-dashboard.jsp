
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Provider Dashboard | Smart Parking</title>
    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: #f1f5f9;
            color: #1e293b;
        }

        .topbar {
            min-height: 72px;
            padding: 16px 6%;
            background: #0f172a;
            color: #ffffff;
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
            display: inline-flex;
            align-items: center;
            margin: 0;
        }

        .logout-button {
            appearance: none;
            -webkit-appearance: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-height: 42px;
            padding: 11px 20px;
            border: 1px solid #ef4444;
            border-radius: 8px;
            background-color: #dc2626;
            color: #ffffff;
            font-family: Arial, Helvetica, sans-serif;
            font-size: 15px;
            font-weight: 700;
            line-height: 1.2;
            cursor: pointer;
            transition: background-color 0.2s ease;
        }

        .logout-button:hover {
            background-color: #b91c1c;
        }

        .logout-button:focus-visible,
        .action-link:focus-visible {
            outline: 3px solid #60a5fa;
            outline-offset: 3px;
        }

        .container {
            width: min(1100px, 92%);
            margin: 32px auto;
        }

        .welcome {
            padding: 28px;
            background: #ffffff;
            border: 1px solid #e2e8f0;
            border-radius: 14px;
            box-shadow: 0 6px 20px rgba(15, 23, 42, 0.04);
        }

        .welcome h2 {
            margin: 0 0 12px;
            font-size: 25px;
            color: #0f172a;
        }

        .welcome p {
            margin: 8px 0;
            line-height: 1.6;
            color: #475569;
        }

        .role {
            display: inline-block;
            margin-top: 6px;
            padding: 6px 10px;
            border-radius: 6px;
            background: #dbeafe;
            color: #1d4ed8;
            font-size: 12px;
            font-weight: 700;
        }

        .section-title {
            margin: 30px 0 16px;
            font-size: 21px;
            color: #0f172a;
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
            background: #ffffff;
            border: 1px solid #e2e8f0;
            border-radius: 12px;
            transition: transform 0.2s ease, box-shadow 0.2s ease,
            border-color 0.2s ease;
        }

        .card:hover {
            transform: translateY(-3px);
            border-color: #93c5fd;
            box-shadow: 0 8px 22px rgba(15, 23, 42, 0.07);
        }

        .card h3 {
            margin: 0 0 12px;
            font-size: 18px;
            color: #0f172a;
        }

        .card p {
            flex: 1;
            margin: 0 0 20px;
            color: #64748b;
            font-size: 14px;
            line-height: 1.6;
        }

        .card-icon {
            display: flex;
            align-items: center;
            justify-content: center;
            width: 46px;
            height: 46px;
            margin-bottom: 18px;
            border-radius: 10px;
            background: #dbeafe;
            color: #1d4ed8;
            font-size: 23px;
            font-weight: 700;
        }

        .action-link {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-height: 42px;
            padding: 11px 16px;
            border-radius: 8px;
            background: #1d4ed8;
            color: #ffffff;
            text-decoration: none;
            font-size: 14px;
            font-weight: 700;
            transition: background-color 0.2s ease;
        }

        .action-link:hover {
            background: #1e40af;
        }

        .action-link.disabled {
            background: #94a3b8;
            cursor: not-allowed;
        }

        .footer {
            padding: 24px 16px;
            text-align: center;
            color: #64748b;
            font-size: 13px;
        }

        @media (max-width: 760px) {
            .dashboard-grid {
                grid-template-columns: repeat(2, minmax(0, 1fr));
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
        }

        @media (max-width: 520px) {
            .dashboard-grid {
                grid-template-columns: 1fr;
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
    <h1 class="brand">Smart Parking | Provider Portal</h1>

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
            <c:out value="${sessionScope.userName}" default="Provider"/>!
        </h2>

        <p>
            Manage your parking facilities, submit verification documents,
            and prepare your locations for customer reservations.
        </p>

        <span class="role">PROVIDER ACCOUNT</span>
    </section>

    <h2 class="section-title">Provider Workspace</h2>

    <section class="dashboard-grid">
        <article class="card">
            <div class="card-icon" aria-hidden="true">✓</div>
            <h3>Provider Application</h3>
            <p>
                Submit your business details and track your provider
                verification application.
            </p>
            <a class="action-link"
               href="${pageContext.request.contextPath}/provider/applications">
                Manage Application
            </a>
        </article>

        <article class="card">
            <div class="card-icon" aria-hidden="true">⌖</div>
            <h3>Parking Locations</h3>
            <p>
                Create and manage your parking facilities and their
                location information.
            </p>
            <a class="action-link"
               href="${pageContext.request.contextPath}/provider/parkings">
                Manage Locations
            </a>
        </article>

        <article class="card">
            <div class="card-icon" aria-hidden="true">▤</div>
            <h3>Verification Documents</h3>
            <p>
                Upload and manage the documents required for provider
                and parking verification.
            </p>

            <c:choose>
                <c:when test="${not empty sessionScope.lastProviderApplicationId}">
                    <a class="action-link"
                       href="${pageContext.request.contextPath}/provider/documents?applicationId=${sessionScope.lastProviderApplicationId}">
                        Manage Documents
                    </a>
                </c:when>
                <c:otherwise>
                    <a class="action-link"
                       href="${pageContext.request.contextPath}/provider/applications">
                        Start with Application
                    </a>
                </c:otherwise>
            </c:choose>
        </article>

        <article class="card">
            <div class="card-icon" aria-hidden="true">▦</div>
            <h3>Parking Slots</h3>
            <p>
                Configure parking slots and manage the spaces available
                at your parking locations.
            </p>
            <a class="action-link"
               href="${pageContext.request.contextPath}/provider/slots">
                Manage Slots
            </a>
        </article>
    </section>
</main>

<footer class="footer">
    Smart Parking Space Reservation System
</footer>

</body>
</html>
