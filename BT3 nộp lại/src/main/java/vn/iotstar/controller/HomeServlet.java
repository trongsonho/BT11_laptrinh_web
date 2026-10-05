package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.ProductServiceImpl;

import java.io.IOException;

@WebServlet(urlPatterns = {"", "/"})
public class HomeServlet
        extends HttpServlet {

    private final ProductService productService =
            new ProductServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute(
                "products",
                productService.findLatest(10));

        req.getRequestDispatcher(
                "/home.jsp")
                .forward(req, resp);
    }
}
