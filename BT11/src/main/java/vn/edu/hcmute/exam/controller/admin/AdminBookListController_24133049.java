package vn.edu.hcmute.exam.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.exam.model.Book_24133049;
import vn.edu.hcmute.exam.service.BookServiceImpl_24133049;
import vn.edu.hcmute.exam.service.IBookService_24133049;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminBookListController_24133049", urlPatterns = {"/admin/books"})
public class AdminBookListController_24133049 extends HttpServlet {

    private static final int ADMIN_PAGE_SIZE = 5;

    private final IBookService_24133049 bookService = new BookServiceImpl_24133049();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {
            }
        }
        if (page < 1) page = 1;

        int totalBooks = bookService.countAllBooks();
        int totalPages = (int) Math.ceil((double) totalBooks / ADMIN_PAGE_SIZE);
        if (totalPages < 1) totalPages = 1;
        if (page > totalPages) page = totalPages;

        List<Book_24133049> books = bookService.getAllBooks(page, ADMIN_PAGE_SIZE);

        HttpSession session = req.getSession(false);
        if (session != null) {
            String successMsg = (String) session.getAttribute("adminSuccessMessage");
            if (successMsg != null) {
                req.setAttribute("successMessage", successMsg);
                session.removeAttribute("adminSuccessMessage");
            }
            String errorMsg = (String) session.getAttribute("adminErrorMessage");
            if (errorMsg != null) {
                req.setAttribute("errorMessage", errorMsg);
                session.removeAttribute("adminErrorMessage");
            }
        }

        req.setAttribute("books", books);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalBooks", totalBooks);

        resp.setContentType("text/html;charset=UTF-8");
        req.getRequestDispatcher("/WEB-INF/views/admin/book-list.jsp").include(req, resp);
    }
}
