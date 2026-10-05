<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang chủ - Cửa hàng</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f8f9fa; color: #333; }
        header { background-color: #343a40; color: white; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        header h1 { margin: 0; font-size: 24px; }
        nav a { color: white; text-decoration: none; margin-left: 20px; font-weight: bold; }
        nav a:hover { color: #ffc107; }
        .container { max-width: 1200px; margin: 30px auto; padding: 0 20px; }
        .section-title { font-size: 24px; margin-bottom: 20px; border-bottom: 2px solid #007bff; padding-bottom: 10px; display: flex; justify-content: space-between; align-items: center; }
        .section-title a { font-size: 16px; color: #007bff; text-decoration: none; }
        .product-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 20px; }
        .product-card { background: white; border: 1px solid #ddd; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 5px rgba(0,0,0,0.05); transition: transform 0.2s, box-shadow 0.2s; display: flex; flex-direction: column; }
        .product-card:hover { transform: translateY(-5px); box-shadow: 0 5px 15px rgba(0,0,0,0.1); }
        .product-image { width: 100%; height: 180px; object-fit: cover; background-color: #eee; }
        .product-info { padding: 15px; flex-grow: 1; display: flex; flex-direction: column; }
        .product-name { font-size: 16px; font-weight: bold; margin: 0 0 10px 0; color: #333; text-decoration: none; }
        .product-name:hover { color: #007bff; }
        .product-price { font-size: 18px; color: #e44d26; font-weight: bold; margin-bottom: 5px; }
        .product-quantity { font-size: 13px; color: #666; margin-bottom: 10px; }
        .product-action { margin-top: auto; }
        .btn-detail { display: block; text-align: center; background-color: #007bff; color: white; padding: 8px 12px; border-radius: 4px; text-decoration: none; font-size: 14px; }
        .btn-detail:hover { background-color: #0056b3; }
        .no-products { text-align: center; color: #777; font-size: 18px; padding: 50px 0; grid-column: 1 / -1; }
    </style>
</head>
<body>

<jsp:include page="/header.jsp" />

<div class="container">
    <div class="section-title">
        <span>10 Sản phẩm mới nhất</span>
        <a href="${pageContext.request.contextPath}/product">Xem tất cả sản phẩm &raquo;</a>
    </div>

    <div class="product-grid">
        <c:choose>
            <c:when test="${not empty products}">
                <c:forEach items="${products}" var="p">
                    <div class="product-card">
                        <c:choose>
                            <c:when test="${not empty p.image}">
                                <c:choose>
                                    <c:when test="${p.image.startsWith('http')}">
                                        <img class="product-image" src="${p.image}" alt="${p.name}">
                                    </c:when>
                                    <c:otherwise>
                                        <img class="product-image" src="${pageContext.request.contextPath}/image?fname=${p.image}" alt="${p.name}">
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <img class="product-image" src="https://via.placeholder.com/220x180?text=No+Image" alt="${p.name}">
                            </c:otherwise>
                        </c:choose>

                        <div class="product-info">
                            <a href="${pageContext.request.contextPath}/product/detail?id=${p.id}" class="product-name">${p.name}</a>
                            <div class="product-price">
                                <fmt:formatNumber value="${p.price}" pattern="#,##0" /> VNĐ
                            </div>
                            <div class="product-quantity">Số lượng: ${p.quantity}</div>
                            <div class="product-action">
                                <a href="${pageContext.request.contextPath}/product/detail?id=${p.id}" class="btn-detail">Xem chi tiết</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="no-products">Hiện chưa có sản phẩm nào.</div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

</body>
</html>
