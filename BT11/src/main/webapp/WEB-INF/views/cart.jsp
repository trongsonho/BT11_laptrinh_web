<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html lang="vi"><head><title>Giỏ hàng</title></head><body>
<h2>Giỏ hàng</h2>
<c:if test="${not empty cartMessage}"><div class="alert alert-info"><c:out value="${cartMessage}"/></div></c:if>
<c:choose><c:when test="${empty cartLines}">
  <div class="alert alert-secondary">Giỏ hàng đang trống.</div>
  <a class="btn btn-primary" href="${pageContext.request.contextPath}/home">Quay lại mua sách</a>
</c:when><c:otherwise>
<div class="table-responsive"><table class="table align-middle">
<thead><tr><th>Ảnh</th><th>Sách</th><th>Đơn giá</th><th>Số lượng</th><th>Thành tiền</th><th></th></tr></thead>
<tbody><c:forEach var="line" items="${cartLines}"><tr>
<td><c:if test="${not empty line.book}"><img src="<c:out value='${line.book.coverImage}'/>" alt="Bìa sách" width="70"></c:if></td>
<td><c:choose><c:when test="${empty line.book}">Sách ID ${line.bookId} đã bị xóa</c:when><c:otherwise>
<a href="${pageContext.request.contextPath}/book-detail?id=${line.bookId}"><c:out value="${line.book.title}"/></a>
<div class="small">Tồn kho: ${line.book.quantity}</div></c:otherwise></c:choose>
<c:if test="${not empty line.error}"><div class="text-danger"><c:out value="${line.error}"/></div></c:if></td>
<td><fmt:formatNumber value="${line.book.price}" maxFractionDigits="2"/> ₫</td>
<td><form action="${pageContext.request.contextPath}/cart" method="post" class="d-flex gap-1">
<input type="hidden" name="csrfToken" value="${sessionScope.commerceCsrf}">
<input type="hidden" name="action" value="update"><input type="hidden" name="bookId" value="${line.bookId}">
<button type="button" class="btn btn-outline-secondary" aria-label="Giảm số lượng" onclick="adjustCartQuantity(this,-1)">−</button>
<input aria-label="Số lượng" type="number" name="quantity" min="1" max="${empty line.book ? 1 : line.book.quantity}" value="${line.quantity}" required class="form-control" style="width:100px">
<button type="button" class="btn btn-outline-secondary" aria-label="Tăng số lượng" onclick="adjustCartQuantity(this,1)">+</button>
<button class="btn btn-primary" type="submit">Lưu</button></form></td>
<td><fmt:formatNumber value="${line.subtotal}" maxFractionDigits="2"/> ₫</td>
<td><form action="${pageContext.request.contextPath}/cart" method="post">
<input type="hidden" name="csrfToken" value="${sessionScope.commerceCsrf}"><input type="hidden" name="action" value="remove">
<input type="hidden" name="bookId" value="${line.bookId}"><button class="btn btn-outline-danger" type="submit">Xóa</button></form></td>
</tr></c:forEach></tbody></table></div>
<p class="fs-4">Tổng tiền: <strong><fmt:formatNumber value="${cartTotal}" maxFractionDigits="2"/> ₫</strong></p>
<div class="d-flex gap-2 flex-wrap">
<a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/home">Tiếp tục mua sách</a>
<c:if test="${cartValid}"><a class="btn btn-success" href="${pageContext.request.contextPath}/checkout">Đặt hàng COD</a></c:if>
<form action="${pageContext.request.contextPath}/cart" method="post" onsubmit="return confirm('Xóa toàn bộ giỏ hàng?')">
<input type="hidden" name="csrfToken" value="${sessionScope.commerceCsrf}"><input type="hidden" name="action" value="clear">
<input type="hidden" name="confirmed" value="yes"><button type="submit" class="btn btn-danger">Xóa toàn bộ giỏ</button></form>
</div>
<c:if test="${not cartValid}"><p class="text-danger mt-3">Vui lòng sửa số lượng hoặc xóa sách không hợp lệ trước khi đặt hàng.</p></c:if>
</c:otherwise></c:choose>
<script>
function adjustCartQuantity(button, delta) {
  const input = button.form.querySelector('[name=quantity]');
  const value = Number(input.value) + delta;
  if (Number.isInteger(value) && value >= 1 && value <= Number(input.max)) input.value = value;
}
</script></body></html>
