package vn.edu.hcmute.exam.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmute.exam.service.BookServiceImpl_24133049;
import vn.edu.hcmute.exam.service.IBookService_24133049;

import java.io.IOException;

@WebServlet(name = "AdminBookDeleteController_24133049", urlPatterns = {"/admin/books/delete"})
public class AdminBookDeleteController_24133049 extends HttpServlet {

    private final IBookService_24133049 bookService = new BookServiceImpl_24133049();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        boolean deleted;
        try { deleted = bookService.deleteBook(bookId); }
        catch (IllegalStateException e) {
            req.getSession(true).setAttribute("adminErrorMessage", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/admin/books"); return;
        }
        if (deleted) {
            req.getSession(true).setAttribute("adminSuccessMessage", "Đã xóa cuốn sách (ID: " + bookId + ") thành công!");
        } else {
            req.getSession(true).setAttribute("adminErrorMessage", "Xóa cuốn sách thất bại do có ràng buộc dữ liệu hoặc lỗi hệ thống.");
        }

        resp.sendRedirect(req.getContextPath() + "/admin/books");
    }
}
