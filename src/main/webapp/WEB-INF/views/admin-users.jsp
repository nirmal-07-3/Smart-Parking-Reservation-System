<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>User Management | Smart Parking</title>
    <style>
        * { box-sizing: border-box; }
        body { margin: 0; background: #0b1120; color: #e5e7eb; font-family: Arial, sans-serif; }
        header { padding: 22px 5%; background: #111b30; border-bottom: 1px solid #26344d; }
        main { max-width: 1200px; margin: 30px auto; padding: 0 20px; }
        a { color: #7dd3fc; text-decoration: none; }
        .notice { background: #17243a; padding: 14px; border-radius: 8px; margin: 16px 0; }
        .table-wrap { overflow-x: auto; background: #111b30; border-radius: 12px; border: 1px solid #26344d; }
        table { width: 100%; border-collapse: collapse; min-width: 800px; }
        th, td { padding: 14px; text-align: left; border-bottom: 1px solid #26344d; }
        th { color: #7dd3fc; }
        select, button { padding: 9px; border-radius: 7px; border: 1px solid #334155; }
        select { background: #0b1120; color: white; }
        button { background: #0284c7; color: white; cursor: pointer; }
        .muted { color: #9ca3af; }
    </style>
</head>
<body>
<header>
    <h2>User Management</h2>
    <a href="${pageContext.request.contextPath}/dashboard">Back to dashboard</a>
</header>
<main>
    <c:if test="${not empty sessionScope.adminMessage}">
        <div class="notice"><c:out value="${sessionScope.adminMessage}"/></div>
        <c:remove var="adminMessage" scope="session"/>
    </c:if>

    <p class="muted">Review registered customers and providers, and manage account status.</p>
    <div class="table-wrap">
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Role</th>
                <th>Status</th>
                <th>Joined</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="user" items="${users}">
                <tr>
                    <td><c:out value="${user.id}"/></td>
                    <td><c:out value="${user.name}"/></td>
                    <td><c:out value="${user.email}"/></td>
                    <td><c:out value="${user.phone}"/></td>
                    <td><c:out value="${user.role}"/></td>
                    <td><c:out value="${user.status}"/></td>
                    <td><c:out value="${user.createdAt}"/></td>
                    <td>
                        <c:if test="${user.role ne 'ADMIN'}">
                            <form method="post" action="${pageContext.request.contextPath}/admin/users">
                                <input type="hidden" name="userId" value="${user.id}">
                                <select name="status" required>
                                    <option value="ACTIVE" ${user.status eq 'ACTIVE' ? 'selected' : ''}>Active</option>
                                    <option value="BLOCKED" ${user.status eq 'BLOCKED' ? 'selected' : ''}>Blocked</option>
                                </select>
                                <button type="submit">Save</button>
                            </form>
                        </c:if>
                        <c:if test="${user.role eq 'ADMIN'}">
                            <span class="muted">Protected account</span>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</main>
</body>
</html>