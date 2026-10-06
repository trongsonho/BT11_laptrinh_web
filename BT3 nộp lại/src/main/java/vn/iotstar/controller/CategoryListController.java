package vn.iotstar.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;

@WebServlet(urlPatterns = { "/admin/category/list" })
public class CategoryListController extends HttpServlet {

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

        try {

            List<Category> categoryList =
                    categoryService.getAll();

            req.setAttribute(
                    "categoryList",
                    categoryList
            );

            RequestDispatcher dispatcher =
                    req.getRequestDispatcher(
                            "/views/admin/list-category.jsp"
                    );

            dispatcher.forward(req, resp);

        } catch (Throwable e) {

            e.printStackTrace();

            resp.setContentType(
                    "text/html;charset=UTF-8"
            );

            resp.getWriter().println(
                    "<h2>Lỗi khi tải danh sách Category</h2>"
            );

            resp.getWriter().println(
                    "<pre>"
                    + e.getMessage()
                    + "</pre>"
            );
        }
    }
}