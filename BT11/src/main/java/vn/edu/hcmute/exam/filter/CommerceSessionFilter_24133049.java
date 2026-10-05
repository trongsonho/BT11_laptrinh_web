package vn.edu.hcmute.exam.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import vn.edu.hcmute.exam.model.*;
import vn.edu.hcmute.exam.util.CommerceRequest_24133049;
import java.io.IOException;

public class CommerceSessionFilter_24133049 implements Filter {
    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        synchronized (session) {
            if (session.getAttribute("commerceCsrf") == null) session.setAttribute("commerceCsrf", CommerceRequest_24133049.token());
            User_24133049 user = (User_24133049) session.getAttribute("currentUser");
            Cart_24133049 cart = (Cart_24133049) session.getAttribute("shoppingCart");
            if (cart != null && (user == null || user.getId() != cart.getUserId() || user.isAdmin() || !user.isActive())) {
                session.removeAttribute("shoppingCart"); cart = null;
            }
            req.setAttribute("cartQuantity", cart == null ? 0L : cart.getTotalQuantity());
        }
        chain.doFilter(request, response);
    }
}
