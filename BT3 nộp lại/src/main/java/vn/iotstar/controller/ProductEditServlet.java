package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;
import vn.iotstar.util.Constant;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/admin/products/edit")
@MultipartConfig
public class ProductEditServlet
        extends HttpServlet {

    private final ProductService productService =
            new ProductServiceImpl();
    private final CategoryService categoryService =
            new CategoryServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        if (!vn.iotstar.util.AuthUtil.checkAdmin(req, resp)) {
            return;
        }

        String idParam =
                req.getParameter("id");

        if (idParam == null) {
            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST);

            return;
        }

        Product product =
                productService.findById(
                        Long.parseLong(idParam));

        if (product == null) {
            resp.sendError(
                    HttpServletResponse.SC_NOT_FOUND);

            return;
        }

        req.setAttribute(
                "categories",
                categoryService.getAll());

        req.setAttribute(
                "product",
                product);

        req.getRequestDispatcher(
                "/views/admin/edit-product.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        if (!vn.iotstar.util.AuthUtil.checkAdmin(req, resp)) {
            return;
        }

        req.setCharacterEncoding("UTF-8");

        Long id =
                Long.parseLong(
                        req.getParameter("id"));

        Product product =
                productService.findById(id);

        if (product == null) {
            resp.sendError(
                    HttpServletResponse.SC_NOT_FOUND);

            return;
        }

        product.setName(
                req.getParameter("name"));

        product.setPrice(
                new BigDecimal(
                        req.getParameter("price")));

        product.setQuantity(
                Integer.parseInt(
                        req.getParameter("quantity")));

        product.setDescription(
                req.getParameter("description"));

        String categoryId =
                req.getParameter("categoryId");

        if (categoryId != null
                && !categoryId.isBlank()) {

            Category category =
                    new Category();

            category.setId(
                    Long.parseLong(categoryId));

            product.setCategory(category);
        }

        Part imagePart =
                req.getPart("image");

        if (imagePart != null
                && imagePart.getSize() > 0) {

            String fileName =
                    System.currentTimeMillis()
                            + "-"
                            + imagePart.getSubmittedFileName();

            String uploadPath =
                    Constant.DIR
                            + "/product";

            java.io.File directory =
                    new java.io.File(uploadPath);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            imagePart.write(
                    uploadPath
                            + "/"
                            + fileName);

            product.setImage(
                    "product/"
                            + fileName);
        }

        productService.update(product);

        resp.sendRedirect(
                req.getContextPath()
                        + "/admin/products");
    }
}
