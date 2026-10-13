
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Error | Smart Parking</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f3f6fb; color: #172033; margin: 0; }
        main { max-width: 550px; margin: 60px auto; padding: 0 20px; }
        .panel { background: white; padding: 28px; border-radius: 12px; box-shadow: 0 4px 18px rgba(16,33,61,.08); }
        .error { background: #fff0ef; color: #b42318; padding: 12px; border-radius: 7px; }
        a { color: #1765d1; }
    </style>
</head>
<body>
<main>
    <section class="panel">
        <h1>Payment Could Not Be Processed</h1>
        <p class="error"><c:out value="${errorMessage}"/></p>
        <p><a href="${pageContext.request.contextPath}/reservation/history">Return to Booking History</a></p>
    </section>
</main>
</body>
</html>
