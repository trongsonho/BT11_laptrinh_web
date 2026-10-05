<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng Ký Tài Khoản - BookStore 24133049</title>
</head>
<body>

    <div class="row justify-content-center my-4">
        <div class="col-md-6 col-lg-5">
            <div class="card shadow border-0 rounded-3">
                <div class="card-header bg-warning text-dark text-center py-3">
                    <h4 class="mb-0 fw-bold"><i class="bi bi-person-plus me-2"></i>ĐĂNG KÝ TÀI KHOẢN</h4>
                    <small class="text-muted">Kích hoạt tài khoản bằng mã OTP qua Email (Câu 2)</small>
                </div>
                <div class="card-body p-4">

                    <!-- Báo lỗi -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger py-2 small" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-1"></i>${errorMessage}
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/register" method="post">
                        <div class="mb-3">
                            <label for="fullname" class="form-label fw-semibold">Họ và tên:</label>
                            <input type="text" class="form-control" id="fullname" name="fullname" value="${fullname}" placeholder="Nguyễn Văn A" required>
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label fw-semibold">Địa chỉ Email (Nhận mã OTP):</label>
                            <input type="email" class="form-control" id="email" name="email" value="${email}" placeholder="example@gmail.com" required>
                            <small class="text-muted">Mã OTP gồm 6 chữ số sẽ được gửi tới email này để kích hoạt.</small>
                        </div>

                        <div class="mb-3">
                            <label for="phone" class="form-label fw-semibold">Số điện thoại:</label>
                            <input type="tel" class="form-control" id="phone" name="phone" value="${phone}" placeholder="0901234567" required>
                        </div>

                        <div class="mb-3">
                            <label for="password" class="form-label fw-semibold">Mật khẩu:</label>
                            <input type="password" class="form-control" id="password" name="password" placeholder="Tối thiểu 6 ký tự" required minlength="6">
                        </div>

                        <div class="mb-3">
                            <label for="confirmPassword" class="form-label fw-semibold">Xác nhận mật khẩu:</label>
                            <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="Nhập lại mật khẩu" required minlength="6">
                        </div>

                        <div class="d-grid gap-2 mt-4">
                            <button type="submit" class="btn btn-warning fw-bold py-2 shadow-sm">
                                <i class="bi bi-envelope-paper-heart me-1"></i>Đăng Ký & Nhận Mã OTP
                            </button>
                        </div>
                    </form>

                    <hr class="my-3">

                    <div class="text-center small">
                        <span>Đã có tài khoản? </span>
                        <a href="${pageContext.request.contextPath}/login" class="fw-bold text-decoration-none">
                            Đăng nhập tại đây <i class="bi bi-arrow-right"></i>
                        </a>
                    </div>

                </div>
            </div>
        </div>
    </div>

</body>
</html>
