package vn.iotstar.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.ProductServiceImpl;

import java.io.IOException;

@WebServlet("/admin/products/delete")
public class ProductDeleteServlet
        extends HttpServlet {

    private final ProductService productService =
            new ProductServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws IOException {

        if (!vn.iotstar.util.AuthUtil.checkAdmin(req, resp)) {
            return;
        }

        String idParam =
                req.getParameter("id");

        if (idParam != null) {

            try {

                Long id =
                        Long.parseLong(idParam);

                productService.delete(id);

            } catch (NumberFormatException ignored) {
            }
        }

        resp.sendRedirect(
                req.getContextPath()
                        + "/admin/products");
    }
}
