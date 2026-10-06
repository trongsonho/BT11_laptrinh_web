<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quên mật khẩu</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f6f9; margin: 0; padding: 0; }
        .form-container { width: 400px; margin: 60px auto; padding: 30px; background: #fff; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #333; margin-bottom: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; color: #555; }
        input[type="email"] { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        button { width: 100%; padding: 10px; background-color: #ffc107; border: none; color: #333; font-size: 16px; font-weight: bold; border-radius: 4px; cursor: pointer; }
        button:hover { background-color: #e0a800; }
        .error { color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 4px; margin-bottom: 15px; }
        .links { text-align: center; margin-top: 15px; }
        .links a { color: #007bff; text-decoration: none; }
    </style>
</head>
<body>
<jsp:include page="/header.jsp" />
<div class="form-container">
    <h2>Quên mật khẩu</h2>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/forgot-password" method="post">
        <div class="form-group">
            <label for="email">Nhập Email đã đăng ký:</label>
            <input type="email" id="email" name="email" required autofocus>
        </div>
        <button type="submit">Gửi mã OTP đặt lại mật khẩu</button>
    </form>

    <div class="links">
        <p><a href="${pageContext.request.contextPath}/login">Quay lại Đăng nhập</a></p>
    </div>
</div>
</body>
</html>
