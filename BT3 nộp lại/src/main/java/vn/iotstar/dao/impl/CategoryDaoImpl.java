package vn.iotstar.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.CategoryDao;
import vn.iotstar.entity.Category;

public class CategoryDaoImpl implements CategoryDao {

    @Override
    public void insert(Category category) {

        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {

            transaction.begin();

            em.persist(category);

            transaction.commit();

        } catch (Exception e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            e.printStackTrace();
            throw e;

        } finally {

            em.close();
        }
    }

    @Override
    public void edit(Category category) {

        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {

            transaction.begin();

            em.merge(category);

            transaction.commit();

        } catch (Exception e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            e.printStackTrace();
            throw e;

        } finally {

            em.close();
        }
    }

    @Override
    public void delete(int id) {

        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {

            transaction.begin();

            Category category =
                    em.find(Category.class, id);

            if (category != null) {
                em.remove(category);
            }

            transaction.commit();

        } catch (Exception e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            e.printStackTrace();
            throw e;

        } finally {

            em.close();
        }
    }

    @Override
    public Category get(int id) {

        EntityManager em = JpaConfig.getEntityManager();

        try {

            return em.find(Category.class, id);

        } finally {

            em.close();
        }
    }

    @Override
    public Category get(String name) {

        EntityManager em = JpaConfig.getEntityManager();

        try {

            String jpql =
                    "SELECT c FROM Category c " +
                    "WHERE c.name = :name";

            TypedQuery<Category> query =
                    em.createQuery(
                            jpql,
                            Category.class
                    );

            query.setParameter("name", name);

            List<Category> result =
                    query.getResultList();

            if (result.isEmpty()) {
                return null;
            }

            return result.get(0);

        } finally {

            em.close();
        }
    }

    @Override
    public List<Category> getAll() {

        EntityManager em = JpaConfig.getEntityManager();

        try {

            TypedQuery<Category> query =
                    em.createNamedQuery(
                            "Category.findAll",
                            Category.class
                    );

            return query.getResultList();

        } finally {

            em.close();
        }
    }

    @Override
    public List<Category> search(String keyword) {

        EntityManager em = JpaConfig.getEntityManager();

        try {

            String jpql =
                    "SELECT c FROM Category c " +
                    "WHERE c.name LIKE :keyword " +
                    "ORDER BY c.id DESC";

            TypedQuery<Category> query =
                    em.createQuery(
                            jpql,
                            Category.class
                    );

            query.setParameter(
                    "keyword",
                    "%" + keyword + "%"
            );

            return query.getResultList();

        } finally {

            em.close();
        }
    }
}