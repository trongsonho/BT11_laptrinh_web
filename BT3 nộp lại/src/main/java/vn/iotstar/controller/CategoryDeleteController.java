package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;

@WebServlet(urlPatterns = { "/admin/category/delete" })
public class CategoryDeleteController extends HttpServlet {

    private static final long serialVersionUID = 1L;

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

        String id = req.getParameter("id");

        if (id == null || id.trim().isEmpty()) {

            resp.sendRedirect(
                    req.getContextPath()
                    + "/admin/category/list"
            );

            return;
        }

        try {

            int categoryId =
                    Integer.parseInt(id);

            Category category =
                    categoryService.get(categoryId);

            if (category != null) {

                categoryService.delete(categoryId);
            }

        } catch (NumberFormatException e) {

            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();
        }

        resp.sendRedirect(
                req.getContextPath()
                + "/admin/category/list"
        );
    }
}