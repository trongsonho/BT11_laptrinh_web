<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng Nhập - BookStore 24133049</title>
</head>
<body>

    <div class="row justify-content-center my-5">
        <div class="col-md-5 col-lg-4">
            <div class="card shadow border-0 rounded-3">
                <div class="card-header bg-primary text-white text-center py-3">
                    <h4 class="mb-0 fw-bold"><i class="bi bi-person-circle me-2"></i>ĐĂNG NHẬP</h4>
                    <small>Hệ thống Quản lý Sách - Đề 02</small>
                </div>
                <div class="card-body p-4">

                    <!-- Báo lỗi -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger py-2 small" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-1"></i>${errorMessage}
                            <c:if test="${not empty unactivatedEmail}">
                                <div class="mt-2">
                                    <a href="${pageContext.request.contextPath}/verify-otp?email=${unactivatedEmail}" class="btn btn-outline-danger btn-sm w-100">
                                        Kích hoạt tài khoản ngay &raquo;
                                    </a>
                                </div>
                            </c:if>
                        </div>
                    </c:if>

                    <!-- Báo thành công -->
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success py-2 small" role="alert">
                            <i class="bi bi-check-circle-fill me-1"></i>${successMessage}
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="mb-3">
                            <label for="email" class="form-label fw-semibold">Địa chỉ Email:</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                                <input type="email" class="form-control" id="email" name="email" value="${email}" placeholder="admin@bookstore.vn" required autofocus>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="password" class="form-label fw-semibold">Mật khẩu:</label>
                            <div class="input-group">
                                <span class="input-group-text"><i class="bi bi-key"></i></span>
                                <input type="password" class="form-control" id="password" name="password" placeholder="••••••" required>
                            </div>
                        </div>

                        <div class="d-grid gap-2 mt-4">
                            <button type="submit" class="btn btn-primary fw-bold py-2 shadow-sm">
                                <i class="bi bi-box-arrow-in-right me-1"></i>Đăng Nhập
                            </button>
                        </div>
                    </form>

                    <hr class="my-4">

                    <div class="text-center small">
                        <span>Chưa có tài khoản? </span>
                        <a href="${pageContext.request.contextPath}/register" class="fw-bold text-decoration-none">
                            Đăng ký ngay <i class="bi bi-arrow-right"></i>
                        </a>
                    </div>

                    <!-- Gợi ý tài khoản demo -->
                    <div class="mt-4 p-2 bg-light rounded border text-muted small">
                        <strong>Tài khoản thử nghiệm:</strong><br>
                        - Admin: <code>admin@bookstore.vn</code> / <code>123456</code><br>
                        - User: <code>user1@gmail.com</code> / <code>123456</code>
                    </div>

                </div>
            </div>
        </div>
    </div>

</body>
</html>
