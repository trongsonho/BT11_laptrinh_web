package vn.edu.hcmute.exam.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.exam.model.User_24133049;
import vn.edu.hcmute.exam.service.IRatingService_24133049;
import vn.edu.hcmute.exam.service.RatingServiceImpl_24133049;

import java.io.IOException;

@WebServlet(name = "ReviewController_24133049", urlPatterns = {"/review", "/review/add"})
public class ReviewController_24133049 extends HttpServlet {

    private final IRatingService_24133049 ratingService = new RatingServiceImpl_24133049();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        User_24133049 currentUser = (session != null) ? (User_24133049) session.getAttribute("currentUser") : null;

        String bookIdParam = req.getParameter("bookId");
        if (bookIdParam == null || bookIdParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        int bookId;
        try {
            bookId = Integer.parseInt(bookIdParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        // Bắt buộc đăng nhập để đánh giá
        if (currentUser == null) {
            session = req.getSession(true);
            session.setAttribute("errorMessage", "Vui lòng đăng nhập để gửi đánh giá và nhận xét sách.");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String ratingParam = req.getParameter("rating");
        String reviewText = req.getParameter("reviewText");

        int ratingVal = 5;
        if (ratingParam != null && !ratingParam.trim().isEmpty()) {
            try {
                ratingVal = Integer.parseInt(ratingParam);
            } catch (NumberFormatException ignored) {
            }
        }

        if (reviewText == null || reviewText.trim().isEmpty()) {
            reviewText = "Người dùng đánh giá " + ratingVal + " sao.";
        }

        // Ghi hoặc cập nhật đánh giá (Composite Key: userid + bookid)
        boolean success = ratingService.submitRating(currentUser.getId(), bookId, ratingVal, reviewText.trim());

        // Sử dụng mô hình POST-Redirect-GET để chống gửi lặp form (resubmission)
        session.setAttribute("reviewSuccess", success ? "Gửi đánh giá thành công!" : "Gửi đánh giá thất bại.");
        resp.sendRedirect(req.getContextPath() + "/book-detail?id=" + bookId);
    }
}
