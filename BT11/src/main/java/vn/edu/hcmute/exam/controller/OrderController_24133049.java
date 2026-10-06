package vn.edu.hcmute.exam.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmute.exam.service.OrderService_24133049;
import vn.edu.hcmute.exam.util.CommerceRequest_24133049;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name="OrderController_24133049", urlPatterns={"/order", "/order-success"})
public class OrderController_24133049 extends HttpServlet {
    private final OrderService_24133049 service = new OrderService_24133049();
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (req.getSession()) {
            resp.setHeader("Cache-Control", "no-store");
            try {
                var user = CommerceRequest_24133049.requireUser(req, resp); if (user == null) return;
                long id;
                try { id = Long.parseLong(req.getParameter("id")); if (id < 1) throw new NumberFormatException(); }
                catch (NumberFormatException e) { resp.sendError(404, "Không tìm thấy đơn hàng."); return; }
                var order = service.findOwnedOrder(id, user.getId());
                if (order == null) { resp.sendError(404, "Không tìm thấy đơn hàng của bạn."); return; }
                req.setAttribute("order", order); req.setAttribute("orderSuccess", "/order-success".equals(req.getServletPath()));
                resp.setContentType("text/html;charset=UTF-8"); req.getRequestDispatcher("/WEB-INF/views/order.jsp").include(req, resp);
            } catch (SQLException e) { log("Cannot read owned order", e); resp.sendError(503, "Không thể đọc đơn hàng. Vui lòng thử lại."); }
        }
    }
}
