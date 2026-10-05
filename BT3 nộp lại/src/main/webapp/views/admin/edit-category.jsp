<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sửa danh mục</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
        .main-container { padding: 20px; }
    </style>
</head>
<body>
    <jsp:include page="/header.jsp" />
    <div class="main-container">
    <h2>Sửa danh mục</h2>
    <form action="edit" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${category.id}"/>
        <div>
            <label>Tên danh mục:</label>
            <input type="text" name="name" value="${category.name}" required/>
        </div>
        <br/>
        <div>
            <label>Ảnh đại diện hiện tại:</label><br/>
            <c:if test="${not empty category.icon}">
                <img src="${pageContext.request.contextPath}/image?fname=${category.icon}"
                     width="100" height="100" alt="Image"/>
            </c:if>
        </div>
        <br/>
        <div>
            <label>Chọn ảnh mới:</label>
            <input type="file" name="icon"/>
        </div>
        <br/>
        <button type="submit">Cập nhật</button>
        <button type="reset">Hủy</button>
    </form>
    <br/>
    <a href="list">Quay lại danh sách</a>
    </div>
</body>
</html>