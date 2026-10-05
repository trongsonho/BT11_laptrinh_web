package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.ProductServiceImpl;

import java.io.IOException;

@WebServlet("/product")
public class ProductListServlet
        extends HttpServlet {

    private final ProductService productService =
            new ProductServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        int page = 1;

        String pageParam =
                req.getParameter("page");

        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        int pageSize = 6;

        long total =
                productService.count();

        int totalPages =
                (int) Math.ceil(
                        (double) total
                                / pageSize);

        if (totalPages > 0
                && page > totalPages) {
            page = totalPages;
        }

        req.setAttribute(
                "products",
                productService.findPage(
                        page,
                        pageSize));

        req.setAttribute(
                "currentPage",
                page);

        req.setAttribute(
                "totalPages",
                totalPages);

        req.getRequestDispatcher(
                "/product.jsp")
                .forward(req, resp);
    }
}
