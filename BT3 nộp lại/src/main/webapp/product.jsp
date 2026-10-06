<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách sản phẩm</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f8f9fa; color: #333; }
        header { background-color: #343a40; color: white; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        header h1 { margin: 0; font-size: 24px; }
        nav a { color: white; text-decoration: none; margin-left: 20px; font-weight: bold; }
        nav a:hover { color: #ffc107; }
        .container { max-width: 1200px; margin: 30px auto; padding: 0 20px; }
        .section-title { font-size: 24px; margin-bottom: 20px; border-bottom: 2px solid #007bff; padding-bottom: 10px; }
        .product-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 25px; }
        .product-card { background: white; border: 1px solid #ddd; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 5px rgba(0,0,0,0.05); transition: transform 0.2s; display: flex; flex-direction: column; }
        .product-card:hover { transform: translateY(-5px); box-shadow: 0 5px 15px rgba(0,0,0,0.1); }
        .product-image { width: 100%; height: 200px; object-fit: cover; background-color: #eee; }
        .product-info { padding: 15px; flex-grow: 1; display: flex; flex-direction: column; }
        .product-name { font-size: 18px; font-weight: bold; margin: 0 0 10px 0; color: #333; text-decoration: none; }
        .product-name:hover { color: #007bff; }
        .product-price { font-size: 20px; color: #e44d26; font-weight: bold; margin-bottom: 5px; }
        .product-quantity { font-size: 14px; color: #666; margin-bottom: 10px; }
        .product-action { margin-top: auto; }
        .btn-detail { display: block; text-align: center; background-color: #007bff; color: white; padding: 10px 15px; border-radius: 4px; text-decoration: none; font-size: 14px; font-weight: bold; }
        .btn-detail:hover { background-color: #0056b3; }
        .pagination { display: flex; justify-content: center; align-items: center; margin: 40px 0; gap: 8px; }
        .pagination a, .pagination span { padding: 8px 16px; border: 1px solid #ddd; border-radius: 4px; text-decoration: none; color: #007bff; font-weight: bold; }
        .pagination a:hover { background-color: #007bff; color: white; }
        .pagination .active { background-color: #007bff; color: white; border-color: #007bff; }
        .pagination .disabled { color: #aaa; pointer-events: none; border-color: #eee; }
        .no-products { text-align: center; color: #777; font-size: 18px; padding: 50px 0; grid-column: 1 / -1; }
    </style>
</head>
<body>

<jsp:include page="/header.jsp" />

<div class="container">
    <div class="section-title">
        Tất cả sản phẩm (Trang ${currentPage} / ${totalPages > 0 ? totalPages : 1})
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
                                <img class="product-image" src="https://via.placeholder.com/280x200?text=No+Image" alt="${p.name}">
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

    <!-- Pagination: 6 sản phẩm/trang -->
    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <c:choose>
                <c:when test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/product?page=${currentPage - 1}">&laquo; Trước</a>
                </c:when>
                <c:otherwise>
                    <span class="disabled">&laquo; Trước</span>
                </c:otherwise>
            </c:choose>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <span class="active">${i}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/product?page=${i}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:choose>
                <c:when test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/product?page=${currentPage + 1}">Sau &raquo;</a>
                </c:when>
                <c:otherwise>
                    <span class="disabled">Sau &raquo;</span>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>
</div>

</body>
</html>
