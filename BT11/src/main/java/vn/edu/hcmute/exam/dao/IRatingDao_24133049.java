package vn.edu.hcmute.exam.dao;

import vn.edu.hcmute.exam.model.Rating_24133049;

import java.util.List;

public interface IRatingDao_24133049 {
    List<Rating_24133049> findByBookId(int bookId);
    Rating_24133049 findByUserAndBook(int userId, int bookId);
    boolean insertOrUpdate(Rating_24133049 rating);
    int countByBookId(int bookId);
}
