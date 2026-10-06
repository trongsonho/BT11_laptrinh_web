package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = { "/admin/category/add" })
public class CategoryAddController extends HttpServlet {

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

        RequestDispatcher dispatcher =
                req.getRequestDispatcher(
                        "/views/admin/add-category.jsp"
                );

        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        if (!vn.iotstar.util.AuthUtil.checkAdmin(req, resp)) {
            return;
        }

        Category category = new Category();

        DiskFileItemFactory factory =
                DiskFileItemFactory.builder().get();

        JakartaServletFileUpload<DiskFileItem, DiskFileItemFactory>
                upload =
                new JakartaServletFileUpload<>(factory);

        try {

            List<DiskFileItem> items =
                    upload.parseRequest(req);

            for (DiskFileItem item : items) {

                // =========================
                // TEXT FIELD
                // =========================
                if (item.isFormField()) {

                    if ("name".equals(item.getFieldName())) {

                        category.setName(
                                item.getString(
                                        StandardCharsets.UTF_8
                                )
                        );
                    }

                }

                // =========================
                // FILE
                // =========================
                else {

                    if ("icon".equals(item.getFieldName())
                            && item.getSize() > 0) {

                        String originalFileName =
                                item.getName();

                        if (originalFileName == null
                                || originalFileName.trim().isEmpty()) {
                            continue;
                        }

                        int index =
                                originalFileName.lastIndexOf(".");

                        String ext = "";

                        if (index >= 0
                                && index < originalFileName.length() - 1) {

                            ext =
                                    originalFileName
                                            .substring(index + 1)
                                            .toLowerCase();
                        }

                        String fileName;

                        if (!ext.isEmpty()) {

                            fileName =
                                    System.currentTimeMillis()
                                    + "."
                                    + ext;

                        } else {

                            fileName =
                                    String.valueOf(
                                            System.currentTimeMillis()
                                    );
                        }

                        // uploads/category
                        File dir =
                                new File(
                                        Constant.DIR
                                        + "/category"
                                );

                        if (!dir.exists()) {
                            dir.mkdirs();
                        }

                        File file =
                                new File(dir, fileName);

                        item.write(file.toPath());

                        category.setIcon(
                                "category/" + fileName
                        );
                    }
                }
            }

            // INSERT DATABASE
            categoryService.insert(category);

            resp.sendRedirect(
                    req.getContextPath()
                    + "/admin/category/list"
            );

        } catch (Exception e) {

            e.printStackTrace();

            resp.sendRedirect(
                    req.getContextPath()
                    + "/admin/category/list"
            );
        }
    }
}