<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm sản phẩm mới</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f8f9fa; margin: 0; padding: 0; }
        .form-container { max-width: 600px; margin: 30px auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        h2 { margin-top: 0; color: #333; }
        .form-group { margin-bottom: 20px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; color: #555; }
        input[type="text"], input[type="number"], select, textarea { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        textarea { height: 100px; resize: vertical; }
        .btn-group { display: flex; gap: 10px; margin-top: 25px; }
        .btn { padding: 10px 20px; border-radius: 4px; font-weight: bold; cursor: pointer; text-decoration: none; border: none; font-size: 15px; }
        .btn-primary { background-color: #007bff; color: white; }
        .btn-primary:hover { background-color: #0056b3; }
        .btn-secondary { background-color: #6c757d; color: white; }
    </style>
</head>
<body>

<jsp:include page="/header.jsp" />

<div class="form-container">
    <h2>Thêm sản phẩm mới</h2>

    <form action="${pageContext.request.contextPath}/admin/products/add" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="name">Tên sản phẩm (*):</label>
            <input type="text" id="name" name="name" required>
        </div>

        <div class="form-group">
            <label for="price">Giá (VNĐ) (*):</label>
            <input type="number" id="price" name="price" step="0.01" min="0" required>
        </div>

        <div class="form-group">
            <label for="quantity">Số lượng (*):</label>
            <input type="number" id="quantity" name="quantity" min="0" required>
        </div>

        <div class="form-group">
            <label for="categoryId">Danh mục:</label>
            <select id="categoryId" name="categoryId">
                <option value="">-- Chọn danh mục --</option>
                <c:forEach items="${categories}" var="cat">
                    <option value="${cat.id}">${cat.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="image">Hình ảnh sản phẩm:</label>
            <input type="file" id="image" name="image" accept="image/*">
        </div>

        <div class="form-group">
            <label for="description">Mô tả sản phẩm:</label>
            <textarea id="description" name="description"></textarea>
        </div>

        <div class="btn-group">
            <button type="submit" class="btn btn-primary">Lưu sản phẩm</button>
            <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-secondary">Hủy bỏ</a>
        </div>
    </form>
</div>

</body>
</html>
