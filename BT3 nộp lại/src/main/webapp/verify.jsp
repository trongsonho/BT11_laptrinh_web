<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Xác thực OTP</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f6f9; margin: 0; padding: 0; }
        .form-container { width: 400px; margin: 60px auto; padding: 30px; background: #fff; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #333; margin-bottom: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; color: #555; }
        input[type="text"], input[type="email"] { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        button { width: 100%; padding: 10px; background-color: #28a745; border: none; color: #fff; font-size: 16px; border-radius: 4px; cursor: pointer; }
        button:hover { background-color: #218838; }
        .error { color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 4px; margin-bottom: 15px; }
        .info { color: #0c5460; background-color: #d1ecf1; border: 1px solid #bee5eb; padding: 10px; border-radius: 4px; margin-bottom: 15px; }
        .links { text-align: center; margin-top: 15px; }
        .links a { color: #007bff; text-decoration: none; }
    </style>
</head>
<body>
<jsp:include page="/header.jsp" />
<div class="form-container">
    <h2>Xác thực OTP</h2>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <div class="info">
        Mã OTP đã được gửi đến email của bạn. Vui lòng nhập mã OTP để kích hoạt tài khoản (hiệu lực 5 phút).
    </div>

    <form action="${pageContext.request.contextPath}/verify" method="post">
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" value="${not empty email ? email : sessionScope.verifyEmail}" ${not empty email || not empty sessionScope.verifyEmail ? 'readonly' : ''} required>
        </div>
        <div class="form-group">
            <label for="otp">Mã OTP (6 chữ số):</label>
            <input type="text" id="otp" name="otp" maxlength="6" placeholder="Nhập mã OTP..." required autofocus>
        </div>
        <button type="submit">Kích hoạt tài khoản</button>
    </form>

    <div class="links">
        <p><a href="${pageContext.request.contextPath}/login">Quay lại Đăng nhập</a></p>
    </div>
</div>
</body>
</html>
