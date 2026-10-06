<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property='title'>BookStore - Hồ Trọng Sơn 24133049</sitemesh:write></title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        :root {
            --primary-color: #0d6efd;
            --accent-color: #ffc107;
        }
        body {
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            background-color: #f8f9fa;
        }
        .main-content {
            flex: 1 0 auto;
        }
        .navbar-brand {
            font-weight: 700;
            letter-spacing: 0.5px;
        }
        .footer-custom {
            flex-shrink: 0;
            background: linear-gradient(135deg, #1e293b, #0f172a);
            color: #cbd5e1;
            padding: 25px 0;
            border-top: 3px solid #3b82f6;
        }
        .book-card {
            transition: transform 0.2s, box-shadow 0.2s;
            border: 1px solid #e2e8f0;
            border-radius: 8px;
            overflow: hidden;
            background: #fff;
        }
        .book-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.08);
        }
        .book-cover-img {
            height: 280px;
            object-fit: cover;
            width: 100%;
            background-color: #f1f5f9;
        }
        .star-rating {
            color: #f59e0b;
        }
    </style>
    <sitemesh:write property='head'/>
</head>
<body>

    <!-- Header Navigation Bar (Câu 1: Trang Chủ, Sản phẩm, Đăng nhập, Trang quản trị) -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm py-2">
        <div class="container">
            <a class="navbar-brand text-warning" href="${pageContext.request.contextPath}/home">
                <i class="bi bi-book-half me-2"></i>BookStore 24133049
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarUserContent">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarUserContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/home">
                            <i class="bi bi-house-door me-1"></i>Trang Chủ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/books">
                            <i class="bi bi-grid me-1"></i>Sản phẩm
                        </a>
                    </li>
                    <c:if test="${sessionScope.currentUser != null && sessionScope.currentUser.active && not sessionScope.currentUser.admin}">
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/cart">Giỏ hàng <span class="badge bg-success">${cartQuantity}</span></a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/order-history">Lịch sử đặt hàng</a></li>
                    </c:if>
                    <!-- Trang quản trị: Chỉ hiển thị cho Admin -->
                    <c:if test="${sessionScope.currentUser != null && sessionScope.currentUser.admin}">
                        <li class="nav-item">
                            <a class="nav-link text-warning fw-bold" href="${pageContext.request.contextPath}/admin/books">
                                <i class="bi bi-shield-lock me-1"></i>Trang quản trị
                            </a>
                        </li>
                    </c:if>
                </ul>

                <!-- Login / Logout State -->
                <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-center">
                    <c:choose>
                        <c:when test="${sessionScope.currentUser != null}">
                            <li class="nav-item me-3">
                                <span class="badge bg-secondary py-2 px-3">
                                    <i class="bi bi-person-circle me-1"></i>Xin chào, <b><c:out value="${sessionScope.currentUser.fullname}"/></b>
                                    <c:if test="${sessionScope.currentUser.admin}">
                                        <span class="badge bg-danger ms-1">Admin</span>
                                    </c:if>
                                </span>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-outline-danger btn-sm" href="${pageContext.request.contextPath}/logout">
                                    <i class="bi bi-box-arrow-right me-1"></i>Đăng xuất
                                </a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item me-2">
                                <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/login">
                                    <i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-warning btn-sm" href="${pageContext.request.contextPath}/register">
                                    <i class="bi bi-person-plus me-1"></i>Đăng ký
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content Area -->
    <main class="main-content py-4">
        <div class="container">
            <sitemesh:write property='body'/>
        </div>
    </main>

    <!-- Footer Area (Câu 1: Họ tên, MSSV, Mã đề) -->
    <footer class="footer-custom mt-auto">
        <div class="container text-center">
            <div class="row align-items-center">
                <div class="col-md-4 text-md-start mb-2 mb-md-0">
                    <h5 class="text-white mb-0"><i class="bi bi-mortarboard me-2"></i>ĐỒ ÁN LẬP TRÌNH WEB</h5>
                    <small class="text-muted">Khoa CNTT - Bộ môn Công nghệ Phần mềm</small>
                </div>
                <div class="col-md-8 text-md-end">
                    <div class="p-2 rounded bg-dark d-inline-block border border-secondary">
                        <span class="text-light me-3"><i class="bi bi-person-badge text-info me-1"></i>Họ tên: <strong class="text-white">Hồ Trọng Sơn</strong></span>
                        <span class="text-light me-3"><i class="bi bi-card-text text-warning me-1"></i>MSSV: <strong class="text-warning">24133049</strong></span>
                        <span class="text-light"><i class="bi bi-file-earmark-code text-success me-1"></i>Mã đề: <strong class="text-success">02</strong></span>
                    </div>
                </div>
            </div>
        </div>
    </footer>

    <!-- Bootstrap 5 Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
