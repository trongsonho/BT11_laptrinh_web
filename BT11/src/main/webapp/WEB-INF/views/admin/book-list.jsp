<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản Lý Sách (CRUD) - Admin 24133049</title>
</head>
<body>

    <!-- Header bar -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h3 class="text-dark fw-bold mb-1">
                <i class="bi bi-book-half text-primary me-2"></i>Quản Lý Danh Sách Sách (CRUD)
            </h3>
            <span class="text-muted">Câu 6: Xây dựng chức năng CRUD (tạo, xem, cập nhật và xóa) có phân trang</span>
        </div>
        <a href="${pageContext.request.contextPath}/admin/books/add" class="btn btn-primary btn-lg shadow-sm">
            <i class="bi bi-plus-lg me-1"></i>+ Thêm Sách Mới
        </a>
    </div>

    <!-- Thông báo kết quả -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i>${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Bảng danh sách sách -->
    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover table-striped align-middle mb-0">
                    <thead class="table-dark">
                        <tr>
                            <th class="text-center" style="width: 60px;">ID</th>
                            <th style="width: 90px;">Bìa sách</th>
                            <th>Tiêu đề sách</th>
                            <th>Mã ISBN</th>
                            <th>Tác giả</th>
                            <th>Nhà xuất bản</th>
                            <th class="text-end">Giá</th>
                            <th class="text-center">Số lượng</th>
                            <th class="text-center">Đánh giá</th>
                            <th class="text-center" style="width: 150px;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty books}">
                                <tr>
                                    <td colspan="10" class="text-center py-4 text-muted">
                                        Không tìm thấy cuốn sách nào trong hệ thống.
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="b" items="${books}">
                                    <tr>
                                        <td class="text-center fw-bold text-secondary">${b.bookId}</td>
                                        <td>
                                            <img src="${b.coverImage}" alt="${b.title}" class="rounded" style="width: 60px; height: 80px; object-fit: cover;" onerror="this.src='https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400'">
                                        </td>
                                        <td>
                                            <div class="fw-bold text-primary">${b.title}</div>
                                            <small class="text-muted"><fmt:formatDate value="${b.publishDate}" pattern="dd/MM/yyyy"/></small>
                                        </td>
                                        <td><span class="badge bg-secondary">${b.isbn}</span></td>
                                        <td><span class="fw-semibold">${b.authorNames}</span></td>
                                        <td>${b.publisher}</td>
                                        <td class="text-end fw-bold text-danger">
                                            <fmt:formatNumber value="${b.price}" type="currency" currencySymbol="₫" maxFractionDigits="2"/>
                                        </td>
                                        <td class="text-center">
                                            <span class="badge ${b.quantity > 0 ? 'bg-success' : 'bg-danger'}">${b.quantity}</span>
                                        </td>
                                        <td class="text-center">
                                            <span class="badge bg-warning text-dark"><i class="bi bi-chat-text me-1"></i>${b.reviewCount}</span>
                                        </td>
                                        <td class="text-center">
                                            <div class="btn-group btn-group-sm">
                                                <a href="${pageContext.request.contextPath}/admin/books/edit?id=${b.bookId}" class="btn btn-outline-primary" title="Sửa thông tin">
                                                    <i class="bi bi-pencil-square"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/admin/books/delete?id=${b.bookId}" class="btn btn-outline-danger" 
                                                   onclick="return confirm('Bạn có chắc chắn muốn xóa cuốn sách: [${b.title}] không? Hành động này sẽ xóa các đánh giá liên quan!');" title="Xóa sách">
                                                    <i class="bi bi-trash"></i>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="card-footer bg-white d-flex justify-content-between align-items-center py-3">
            <span class="text-muted small">
                Hiển thị trang <b>${currentPage}</b> / <b>${totalPages}</b> (Tổng cộng <b>${totalBooks}</b> đầu sách)
            </span>

            <!-- Phân trang Admin -->
            <c:if test="${totalPages > 1}">
                <nav aria-label="Admin Page navigation">
                    <ul class="pagination pagination-sm mb-0">
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/admin/books?page=${currentPage - 1}">&laquo; Trước</a>
                        </li>
                        <c:forEach var="p" begin="1" end="${totalPages}">
                            <li class="page-item ${p == currentPage ? 'active' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/admin/books?page=${p}">${p}</a>
                            </li>
                        </c:forEach>
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/admin/books?page=${currentPage + 1}">Sau &raquo;</a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>

</body>
</html>
