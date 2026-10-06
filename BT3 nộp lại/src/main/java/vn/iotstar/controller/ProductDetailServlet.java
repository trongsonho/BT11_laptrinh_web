package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.entity.Product;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.ProductServiceImpl;

import java.io.IOException;

@WebServlet("/product/detail")
public class ProductDetailServlet
        extends HttpServlet {

    private final ProductService productService =
            new ProductServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam =
                req.getParameter("id");

        if (idParam == null) {
            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST);

            return;
        }

        try {

            Long id =
                    Long.parseLong(idParam);

            Product product =
                    productService.findById(id);

            if (product == null) {

                resp.sendError(
                        HttpServletResponse.SC_NOT_FOUND);

                return;
            }

            req.setAttribute(
                    "product",
                    product);

            req.getRequestDispatcher(
                    "/product-detail.jsp")
                    .forward(req, resp);

        } catch (NumberFormatException e) {

            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
