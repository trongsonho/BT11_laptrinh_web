package vn.iotstar.service.impl;

import java.io.File;
import java.util.List;

import vn.iotstar.dao.CategoryDao;
import vn.iotstar.dao.impl.CategoryDaoImpl;
import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.util.Constant;

public class CategoryServiceImpl
        implements CategoryService {

    private final CategoryDao categoryDao =
            new CategoryDaoImpl();

    @Override
    public void insert(Category category) {

        categoryDao.insert(category);
    }

    @Override
    public void edit(Category category) {

        Category oldCategory =
                categoryDao.get(category.getId());

        if (oldCategory == null) {
            return;
        }

        if (category.getIcon() != null
                && !category.getIcon().isEmpty()) {

            String oldIcon =
                    oldCategory.getIcon();

            if (oldIcon != null
                    && !oldIcon.isEmpty()
                    && !oldIcon.equals(
                            category.getIcon())) {

                File file =
                        new File(
                                Constant.DIR,
                                oldIcon
                        );

                if (file.exists()) {
                    file.delete();
                }
            }
        }

        categoryDao.edit(category);
    }

    @Override
    public void delete(int id) {

        Category category =
                categoryDao.get(id);

        if (category == null) {
            return;
        }

        if (category.getIcon() != null
                && !category.getIcon().isEmpty()) {

            File file =
                    new File(
                            Constant.DIR,
                            category.getIcon()
                    );

            if (file.exists()) {
                file.delete();
            }
        }

        categoryDao.delete(id);
    }

    @Override
    public Category get(int id) {

        return categoryDao.get(id);
    }

    @Override
    public Category get(String name) {

        return categoryDao.get(name);
    }

    @Override
    public List<Category> getAll() {

        return categoryDao.getAll();
    }

    @Override
    public List<Category> search(
            String keyword) {

        return categoryDao.search(keyword);
    }
}