<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html lang="vi"><head><title>Chi tiết đơn hàng</title></head><body>
<c:if test="${orderSuccess}"><div class="alert alert-success"><h3>Đặt hàng thành công!</h3>Đơn COD được ghi nhận. Bạn sẽ thanh toán khi nhận hàng.</div></c:if>
<h2>Đơn <c:out value="${order.code}"/></h2>
<p>Thời gian: <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/> (giờ SQL Server)</p>
<p>Trạng thái đơn: <span class="badge bg-${order.statusBadge}"><c:out value="${order.statusLabel}"/></span> (<c:out value="${order.status}"/>) · Phương thức: <c:out value="${order.paymentMethod}"/> · Thanh toán: <strong><c:out value="${order.paymentStatus}"/></strong>
<c:if test="${order.paymentStatus == 'UNPAID'}">(Chưa thanh toán)</c:if></p>
<div class="card mb-4"><div class="card-body">
<h4>Thông tin giao hàng</h4>
<p>Người nhận: <c:out value="${order.shipping.recipientName}"/></p><p>Điện thoại: <c:out value="${order.shipping.phone}"/></p>
<p>Địa chỉ: <c:out value="${order.shipping.address}"/></p><p>Ghi chú: <c:out value="${order.shipping.note}"/></p>
</div></div>
<div class="table-responsive"><table class="table"><thead><tr><th>Sách</th><th>Đơn giá</th><th>Số lượng</th><th>Thành tiền</th></tr></thead>
<tbody><c:forEach var="item" items="${order.items}"><tr><td><c:out value="${item.title}"/></td>
<td><fmt:formatNumber value="${item.unitPrice}" maxFractionDigits="2"/> ₫</td><td>${item.quantity}</td>
<td><fmt:formatNumber value="${item.subtotal}" maxFractionDigits="2"/> ₫</td></tr></c:forEach></tbody></table></div>
<p class="fs-4">Tổng tiền: <strong><fmt:formatNumber value="${order.total}" maxFractionDigits="2"/> ₫</strong></p>
<a class="btn btn-primary" href="${pageContext.request.contextPath}/order?id=${order.id}">Xem chi tiết đơn</a>
<a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/order-history">Lịch sử đặt hàng</a>
<a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/home">Tiếp tục mua sách</a>
</body></html>
