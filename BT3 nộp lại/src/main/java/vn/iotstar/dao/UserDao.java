package vn.iotstar.dao;

import vn.iotstar.entity.User;

public interface UserDao {

    User findByUsername(String username);

    User findByEmail(String email);

    User findById(Long id);

    void insert(User user);

    void update(User user);

    void delete(Long id);
}
