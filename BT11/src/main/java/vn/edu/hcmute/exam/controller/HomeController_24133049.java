package vn.edu.hcmute.exam.controller;

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
import java.util.List;

@WebServlet(name = "HomeController_24133049", urlPatterns = {"", "/home", "/books"})
public class HomeController_24133049 extends HttpServlet {

    private static final int PAGE_SIZE = 3; // Đúng 03 sản phẩm / trang theo yêu cầu Câu 3 đề thi

    private final IAuthorService_24133049 authorService = new AuthorServiceImpl_24133049();
    private final IBookService_24133049 bookService = new BookServiceImpl_24133049();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Author_24133049> authors = authorService.getAllAuthors();
        req.setAttribute("authors", authors);

        if (authors == null || authors.isEmpty()) {
            resp.setContentType("text/html;charset=UTF-8");
            req.getRequestDispatcher("/WEB-INF/views/home.jsp").include(req, resp);
            return;
        }

        // Lấy authorId được chọn (mặc định là tác giả đầu tiên)
        int authorId = authors.get(0).getAuthorId();
        String authorIdParam = req.getParameter("authorId");
        if (authorIdParam != null && !authorIdParam.trim().isEmpty()) {
            try {
                authorId = Integer.parseInt(authorIdParam);
            } catch (NumberFormatException ignored) {
            }
        }

        // Tìm đối tượng tác giả đã chọn
        Author_24133049 selectedAuthor = null;
        for (Author_24133049 a : authors) {
            if (a.getAuthorId() == authorId) {
                selectedAuthor = a;
                break;
            }
        }
        if (selectedAuthor == null) {
            selectedAuthor = authors.get(0);
            authorId = selectedAuthor.getAuthorId();
        }

        // Xử lý phân trang
        int page = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {
            }
        }
        if (page < 1) page = 1;

        int totalBooks = bookService.countBooksByAuthor(authorId);
        int totalPages = (int) Math.ceil((double) totalBooks / PAGE_SIZE);
        if (totalPages < 1) totalPages = 1;
        if (page > totalPages) page = totalPages;

        List<Book_24133049> books = bookService.getBooksByAuthor(authorId, page, PAGE_SIZE);

        req.setAttribute("selectedAuthor", selectedAuthor);
        req.setAttribute("books", books);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalBooks", totalBooks);
        resp.setContentType("text/html;charset=UTF-8");
        req.getRequestDispatcher("/WEB-INF/views/home.jsp").include(req, resp);
    }
}
