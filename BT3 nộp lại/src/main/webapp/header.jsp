<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<style>
.site-header {
    background-color: #343a40;
    color: white;
    padding: 15px 30px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-sizing: border-box;
    font-family: Arial, sans-serif;
}
.site-header h1 {
    margin: 0;
    font-size: 24px;
}
.site-header h1 a {
    color: white;
    text-decoration: none;
}
.site-header nav {
    display: flex;
    align-items: center;
    gap: 20px;
}
.site-header nav a {
    color: white;
    text-decoration: none;
    font-weight: bold;
    font-size: 15px;
    transition: color 0.2s;
}
.site-header nav a:hover {
    color: #ffc107;
}
.site-header .user-greeting {
    color: #ffc107;
    font-weight: bold;
    font-size: 15px;
}
</style>

<header class="site-header">
    <h1><a href="${pageContext.request.contextPath}/">MyShop</a></h1>
    <nav>
        <a href="${pageContext.request.contextPath}/">Trang chủ</a>
        <a href="${pageContext.request.contextPath}/product">Sản phẩm</a>

        <c:choose>
            <%-- Case C: Logged-in ADMIN --%>
            <c:when test="${not empty sessionScope.user and (sessionScope.user.role == 'ADMIN' or sessionScope.user.role == 'admin')}">
                <a href="${pageContext.request.contextPath}/admin/products">Admin Sản phẩm</a>
                <a href="${pageContext.request.contextPath}/admin/category/list">Admin Category</a>
                <span class="user-greeting">Chào, ${sessionScope.user.username}</span>
                <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </c:when>

            <%-- Case B: Logged-in NORMAL USER --%>
            <c:when test="${not empty sessionScope.user}">
                <span class="user-greeting">Chào, ${sessionScope.user.username}</span>
                <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </c:when>

            <%-- Case A: NOT LOGGED IN --%>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>