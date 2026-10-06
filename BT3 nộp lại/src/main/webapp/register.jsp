<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    // Dự phòng khi truy cập trực tiếp register.jsp thay vì qua Servlet
    if (request.getAttribute("defaultUsername") == null) {
        long timestampSuffix = System.currentTimeMillis() % 1000000;
        request.setAttribute("defaultUsername", "user" + timestampSuffix);
        int randomDigits = 1000 + new java.util.Random().nextInt(9000);
        request.setAttribute("defaultPassword", "Pass@" + randomDigits);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng ký tài khoản</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f6f9; margin: 0; padding: 0; }
        .form-container { width: 400px; margin: 60px auto; padding: 30px; background: #fff; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #333; margin-bottom: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; color: #555; }
        input[type="text"], input[type="email"], input[type="password"] { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        .hint { font-size: 12px; color: #6c757d; margin-top: 4px; }
        button { width: 100%; padding: 10px; background-color: #007bff; border: none; color: #fff; font-size: 16px; border-radius: 4px; cursor: pointer; }
        button:hover { background-color: #0056b3; }
        .error { color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 4px; margin-bottom: 15px; }
        .links { text-align: center; margin-top: 15px; }
        .links a { color: #007bff; text-decoration: none; }
        .links a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<jsp:include page="/header.jsp" />
<div class="form-container">
    <h2>Đăng ký tài khoản</h2>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="post">
        <div class="form-group">
            <label for="username">Tên đăng nhập:</label>
            <input type="text" id="username" name="username"
                   value="${not empty username ? username : (not empty param.username ? param.username : defaultUsername)}" required>
            <div class="hint">Đã tự động điền (có thể chỉnh sửa nếu muốn)</div>
        </div>

        <div class="form-group">
            <label for="password">Mật khẩu:</label>
            <input type="password" id="password" name="password"
                   value="${not empty password ? password : (not empty param.password ? param.password : defaultPassword)}" required>
            <div class="hint">Đã tự động điền (có thể chỉnh sửa nếu muốn)</div>
        </div>

        <div class="form-group">
            <label for="email">Email (*):</label>
            <input type="email" id="email" name="email"
                   value="${not empty email ? email : param.email}"
                   placeholder="Nhập email của bạn để nhận mã OTP..." required autofocus>
        </div>

        <button type="submit">Đăng ký</button>
    </form>

    <div class="links">
        <p>Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập ngay</a></p>
    </div>
</div>
</body>
</html>
