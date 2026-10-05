<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cập Nhật Sách - Admin 24133049</title>
</head>
<body>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h3 class="text-dark fw-bold mb-1"><i class="bi bi-pencil-square text-primary me-2"></i>Cập Nhật Sách: ${book.title}</h3>
            <span class="text-muted">Chỉnh sửa thông tin và liên kết tác giả (Câu 6)</span>
        </div>
        <a href="${pageContext.request.contextPath}/admin/books" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left me-1"></i>Quay lại danh sách
        </a>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger shadow-sm py-2">
            <i class="bi bi-exclamation-triangle-fill me-1"></i>${errorMessage}
        </div>
    </c:if>

    <div class="card shadow-sm border-0">
        <div class="card-body p-4">
            <form action="${pageContext.request.contextPath}/admin/books/edit" method="post">
                <input type="hidden" name="bookId" value="${book.bookId}">

                <div class="row g-3">
                    <div class="col-md-8">
                        <label for="title" class="form-label fw-semibold">Tiêu đề sách: <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="title" name="title" value="${book.title}" required>
                    </div>

                    <div class="col-md-4">
                        <label for="isbn" class="form-label fw-semibold">Mã ISBN: <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="isbn" name="isbn" value="${book.isbn}" required>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Tác giả liên kết: <span class="text-danger">*</span></label>
                        
                        <!-- Lựa chọn Chọn có sẵn hoặc Thêm mới -->
                        <div class="btn-group btn-group-sm w-100 mb-2" role="group">
                            <input type="radio" class="btn-check" name="authorMode" id="modeSelect" value="select" autocomplete="off" ${empty authorMode || authorMode == 'select' ? 'checked' : ''} onchange="toggleAuthorMode('select')">
                            <label class="btn btn-outline-primary" for="modeSelect"><i class="bi bi-list-ul me-1"></i>Chọn tác giả có sẵn</label>

                            <input type="radio" class="btn-check" name="authorMode" id="modeNew" value="new" autocomplete="off" ${authorMode == 'new' ? 'checked' : ''} onchange="toggleAuthorMode('new')">
                            <label class="btn btn-outline-success" for="modeNew"><i class="bi bi-person-plus me-1"></i>Thêm tác giả mới</label>
                        </div>

                        <!-- Hộp chọn tác giả có sẵn -->
                        <div id="selectAuthorBox" style="${authorMode == 'new' ? 'display: none;' : ''}">
                            <select name="authorId" id="authorId" class="form-select" onchange="onSelectAuthorChange(this)">
                                <option value="">-- Chọn tác giả --</option>
                                <c:forEach var="a" items="${authors}">
                                    <option value="${a.authorId}" ${a.authorId == (authorId != null ? authorId : book.primaryAuthorId) ? 'selected' : ''}>
                                        ${a.authorName}
                                    </option>
                                </c:forEach>
                                <option value="new" class="text-success fw-bold">+ Thêm tác giả mới...</option>
                            </select>
                        </div>

                        <!-- Hộp nhập tên tác giả mới -->
                        <div id="newAuthorBox" style="${authorMode == 'new' ? '' : 'display: none;'}">
                            <div class="input-group">
                                <span class="input-group-text bg-success text-white"><i class="bi bi-person-plus-fill"></i></span>
                                <input type="text" class="form-control border-success" id="newAuthorName" name="newAuthorName" value="${newAuthorName}" placeholder="Nhập tên tác giả mới (ví dụ: Nguyễn Du, Paulo Coelho...)">
                            </div>
                            <div class="form-text text-muted">Hệ thống sẽ tự động lưu tác giả mới vào CSDL và liên kết với cuốn sách này.</div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <label for="publisher" class="form-label fw-semibold">Nhà xuất bản:</label>
                        <input type="text" class="form-control" id="publisher" name="publisher" value="${book.publisher}">
                    </div>

                    <div class="col-md-4">
                        <label for="price" class="form-label fw-semibold">Đơn giá (VNĐ):</label>
                        <input type="number" step="0.01" class="form-control" id="price" name="price" value="${book.price}" required>
                    </div>

                    <div class="col-md-4">
                        <label for="quantity" class="form-label fw-semibold">Số lượng tồn kho:</label>
                        <input type="number" class="form-control" id="quantity" name="quantity" value="${book.quantity}" required min="0">
                    </div>

                    <div class="col-md-4">
                        <label for="publishDate" class="form-label fw-semibold">Ngày xuất bản:</label>
                        <input type="date" class="form-control" id="publishDate" name="publishDate" value="${book.publishDate}">
                    </div>

                    <div class="col-md-12">
                        <label for="coverImage" class="form-label fw-semibold">Đường dẫn ảnh bìa (URL):</label>
                        <input type="url" class="form-control" id="coverImage" name="coverImage" value="${book.coverImage}">
                    </div>

                    <div class="col-md-12">
                        <label for="description" class="form-label fw-semibold">Mô tả nội dung sách:</label>
                        <textarea class="form-control" id="description" name="description" rows="4">${book.description}</textarea>
                    </div>
                </div>

                <hr class="my-4">

                <div class="d-flex justify-content-end gap-2">
                    <a href="${pageContext.request.contextPath}/admin/books" class="btn btn-secondary px-4">Hủy bỏ</a>
                    <button type="submit" class="btn btn-success px-4 fw-bold">
                        <i class="bi bi-check-lg me-1"></i>Cập Nhật Sách
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function toggleAuthorMode(mode) {
            var selectBox = document.getElementById('selectAuthorBox');
            var newBox = document.getElementById('newAuthorBox');
            var authorSelect = document.getElementById('authorId');
            var newAuthorInput = document.getElementById('newAuthorName');

            if (mode === 'new') {
                selectBox.style.display = 'none';
                newBox.style.display = 'block';
                document.getElementById('modeNew').checked = true;
                setTimeout(function() { newAuthorInput.focus(); }, 100);
            } else {
                selectBox.style.display = 'block';
                newBox.style.display = 'none';
                document.getElementById('modeSelect').checked = true;
                if (authorSelect.value === 'new') {
                    authorSelect.value = '';
                }
            }
        }

        function onSelectAuthorChange(selectElem) {
            if (selectElem.value === 'new') {
                toggleAuthorMode('new');
            }
        }
    </script>
</body>
</html>
