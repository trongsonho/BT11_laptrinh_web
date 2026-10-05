<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trang Chủ - Danh Sách Sách Theo Tác Giả</title>
</head>
<body>

    <!-- Author Selection / Filter Form (Câu 3) -->
    <div class="card shadow-sm mb-4 border-0">
        <div class="card-body bg-light rounded p-4">
            <form action="${pageContext.request.contextPath}/home" method="get" class="row align-items-center g-3">
                <div class="col-auto">
                    <label for="authorSelect" class="col-form-label fw-bold fs-5 text-primary">
                        <i class="bi bi-person-lines-fill me-1"></i>Tác giả :
                    </label>
                </div>
                <div class="col-md-5">
                    <select name="authorId" id="authorSelect" class="form-select form-select-lg shadow-sm" onchange="this.form.submit()">
                        <c:forEach var="auth" items="${authors}">
                            <option value="${auth.authorId}" ${auth.authorId == selectedAuthor.authorId ? 'selected' : ''}>
                                ${auth.authorName} (<fmt:formatDate value="${auth.dateOfBirth}" pattern="dd/MM/yyyy"/>)
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-primary btn-lg shadow-sm px-4">
                        <i class="bi bi-filter me-1"></i>Lọc sách
                    </button>
                </div>
                <div class="col text-end">
                    <span class="badge bg-info text-dark p-2 fs-6">
                        Tổng cộng: <b>${totalBooks}</b> cuốn sách của <b>${selectedAuthor.authorName}</b>
                    </span>
                </div>
            </form>
        </div>
    </div>

    <!-- Section Title -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="text-secondary mb-0">
            <i class="bi bi-journal-bookmark-fill me-2 text-danger"></i>Danh mục sách: <span class="text-dark fw-bold">${selectedAuthor.authorName}</span>
        </h4>
        <small class="text-muted">Hiển thị 03 sản phẩm / trang theo chuẩn Đề số 02</small>
    </div>

    <!-- Book Grid (Câu 3: 3 sách / trang với mẫu thông tin yêu cầu) -->
    <c:choose>
        <c:when test="${empty books}">
            <div class="alert alert-warning text-center p-4">
                <i class="bi bi-exclamation-triangle fs-3 d-block mb-2"></i>
                Hiện chưa có cuốn sách nào của tác giả này.
            </div>
        </c:when>
        <c:otherwise>
            <div class="row row-cols-1 row-cols-md-3 g-4 mb-4">
                <c:forEach var="book" items="${books}">
                    <div class="col">
                        <div class="card h-100 book-card shadow-sm">
                            <a href="${pageContext.request.contextPath}/book-detail?id=${book.bookId}">
                                <img src="${book.coverImage}" class="card-img-top book-cover-img" alt="${book.title}" onerror="this.src='https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400'">
                            </a>
                            <div class="card-body d-flex flex-column p-3">
                                <h5 class="card-title text-truncate mb-2">
                                    <a href="${pageContext.request.contextPath}/book-detail?id=${book.bookId}" class="text-decoration-none text-primary fw-bold" title="${book.title}">
                                        Tiêu đề: ${book.title}
                                    </a>
                                </h5>
                                <div class="card-text text-muted small mb-3">
                                    <div class="mb-1"><strong>Mã isbn:</strong> <span class="badge bg-secondary">${book.isbn}</span></div>
                                    <div class="mb-1"><strong>Tác giả:</strong> ${selectedAuthor.authorName}</div>
                                    <div class="mb-1"><strong>Publisher:</strong> ${book.publisher}</div>
                                    <div class="mb-1"><strong>Publisher_date:</strong> <fmt:formatDate value="${book.publishDate}" pattern="dd/MM/yyyy"/></div>
                                    <div class="mb-1"><strong>Quantity:</strong> <span class="badge bg-success">${book.quantity}</span></div>
                                    <div class="mb-1 text-danger fw-bold">
                                        <i class="bi bi-chat-left-text me-1"></i>Review (${book.reviewCount})
                                    </div>
                                </div>
                                <%@ include file="cart-add.jspf" %>
                                <div class="mt-auto pt-2 border-top d-flex justify-content-between align-items-center">
                                    <span class="fs-5 fw-bold text-danger">
                                        <fmt:formatNumber value="${book.price}" type="currency" currencySymbol="₫" maxFractionDigits="2"/>
                                    </span>
                                    <a href="${pageContext.request.contextPath}/book-detail?id=${book.bookId}" class="btn btn-outline-primary btn-sm">
                                        Chi tiết <i class="bi bi-arrow-right"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

    <!-- Pagination: Trang trước – 1 2 3 4 – Trang sau (Câu 3) -->
    <c:if test="${totalPages > 1}">
        <nav aria-label="Page navigation" class="mt-4 mb-5">
            <ul class="pagination pagination-lg justify-content-center shadow-sm">
                <!-- Trang trước -->
                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/home?authorId=${selectedAuthor.authorId}&page=${currentPage - 1}" aria-label="Previous">
                        <span aria-hidden="true">&laquo; Trang trước</span>
                    </a>
                </li>

                <!-- Danh sách số trang -->
                <c:forEach var="p" begin="1" end="${totalPages}">
                    <li class="page-item ${p == currentPage ? 'active' : ''}">
                        <a class="page-link" href="${pageContext.request.contextPath}/home?authorId=${selectedAuthor.authorId}&page=${p}">
                            ${p}
                        </a>
                    </li>
                </c:forEach>

                <!-- Trang sau -->
                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/home?authorId=${selectedAuthor.authorId}&page=${currentPage + 1}" aria-label="Next">
                        <span aria-hidden="true">Trang sau &raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
    </c:if>

</body>
</html>
