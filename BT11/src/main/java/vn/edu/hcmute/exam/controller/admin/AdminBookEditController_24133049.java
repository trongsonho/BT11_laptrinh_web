package vn.edu.hcmute.exam.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmute.exam.model.Author_24133049;
import vn.edu.hcmute.exam.model.Book_24133049;
import vn.edu.hcmute.exam.service.AuthorServiceImpl_24133049;
import vn.edu.hcmute.exam.service.BookServiceImpl_24133049;
import vn.edu.hcmute.exam.service.IAuthorService_24133049;
import vn.edu.hcmute.exam.service.IBookService_24133049;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "AdminBookEditController_24133049", urlPatterns = {"/admin/books/edit"})
public class AdminBookEditController_24133049 extends HttpServlet {

    private final IBookService_24133049 bookService = new BookServiceImpl_24133049();
    private final IAuthorService_24133049 authorService = new AuthorServiceImpl_24133049();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/books");
            return;
        }

        int bookId;
        try {
            bookId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/books");
            return;
        }

        Book_24133049 book = bookService.getBookById(bookId);
        if (book == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/books");
            return;
        }

        List<Author_24133049> authors = authorService.getAllAuthors();
        req.setAttribute("book", book);
        req.setAttribute("authors", authors);
        resp.setContentType("text/html;charset=UTF-8");
        req.getRequestDispatcher("/WEB-INF/views/admin/book-edit.jsp").include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String idParam = req.getParameter("bookId");
        String isbn = req.getParameter("isbn");
        String title = req.getParameter("title");
        String publisher = req.getParameter("publisher");
        String priceParam = req.getParameter("price");
        String description = req.getParameter("description");
        String publishDateParam = req.getParameter("publishDate");
        String coverImage = req.getParameter("coverImage");
        String quantityParam = req.getParameter("quantity");
        String authorMode = req.getParameter("authorMode");
        String authorIdParam = req.getParameter("authorId");
        String newAuthorName = req.getParameter("newAuthorName");

        int bookId = 0;
        try {
            bookId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/books");
            return;
        }

        if (title == null || title.trim().isEmpty() || isbn == null || isbn.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Tiêu đề và mã ISBN không được để trống.");
            req.setAttribute("authorMode", authorMode);
            req.setAttribute("newAuthorName", newAuthorName);
            doGet(req, resp);
            return;
        }

        BigDecimal price = BigDecimal.ZERO;
        if (priceParam != null && !priceParam.trim().isEmpty()) {
            try {
                price = new BigDecimal(priceParam.trim());
            } catch (Exception ignored) {
            }
        }

        Date publishDate = null;
        if (publishDateParam != null && !publishDateParam.trim().isEmpty()) {
            try {
                publishDate = Date.valueOf(publishDateParam.trim());
            } catch (Exception ignored) {
            }
        }

        int quantity = 0;
        if (quantityParam != null && !quantityParam.trim().isEmpty()) {
            try {
                quantity = Integer.parseInt(quantityParam.trim());
            } catch (Exception ignored) {
            }
        }

        int authorId = 0;
        if ("new".equalsIgnoreCase(authorMode) || (newAuthorName != null && !newAuthorName.trim().isEmpty() && (authorIdParam == null || authorIdParam.trim().isEmpty() || "new".equalsIgnoreCase(authorIdParam)))) {
            if (newAuthorName != null && !newAuthorName.trim().isEmpty()) {
                authorId = authorService.getOrCreateAuthor(newAuthorName.trim());
            }
        } else if (authorIdParam != null && !authorIdParam.trim().isEmpty() && !"new".equalsIgnoreCase(authorIdParam)) {
            try {
                authorId = Integer.parseInt(authorIdParam.trim());
            } catch (Exception ignored) {
            }
        }

        if (authorId <= 0) {
            req.setAttribute("errorMessage", "Vui lòng chọn tác giả có sẵn hoặc nhập tên tác giả mới.");
            req.setAttribute("authorMode", authorMode);
            req.setAttribute("newAuthorName", newAuthorName);
            doGet(req, resp);
            return;
        }

        Book_24133049 book = new Book_24133049();
        book.setBookId(bookId);
        book.setIsbn(isbn.trim());
        book.setTitle(title.trim());
        book.setPublisher(publisher != null ? publisher.trim() : "");
        book.setPrice(price);
        book.setDescription(description != null ? description.trim() : "");
        book.setPublishDate(publishDate);
        book.setCoverImage(coverImage != null ? coverImage.trim() : "");
        book.setQuantity(quantity);

        boolean updated = bookService.updateBook(book, authorId);
        if (updated) {
            req.getSession(true).setAttribute("adminSuccessMessage", "Cập nhật thông tin sách thành công!");
            resp.sendRedirect(req.getContextPath() + "/admin/books");
        } else {
            req.setAttribute("errorMessage", "Cập nhật sách thất bại. Vui lòng thử lại.");
            req.setAttribute("book", book);
            req.setAttribute("authorMode", authorMode);
            req.setAttribute("newAuthorName", newAuthorName);
            req.setAttribute("authors", authorService.getAllAuthors());
            resp.setContentType("text/html;charset=UTF-8");
            req.getRequestDispatcher("/WEB-INF/views/admin/book-edit.jsp").include(req, resp);
        }
    }
}
