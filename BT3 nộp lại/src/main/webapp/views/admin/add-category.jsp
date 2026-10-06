<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm danh mục</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
        .main-container { padding: 20px; }
    </style>
</head>
<body>
    <jsp:include page="/header.jsp" />
    <div class="main-container">
    <h2>Thêm danh mục mới</h2>
    <form action="add" method="post" enctype="multipart/form-data">
        <div>
            <label>Tên danh mục:</label>
            <input type="text" name="name" required/>
        </div>
        <br/>
        <div>
            <label>Ảnh đại diện:</label>
            <input type="file" name="icon"/>
        </div>
        <br/>
        <button type="submit">Thêm</button>
        <button type="reset">Hủy</button>
    </form>
    <br/>
    <a href="list">Quay lại danh sách</a>
    </div>
</body>
</html>