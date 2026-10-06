<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý sản phẩm</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f8f9fa; }
        .main-container { padding: 20px; }
        .header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .btn { padding: 8px 16px; border-radius: 4px; text-decoration: none; font-weight: bold; cursor: pointer; display: inline-block; }
        .btn-primary { background-color: #007bff; color: white; border: none; }
        .btn-primary:hover { background-color: #0056b3; }
        .btn-secondary { background-color: #6c757d; color: white; border: none; }
        .btn-secondary:hover { background-color: #5a6268; }
        .btn-danger { background-color: #dc3545; color: white; border: none; }
        .btn-danger:hover { background-color: #bd2130; }
        .btn-edit { background-color: #28a745; color: white; border: none; }
        .btn-edit:hover { background-color: #218838; }
        table { width: 100%; border-collapse: collapse; background: white; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        th, td { border: 1px solid #dee2e6; padding: 12px; text-align: left; }
        th { background-color: #e9ecef; }
        tr:hover { background-color: #f1f1f1; }
        .img-thumb { width: 60px; height: 60px; object-fit: cover; border-radius: 4px; }
    </style>
</head>
<body>

<jsp:include page="/header.jsp" />

<div class="main-container">
<div class="header-actions">
    <h2>Danh sách sản phẩm</h2>
    <div>
        <a href="${pageContext.request.contextPath}/admin/category/list" class="btn btn-secondary">Quản lý Category</a>
        <a href="${pageContext.request.contextPath}/admin/products/add" class="btn btn-primary">+ Thêm sản phẩm</a>
        <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Về trang chủ</a>
    </div>
</div>

<table>
    <thead>
        <tr>
            <th>STT</th>
            <th>ID</th>
            <th>Hình ảnh</th>
            <th>Tên sản phẩm</th>
            <th>Giá</th>
            <th>Số lượng</th>
            <th>Danh mục</th>
            <th>Ngày tạo</th>
            <th>Thao tác</th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${not empty products}">
                <c:forEach items="${products}" var="p" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>${p.id}</td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty p.image}">
                                    <c:choose>
                                        <c:when test="${p.image.startsWith('http')}">
                                            <img class="img-thumb" src="${p.image}" alt="${p.name}">
                                        </c:when>
                                        <c:otherwise>
                                            <img class="img-thumb" src="${pageContext.request.contextPath}/image?fname=${p.image}" alt="${p.name}">
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <span>Không ảnh</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><strong>${p.name}</strong></td>
                        <td><fmt:formatNumber value="${p.price}" pattern="#,##0" /> VNĐ</td>
                        <td>${p.quantity}</td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty p.category}">
                                    ${p.category.name}
                                </c:when>
                                <c:otherwise>
                                    <em>Chưa phân loại</em>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${p.createdAt}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/products/edit?id=${p.id}" class="btn btn-edit" style="padding: 4px 8px; font-size: 13px;">Sửa</a>
                            <a href="${pageContext.request.contextPath}/admin/products/delete?id=${p.id}" onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này không?');" class="btn btn-danger" style="padding: 4px 8px; font-size: 13px;">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="9" style="text-align:center; padding: 20px;">Không có sản phẩm nào.</td>
                </tr>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>
</div>

</body>
</html>
