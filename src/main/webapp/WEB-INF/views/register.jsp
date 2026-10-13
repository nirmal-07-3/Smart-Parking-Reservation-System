<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account | Smart Parking</title>
    <style>
        body{margin:0;font-family:Arial,sans-serif;background:#0b1220;color:#f8fafc;display:grid;place-items:center;min-height:100vh}
        .card{width:min(400px,88%);padding:30px;background:#151f32;border:1px solid #2a3850;border-radius:16px}
        h1{margin-top:0;color:#60a5fa}
        label{display:block;margin:14px 0 6px}
        input,select{box-sizing:border-box;width:100%;padding:12px;border-radius:8px;border:1px solid #3a4a63;background:#0b1220;color:white}
        button{width:100%;padding:12px;margin-top:20px;border:0;border-radius:8px;background:#2563eb;color:white;font-weight:bold;cursor:pointer}
        .error{color:#fca5a5}.link{color:#93c5fd}
    </style>
</head>
<body>
<main class="card">
    <h1>Create Account</h1>
    <p>Join Smart Parking to manage your parking needs.</p>

    <p class="error">${error}</p>

    <form action="${pageContext.request.contextPath}/register" method="post">
        <label for="name">Full name</label>
        <input id="name" name="name" type="text" maxlength="100" required>

        <label for="email">Email address</label>
        <input id="email" name="email" type="email" maxlength="150" required>

        <label for="phone">Phone number</label>
        <input id="phone" name="phone" type="tel" maxlength="15" required>

        <label for="password">Password</label>
        <input id="password" name="password" type="password" minlength="8" required>

        <label for="role">Register as</label>
        <select id="role" name="role" required>
            <option value="CUSTOMER">Customer</option>
            <option value="PROVIDER">Parking Provider</option>
        </select>

        <button type="submit">Create Account</button>
    </form>

    <p>Already registered? <a class="link" href="${pageContext.request.contextPath}/login">Sign in</a></p>
</main>
</body>
</html>