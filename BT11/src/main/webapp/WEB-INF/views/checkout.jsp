<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html lang="vi"><head><title>Đặt hàng COD</title></head><body>
<h2>Đặt hàng COD</h2>
<c:if test="${not empty checkoutError}"><div class="alert alert-danger" role="alert"><c:out value="${checkoutError}"/></div></c:if>
<div class="row g-4"><div class="col-lg-7">
<form action="${pageContext.request.contextPath}/checkout" method="post">
<input type="hidden" name="csrfToken" value="${sessionScope.commerceCsrf}">
<input type="hidden" name="checkoutToken" value="<c:out value='${checkoutToken}'/>">
<div class="mb-3"><label for="recipientName" class="form-label">Họ tên người nhận</label>
<input id="recipientName" class="form-control" name="recipientName" required maxlength="100" value="<c:out value='${shipping.recipientName}'/>"></div>
<div class="mb-3"><label for="phone" class="form-label">Số điện thoại</label>
<input id="phone" class="form-control" name="phone" type="tel" required maxlength="15" value="<c:out value='${shipping.phone}'/>">
<div class="form-text">10–11 chữ số bắt đầu bằng 0, hoặc +84 và 9–10 chữ số.</div></div>
<div class="mb-3"><label for="address" class="form-label">Địa chỉ nhận hàng</label>
<textarea id="address" class="form-control" name="address" required maxlength="500" rows="3"><c:out value="${shipping.address}"/></textarea></div>
<div class="mb-3"><label for="note" class="form-label">Ghi chú (tùy chọn)</label>
<textarea id="note" class="form-control" name="note" maxlength="1000" rows="3"><c:out value="${shipping.note}"/></textarea></div>
<p class="alert alert-info">COD – Thanh toán khi nhận hàng. Đơn vừa đặt ở trạng thái <strong>chưa thanh toán</strong>.</p>
<button class="btn btn-success" type="submit" ${cartValid ? '' : 'disabled'}>Xác nhận đặt hàng COD</button>
<a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/cart">Quay lại giỏ hàng</a>
</form></div><div class="col-lg-5">
<div class="card"><div class="card-body"><h4>Sách đặt mua</h4>
<c:forEach var="line" items="${cartLines}"><div class="border-bottom py-3">
<strong><c:out value="${empty line.book ? 'Sách đã bị xóa' : line.book.title}"/></strong>
<p class="mb-1">${line.quantity} × <fmt:formatNumber value="${line.book.price}" maxFractionDigits="2"/> ₫ = <fmt:formatNumber value="${line.subtotal}" maxFractionDigits="2"/> ₫</p>
<c:if test="${not empty line.error}"><div class="text-danger"><c:out value="${line.error}"/></div></c:if>
</div></c:forEach>
<p class="fs-5 mt-3">Tổng tiền: <strong><fmt:formatNumber value="${cartTotal}" maxFractionDigits="2"/> ₫</strong></p>
<c:if test="${not cartValid}"><p class="text-danger">Giỏ hàng không hợp lệ hoặc đang trống. Vui lòng kiểm tra lại giỏ.</p></c:if>
<small class="text-muted">Giá và tồn kho được kiểm tra lại khi xác nhận đơn.</small>
</div></div></div></div></body></html>
