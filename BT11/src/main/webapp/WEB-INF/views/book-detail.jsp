<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chi Tiết Sách - ${book.title}</title>
    <style>
        .detail-cover-img {
            max-height: 420px;
            width: 100%;
            object-fit: cover;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .review-box {
            border-left: 4px solid #0d6efd;
            background-color: #f8fafc;
            padding: 12px 18px;
            margin-bottom: 12px;
            border-radius: 4px;
        }
    </style>
</head>
<body>

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home"><i class="bi bi-house me-1"></i>Trang Chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">${book.title}</li>
        </ol>
    </nav>

    <!-- Success flash alert -->
    <c:if test="${not empty sessionScope.reviewSuccess}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i>${sessionScope.reviewSuccess}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="reviewSuccess" scope="session"/>
    </c:if>

    <!-- Book Detail Card (Câu 4: Mẫu thông tin chi tiết) -->
    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body p-4">
            <div class="row g-4">
                <!-- Cột Trái: [cover_image] -->
                <div class="col-md-4 text-center">
                    <img src="${book.coverImage}" alt="${book.title}" class="detail-cover-img mb-3" onerror="this.src='https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400'">
                    <%@ include file="cart-add.jspf" %>
                    <div class="d-grid gap-2">
                        <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary">
                            <i class="bi bi-arrow-left me-1"></i>Quay lại danh mục
                        </a>
                    </div>
                </div>

                <!-- Cột Phải: Các thông tin chi tiết -->
                <div class="col-md-8">
                    <h2 class="text-primary fw-bold mb-3">${book.title}</h2>
                    <h4 class="text-danger fw-bold mb-3">
                        <fmt:formatNumber value="${book.price}" type="currency" currencySymbol="₫" maxFractionDigits="2"/>
                    </h4>

                    <div class="list-group list-group-flush mb-4">
                        <div class="list-group-item px-0 py-2">
                            <strong>Tiêu đề:</strong> <span class="text-dark">${book.title}</span>
                        </div>
                        <div class="list-group-item px-0 py-2">
                            <strong>Mã isbn:</strong> <span class="badge bg-secondary fs-6">${book.isbn}</span>
                        </div>
                        <div class="list-group-item px-0 py-2">
                            <strong>Tác giả:</strong> <span class="text-primary fw-semibold">${book.authorNames}</span>
                        </div>
                        <div class="list-group-item px-0 py-2">
                            <strong>Publisher:</strong> <span>${book.publisher}</span>
                        </div>
                        <div class="list-group-item px-0 py-2">
                            <strong>Publisher_date:</strong> <span><fmt:formatDate value="${book.publishDate}" pattern="dd/MM/yyyy"/></span>
                        </div>
                        <div class="list-group-item px-0 py-2">
                            <strong>Quantity:</strong> <span class="badge bg-success fs-6">${book.quantity} cuốn</span>
                        </div>
                        <div class="list-group-item px-0 py-2 text-danger fw-bold fs-5">
                            <i class="bi bi-star-fill text-warning me-1"></i>Reviews (${book.reviewCount})
                        </div>
                    </div>

                    <div class="p-3 bg-light rounded border">
                        <h6 class="fw-bold text-secondary mb-2"><i class="bi bi-card-text me-1"></i>Mô tả tóm tắt:</h6>
                        <p class="mb-0 text-muted" style="line-height: 1.6;">${book.description}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Section Reviews & Form thêm reviews (Câu 4) -->
    <div class="card shadow-sm border-0 mb-5">
        <div class="card-header bg-white py-3 border-bottom">
            <h4 class="mb-0 text-primary"><i class="bi bi-chat-square-quote-fill me-2"></i>Reviews</h4>
        </div>
        <div class="card-body p-4">

            <!-- Danh sách Reviews: [users]: [review_text] -->
            <div class="mb-4">
                <h5 class="fw-bold text-dark mb-3">Đánh giá từ độc giả (${book.reviewCount}):</h5>
                <c:choose>
                    <c:when test="${empty ratings}">
                        <div class="alert alert-secondary text-muted">
                            <i class="bi bi-info-circle me-1"></i>Chưa có đánh giá nào cho cuốn sách này. Hãy là người đầu tiên để lại nhận xét!
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="r" items="${ratings}">
                            <div class="review-box shadow-sm">
                                <div class="d-flex justify-content-between align-items-center mb-1">
                                    <span class="fw-bold text-dark fs-6">
                                        <i class="bi bi-person-circle text-primary me-1"></i>[<c:out value="${r.userName}"/>]
                                    </span>
                                    <div>
                                        <span class="text-warning me-2">
                                            <c:forEach begin="1" end="${r.rating}">★</c:forEach><c:forEach begin="${r.rating + 1}" end="5">☆</c:forEach>
                                        </span>
                                        <small class="text-muted"><fmt:formatDate value="${r.reviewDate}" pattern="dd/MM/yyyy HH:mm"/></small>
                                    </div>
                                </div>
                                <div class="text-secondary fs-6 mt-1">
                                    : [<c:out value="${r.reviewText}"/>]
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

            <hr class="my-4">

            <!-- Form thêm reviews (Câu 4) -->
            <div class="p-4 bg-light rounded border">
                <h5 class="fw-bold text-dark mb-3">
                    <i class="bi bi-pencil-square me-2 text-warning"></i>Form thêm reviews
                </h5>

                <c:choose>
                    <c:when test="${sessionScope.currentUser != null}">
                        <form action="${pageContext.request.contextPath}/review/add" method="post">
                            <input type="hidden" name="bookId" value="${book.bookId}">

                            <c:if test="${userRating != null}">
                                <div class="alert alert-info py-2 small mb-3">
                                    <i class="bi bi-info-circle me-1"></i>Bạn đã từng đánh giá cuốn sách này (${userRating.rating} sao). Gửi lại form sẽ cập nhật đánh giá của bạn.
                                </div>
                            </c:if>

                            <div class="mb-3">
                                <label for="ratingSelect" class="form-label fw-semibold">Đánh giá điểm số (1 - 5 sao):</label>
                                <select name="rating" id="ratingSelect" class="form-select w-auto shadow-sm">
                                    <option value="5" ${userRating != null && userRating.rating == 5 ? 'selected' : ''}>★★★★★ (5 sao - Tuyệt vời)</option>
                                    <option value="4" ${userRating != null && userRating.rating == 4 ? 'selected' : ''}>★★★★☆ (4 sao - Rất tốt)</option>
                                    <option value="3" ${userRating != null && userRating.rating == 3 ? 'selected' : ''}>★★★☆☆ (3 sao - Bình thường)</option>
                                    <option value="2" ${userRating != null && userRating.rating == 2 ? 'selected' : ''}>★★☆☆☆ (2 sao - Tạm được)</option>
                                    <option value="1" ${userRating != null && userRating.rating == 1 ? 'selected' : ''}>★☆☆☆☆ (1 sao - Kém)</option>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="reviewText" class="form-label fw-semibold">Nội dung nhận xét:</label>
                                <textarea name="reviewText" id="reviewText" class="form-control" rows="3" placeholder="Chia sẻ cảm nhận của bạn về cuốn sách này..." required><c:out value="${userRating != null ? userRating.reviewText : ''}"/></textarea>
                            </div>

                            <button type="submit" class="btn btn-warning px-4 fw-bold shadow-sm">
                                <i class="bi bi-send-fill me-1"></i>[Submit]
                            </button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning mb-0">
                            <i class="bi bi-lock-fill me-2"></i>Vui lòng <a href="${pageContext.request.contextPath}/login" class="fw-bold text-dark text-decoration-underline">Đăng nhập</a> để viết đánh giá cho cuốn sách này.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>
    </div>

</body>
</html>
