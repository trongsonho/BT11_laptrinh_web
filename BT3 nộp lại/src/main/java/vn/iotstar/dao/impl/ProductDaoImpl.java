package vn.iotstar.dao.impl;

import jakarta.persistence.EntityManager;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.ProductDao;
import vn.iotstar.entity.Product;

import java.util.List;

public class ProductDaoImpl implements ProductDao {

    @Override
    public List<Product> findAll() {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT p FROM Product p LEFT JOIN FETCH p.category ORDER BY p.createdAt DESC, p.id DESC",
                    Product.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findLatest(int limit) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT p FROM Product p LEFT JOIN FETCH p.category ORDER BY p.createdAt DESC, p.id DESC",
                    Product.class
            )
            .setMaxResults(limit)
            .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findPage(int page, int pageSize) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT p FROM Product p LEFT JOIN FETCH p.category ORDER BY p.createdAt DESC, p.id DESC",
                    Product.class
            )
            .setFirstResult((page - 1) * pageSize)
            .setMaxResults(pageSize)
            .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT COUNT(p) FROM Product p",
                    Long.class
            ).getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public Product findById(Long id) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.id = :id",
                    Product.class
            ).setParameter("id", id).getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    @Override
    public void insert(Product product) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(product);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Product product) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            em.getTransaction().begin();
            em.merge(product);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            em.getTransaction().begin();

            Product product = em.find(Product.class, id);

            if (product != null) {
                em.remove(product);
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
