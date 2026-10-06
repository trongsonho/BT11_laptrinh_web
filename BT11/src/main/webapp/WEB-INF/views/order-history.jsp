<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html lang="vi"><head><title>Lịch sử đặt hàng</title></head><body>
<h2>Lịch sử đặt hàng</h2>
<c:forEach var="message" items="${historyMessages}"><div class="alert alert-warning"><c:out value="${message}"/></div></c:forEach>
<form action="${pageContext.request.contextPath}/order-history" method="get" class="row g-2 align-items-end mb-3">
<input type="hidden" name="page" value="1">
<div class="col-sm-6 col-lg-4"><label for="orderStatus" class="form-label">Trạng thái đơn hàng</label>
<select id="orderStatus" name="status" class="form-select" onchange="this.form.submit()">
<option value="" ${empty selectedStatus ? 'selected' : ''}>Tất cả</option>
<c:forEach var="option" items="${statusOptions}"><option value="${option.code}" ${selectedStatus == option.code ? 'selected' : ''}><c:out value="${option.label}"/></option></c:forEach>
</select></div><div class="col-auto"><button type="submit" class="btn btn-primary">Lọc đơn hàng</button></div>
<div class="col-auto"><a href="${pageContext.request.contextPath}/order-history" class="btn btn-outline-secondary">Xem tất cả</a></div></form>
<p>Đang xem: <strong><c:out value="${selectedStatusLabel}"/></strong> · Tổng số đơn: ${history.totalOrders}</p>
<c:choose><c:when test="${empty history.orders}">
<div class="alert alert-secondary"><c:choose><c:when test="${empty selectedStatus}">Bạn chưa có đơn hàng nào.</c:when>
<c:otherwise>Không có đơn hàng khớp trạng thái “<c:out value="${selectedStatusLabel}"/>”.</c:otherwise></c:choose></div>
<a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Quay lại mua sách</a>
</c:when><c:otherwise>
<div class="table-responsive"><table class="table align-middle table-hover">
<thead><tr><th>Mã đơn</th><th>Ngày đặt</th><th>Tổng tiền</th><th>Phương thức</th><th>Thanh toán</th><th>Trạng thái đơn</th><th></th></tr></thead>
<tbody><c:forEach var="order" items="${history.orders}"><tr data-order-id="${order.id}">
<td><c:out value="${order.code}"/></td><td><fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
<td><fmt:formatNumber value="${order.total}" maxFractionDigits="2"/> ₫</td><td><c:out value="${order.paymentMethod}"/></td>
<td><c:choose><c:when test="${order.paymentStatus == 'UNPAID'}">Chưa thanh toán</c:when><c:when test="${order.paymentStatus == 'PAID'}">Đã thanh toán</c:when><c:otherwise>Chưa xác định</c:otherwise></c:choose> (<c:out value="${order.paymentStatus}"/>)</td>
<td><span class="badge bg-${order.statusBadge}"><c:out value="${order.statusLabel}"/></span> <small>(<c:out value="${order.status}"/>)</small></td>
<td><a class="btn btn-outline-primary btn-sm" href="${pageContext.request.contextPath}/order?id=${order.id}">Xem chi tiết</a></td>
</tr></c:forEach></tbody></table></div>
<p>Trang ${history.page} / ${history.totalPages} · 10 đơn/trang</p>
<c:if test="${history.totalPages > 1}"><nav aria-label="Phân trang lịch sử"><ul class="pagination flex-wrap">
<c:if test="${history.page > 1}">
<c:url var="previousUrl" value="/order-history"><c:param name="status" value="${selectedStatus}"/><c:param name="page" value="${history.page - 1}"/></c:url>
<li class="page-item"><a class="page-link" href="<c:out value='${previousUrl}'/>">Trang trước</a></li></c:if>
<c:forEach var="p" items="${history.pageLinks}">
<c:url var="pageUrl" value="/order-history"><c:param name="status" value="${selectedStatus}"/><c:param name="page" value="${p}"/></c:url>
<li class="page-item ${p == history.page ? 'active' : ''}"><a class="page-link" href="<c:out value='${pageUrl}'/>">${p}</a></li>
</c:forEach>
<c:if test="${history.page < history.totalPages}">
<c:url var="nextUrl" value="/order-history"><c:param name="status" value="${selectedStatus}"/><c:param name="page" value="${history.page + 1}"/></c:url>
<li class="page-item"><a class="page-link" href="<c:out value='${nextUrl}'/>">Trang sau</a></li></c:if>
</ul></nav></c:if>
</c:otherwise></c:choose></body></html>
