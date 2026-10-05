<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f6f9; margin: 0; padding: 0; }
        .form-container { width: 400px; margin: 60px auto; padding: 30px; background: #fff; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #333; margin-bottom: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; color: #555; }
        input[type="text"], input[type="password"] { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        button { width: 100%; padding: 10px; background-color: #007bff; border: none; color: #fff; font-size: 16px; border-radius: 4px; cursor: pointer; }
        button:hover { background-color: #0056b3; }
        .error { color: #dc3545; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 4px; margin-bottom: 15px; }
        .success { color: #155724; background-color: #d4edda; border: 1px solid #c3e6cb; padding: 10px; border-radius: 4px; margin-bottom: 15px; }
        .links { text-align: center; margin-top: 15px; }
        .links a { color: #007bff; text-decoration: none; margin: 0 5px; }
        .links a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<jsp:include page="/header.jsp" />
<div class="form-container">
    <h2>Đăng nhập</h2>

    <c:if test="${not empty message}">
        <div class="success">${message}</div>
    </c:if>
    <c:if test="${not empty sessionScope.message}">
        <div class="success">${sessionScope.message}</div>
        <c:remove var="message" scope="session" />
    </c:if>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="form-group">
            <label for="username">Tên đăng nhập:</label>
            <input type="text" id="username" name="username" required autofocus>
        </div>
        <div class="form-group">
            <label for="password">Mật khẩu:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit">Đăng nhập</button>
    </form>

    <div class="links">
        <p>
            <a href="${pageContext.request.contextPath}/register">Đăng ký tài khoản</a> |
            <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
        </p>
        <p><a href="${pageContext.request.contextPath}/">Về trang chủ</a></p>
    </div>
</div>
</body>
</html>
