<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Xác Thực OTP Kích Hoạt Tài Khoản - BookStore 24133049</title>
</head>
<body>

    <div class="row justify-content-center my-5">
        <div class="col-md-5 col-lg-4">
            <div class="card shadow border-0 rounded-3">
                <div class="card-header bg-success text-white text-center py-3">
                    <h4 class="mb-0 fw-bold"><i class="bi bi-shield-check me-2"></i>KÍCH HOẠT TÀI KHOẢN</h4>
                    <small>Nhập mã xác thực OTP gửi qua email (Câu 2)</small>
                </div>
                <div class="card-body p-4">

                    <!-- Báo lỗi -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger py-2 small" role="alert">
                            <i class="bi bi-exclamation-circle-fill me-1"></i>${errorMessage}
                        </div>
                    </c:if>

                    <!-- Báo thông tin -->
                    <c:if test="${not empty sessionScope.infoMessage}">
                        <div class="alert alert-info py-2 small" role="alert">
                            <i class="bi bi-info-circle-fill me-1"></i>${sessionScope.infoMessage}
                        </div>
                        <c:remove var="infoMessage" scope="session"/>
                    </c:if>

                    <p class="text-muted small text-center">
                        Mã xác thực gồm 6 chữ số đã được gửi tới địa chỉ:<br>
                        <strong class="text-primary fs-6">${email}</strong><br>
                        Mã có hiệu lực trong <b>5 phút</b> và tối đa <b>5 lần thử</b>.
                    </p>

                    <form action="${pageContext.request.contextPath}/verify-otp" method="post">
                        <input type="hidden" name="email" value="${email}">

                        <div class="mb-4">
                            <label for="otp" class="form-label fw-semibold text-center d-block">Nhập mã 6 chữ số OTP:</label>
                            <input type="text" class="form-control form-control-lg text-center fw-bold letter-spacing-2" 
                                   id="otp" name="otp" placeholder="123456" maxlength="6" pattern="[0-9]{6}" required autofocus
                                   style="font-size: 24px; letter-spacing: 6px;">
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-success fw-bold py-2 shadow-sm">
                                <i class="bi bi-check2-circle me-1"></i>Kích Hoạt Tài Khoản
                            </button>
                        </div>
                    </form>

                    <hr class="my-3">

                    <div class="d-flex justify-content-between align-items-center small">
                        <a href="${pageContext.request.contextPath}/resend-otp?email=${email}" class="text-decoration-none text-warning fw-bold">
                            <i class="bi bi-arrow-clockwise me-1"></i>Gửi lại mã OTP
                        </a>
                        <a href="${pageContext.request.contextPath}/login" class="text-decoration-none text-muted">
                            Đăng nhập <i class="bi bi-box-arrow-in-right"></i>
                        </a>
                    </div>

                </div>
            </div>
        </div>
    </div>

</body>
</html>
