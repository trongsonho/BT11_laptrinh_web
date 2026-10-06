package vn.edu.hcmute.exam.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmute.exam.model.OrderStatus_24133049;
import vn.edu.hcmute.exam.service.OrderService_24133049;
import vn.edu.hcmute.exam.util.CommerceRequest_24133049;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="OrderHistoryController_24133049", urlPatterns={"/order-history"})
public class OrderHistoryController_24133049 extends HttpServlet {
    private final OrderService_24133049 service = new OrderService_24133049();
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (req.getSession()) {
            resp.setHeader("Cache-Control", "no-store");
            try {
                var user = CommerceRequest_24133049.requireUser(req, resp); if (user == null) return;
                List<String> messages = new ArrayList<>();
                OrderStatus_24133049 status = null;
                try { status = OrderStatus_24133049.filter(req.getParameter("status")); }
                catch (IllegalArgumentException e) { messages.add(e.getMessage()); }
                long page = 1;
                try { page = OrderService_24133049.parsePage(req.getParameter("page")); }
                catch (IllegalArgumentException e) { messages.add(e.getMessage()); }
                req.setAttribute("history", service.history(user.getId(), status, page));
                req.setAttribute("statusOptions", OrderStatus_24133049.values());
                req.setAttribute("selectedStatus", status == null ? "" : status.getCode());
                req.setAttribute("selectedStatusLabel", status == null ? "Tất cả" : status.getLabel());
                req.setAttribute("historyMessages", messages);
                resp.setContentType("text/html;charset=UTF-8");
                req.getRequestDispatcher("/WEB-INF/views/order-history.jsp").include(req, resp);
            } catch (SQLException e) { log("Cannot read order history", e); resp.sendError(503, "Không thể đọc lịch sử đơn hàng. Vui lòng thử lại."); }
        }
    }
}
