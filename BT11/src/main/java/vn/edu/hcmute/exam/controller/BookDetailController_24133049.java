package vn.edu.hcmute.exam.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.exam.model.Book_24133049;
import vn.edu.hcmute.exam.model.Rating_24133049;
import vn.edu.hcmute.exam.model.User_24133049;
import vn.edu.hcmute.exam.service.BookServiceImpl_24133049;
import vn.edu.hcmute.exam.service.IBookService_24133049;
import vn.edu.hcmute.exam.service.IRatingService_24133049;
import vn.edu.hcmute.exam.service.RatingServiceImpl_24133049;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "BookDetailController_24133049", urlPatterns = {"/book-detail"})
public class BookDetailController_24133049 extends HttpServlet {

    private final IBookService_24133049 bookService = new BookServiceImpl_24133049();
    private final IRatingService_24133049 ratingService = new RatingServiceImpl_24133049();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        int bookId;
        try {
            bookId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Book_24133049 book = bookService.getBookById(bookId);
        if (book == null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        List<Rating_24133049> ratings = ratingService.getRatingsByBookId(bookId);

        HttpSession session = req.getSession(false);
        User_24133049 currentUser = (session != null) ? (User_24133049) session.getAttribute("currentUser") : null;
        Rating_24133049 userRating = null;
        if (currentUser != null) {
            userRating = ratingService.getUserRatingForBook(currentUser.getId(), bookId);
        }

        req.setAttribute("book", book);
        req.setAttribute("ratings", ratings);
        req.setAttribute("userRating", userRating);

        resp.setContentType("text/html;charset=UTF-8");
        req.getRequestDispatcher("/WEB-INF/views/book-detail.jsp").include(req, resp);
    }
}
