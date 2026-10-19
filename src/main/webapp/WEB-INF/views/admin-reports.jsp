<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Reports and Analytics | Smart Parking</title>
    <style>
        * { box-sizing: border-box; }
        body { margin: 0; background: #0b1120; color: #e5e7eb; font-family: Arial, sans-serif; }
        header { padding: 22px 5%; background: #111b30; border-bottom: 1px solid #26344d; }
        main { max-width: 1150px; margin: 30px auto; padding: 0 20px; }
        a { color: #7dd3fc; text-decoration: none; }
        .grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 16px; margin: 20px 0 30px; }
        .card { background: #111b30; border: 1px solid #26344d; border-radius: 14px; padding: 22px; min-width: 0; }
        .card p { color: #9ca3af; margin: 0 0 12px; }
        .value { font-size: 28px; font-weight: bold; overflow-wrap: anywhere; }
        h3 { color: #7dd3fc; }
    </style>
</head>
<body>
<header>
    <h2>Reports and Analytics</h2>
    <a href="${pageContext.request.contextPath}/dashboard">Back to dashboard</a>
</header>
<main>
    <h3>User Statistics</h3>
    <section class="grid">
        <div class="card"><p>Total users</p><div class="value">${totalUsers}</div></div>
        <div class="card"><p>Customers</p><div class="value">${customers}</div></div>
        <div class="card"><p>Providers</p><div class="value">${providers}</div></div>
        <div class="card"><p>Active accounts</p><div class="value">${activeUsers}</div></div>
    </section>

    <h3>Reservation Statistics</h3>
    <section class="grid">
        <div class="card"><p>Total reservations</p><div class="value">${totalReservations}</div></div>
        <div class="card"><p>Confirmed</p><div class="value">${confirmedReservations}</div></div>
        <div class="card"><p>Pending</p><div class="value">${pendingReservations}</div></div>
        <div class="card"><p>Cancelled</p><div class="value">${cancelledReservations}</div></div>
    </section>

    <h3>Payment Statistics</h3>
    <section class="grid">
        <div class="card"><p>Total payment records</p><div class="value">${totalPayments}</div></div>
        <div class="card"><p>Successful payments</p><div class="value">${successfulPayments}</div></div>
        <div class="card"><p>Failed payments</p><div class="value">${failedPayments}</div></div>
        <div class="card"><p>Successful payment amount</p><div class="value">₹${successfulPaymentTotal}</div></div>
    </section>
</main>
</body>
</html>