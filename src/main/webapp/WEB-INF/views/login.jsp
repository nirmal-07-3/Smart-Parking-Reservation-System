<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | Smart Parking</title>
    <style>
        body{margin:0;font-family:Arial,sans-serif;background:#0b1220;color:#f8fafc;display:grid;place-items:center;min-height:100vh}
        .card{width:min(380px,88%);padding:30px;background:#151f32;border:1px solid #2a3850;border-radius:16px}
        h1{margin-top:0;color:#60a5fa}
        label{display:block;margin:16px 0 6px}
        input{box-sizing:border-box;width:100%;padding:12px;border-radius:8px;border:1px solid #3a4a63;background:#0b1220;color:white}
        button{width:100%;padding:12px;margin-top:20px;border:0;border-radius:8px;background:#2563eb;color:white;font-weight:bold;cursor:pointer}
        .error{color:#fca5a5}.success{color:#86efac}.link{color:#93c5fd}
    </style>
</head>
<body>
<main class="card">
    <h1>Welcome Back</h1>
    <p>Sign in to Smart Parking.</p>

    <p class="error">${error}</p>

    <p class="success">
        ${param.registered == '1' ? 'Registration successful. Please sign in.' : ''}
    </p>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <label for="email">Email address</label>
        <input id="email" name="email" type="email" maxlength="150" required>

        <label for="password">Password</label>
        <input id="password" name="password" type="password" required>

        <button type="submit">Sign In</button>
    </form>

    <p>New here? <a class="link" href="${pageContext.request.contextPath}/register">Create an account</a></p>
</main>
</body>
</html>