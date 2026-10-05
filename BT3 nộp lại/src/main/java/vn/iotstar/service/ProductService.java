package vn.iotstar.service;

import vn.iotstar.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    List<Product> findLatest(int limit);

    List<Product> findPage(int page, int pageSize);

    long count();

    Product findById(Long id);

    void insert(Product product);

    void update(Product product);

    void delete(Long id);
}
