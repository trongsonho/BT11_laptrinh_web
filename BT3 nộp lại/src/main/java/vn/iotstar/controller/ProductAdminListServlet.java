package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.ProductServiceImpl;

import java.io.IOException;

@WebServlet("/admin/products")
public class ProductAdminListServlet
        extends HttpServlet {

    private final ProductService productService =
            new ProductServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        if (!vn.iotstar.util.AuthUtil.checkAdmin(req, resp)) {
            return;
        }

        req.setAttribute(
                "products",
                productService.findAll());

        req.getRequestDispatcher(
                "/views/admin/list-product.jsp")
                .forward(req, resp);
    }
}
