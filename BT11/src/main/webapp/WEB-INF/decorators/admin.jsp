<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property='title'>Trang Quản Trị - Hồ Trọng Sơn 24133049</sitemesh:write></title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body {
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            background-color: #f1f5f9;
        }
        .admin-wrapper {
            flex: 1 0 auto;
            display: flex;
        }
        .admin-sidebar {
            width: 260px;
            background: #1e293b;
            color: #e2e8f0;
            min-height: calc(100vh - 120px);
            padding: 20px 0;
            flex-shrink: 0;
        }
        .admin-sidebar .nav-link {
            color: #94a3b8;
            padding: 12px 20px;
            font-weight: 500;
            border-left: 4px solid transparent;
            transition: all 0.2s;
        }
        .admin-sidebar .nav-link:hover, .admin-sidebar .nav-link.active {
            color: #fff;
            background-color: #0f172a;
            border-left-color: #3b82f6;
        }
        .admin-sidebar .nav-link i {
            margin-right: 10px;
        }
        .admin-main {
            flex: 1;
            padding: 25px 30px;
            overflow-x: auto;
        }
        .footer-admin {
            flex-shrink: 0;
            background-color: #0f172a;
            color: #94a3b8;
            padding: 15px 0;
            border-top: 1px solid #334155;
            font-size: 0.9rem;
        }
        .badge-admin {
            background: linear-gradient(135deg, #ef4444, #dc2626);
            color: white;
        }
    </style>
    <sitemesh:write property='head'/>
</head>
<body>

    <!-- Admin Top Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark px-4 py-2 border-bottom border-secondary">
        <a class="navbar-brand text-warning fw-bold" href="${pageContext.request.contextPath}/admin/books">
            <i class="bi bi-gear-wide-connected me-2"></i>ADMIN DASHBOARD
        </a>
        <div class="ms-auto d-flex align-items-center">
            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-info btn-sm me-3" target="_blank">
                <i class="bi bi-box-arrow-up-right me-1"></i>Xem trang User
            </a>
            <span class="text-light me-3">
                <i class="bi bi-person-fill-gear me-1"></i>${sessionScope.currentUser.fullname}
                <span class="badge badge-admin ms-1">Admin</span>
            </span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">
                <i class="bi bi-box-arrow-right me-1"></i>Đăng xuất
            </a>
        </div>
    </nav>

    <!-- Admin Wrapper: Sidebar + Content -->
    <div class="admin-wrapper">
        <div class="admin-sidebar shadow-sm">
            <div class="px-3 mb-4 text-center">
                <div class="p-2 rounded bg-secondary bg-opacity-25">
                    <small class="text-uppercase text-muted fw-bold">Khu vực quản trị</small>
                    <div class="fw-semibold text-white">Mã đề: 02 - MSSV: 24133049</div>
                </div>
            </div>
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin/books">
                        <i class="bi bi-book"></i>Quản lý Sách (CRUD)
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/books/add">
                        <i class="bi bi-plus-circle"></i>Thêm Sách Mới
                    </a>
                </li>
                <li class="nav-item mt-3">
                    <div class="px-3 py-1 text-uppercase text-muted" style="font-size: 0.75rem;">Điều hướng</div>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">
                        <i class="bi bi-house"></i>Về Trang Khách
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-danger" href="${pageContext.request.contextPath}/logout">
                        <i class="bi bi-box-arrow-right"></i>Đăng xuất
                    </a>
                </li>
            </ul>
        </div>

        <div class="admin-main">
            <sitemesh:write property='body'/>
        </div>
    </div>

    <!-- Admin Footer (Câu 1: Họ tên, MSSV, Mã đề) -->
    <footer class="footer-admin text-center">
        <div class="container">
            <span>Hệ thống Quản trị BookStore | Thực hiện bởi: <strong class="text-white">Hồ Trọng Sơn</strong> | MSSV: <strong class="text-warning">24133049</strong> | Mã đề thi: <strong class="text-success">02</strong></span>
        </div>
    </footer>

    <!-- Bootstrap 5 Bundle JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
