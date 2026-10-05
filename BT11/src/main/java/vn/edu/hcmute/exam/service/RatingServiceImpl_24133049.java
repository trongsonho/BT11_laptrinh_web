package vn.edu.hcmute.exam.service;

import vn.edu.hcmute.exam.dao.IRatingDao_24133049;
import vn.edu.hcmute.exam.dao.RatingDaoImpl_24133049;
import vn.edu.hcmute.exam.model.Rating_24133049;

import java.util.Collections;
import java.util.List;

public class RatingServiceImpl_24133049 implements IRatingService_24133049 {

    private final IRatingDao_24133049 ratingDao;

    public RatingServiceImpl_24133049() {
        this.ratingDao = new RatingDaoImpl_24133049();
    }

    public RatingServiceImpl_24133049(IRatingDao_24133049 ratingDao) {
        this.ratingDao = ratingDao;
    }

    @Override
    public List<Rating_24133049> getRatingsByBookId(int bookId) {
        if (bookId <= 0) {
            return Collections.emptyList();
        }
        return ratingDao.findByBookId(bookId);
    }

    @Override
    public Rating_24133049 getUserRatingForBook(int userId, int bookId) {
        if (userId <= 0 || bookId <= 0) {
            return null;
        }
        return ratingDao.findByUserAndBook(userId, bookId);
    }

    @Override
    public boolean submitRating(int userId, int bookId, int rating, String reviewText) {
        if (userId <= 0 || bookId <= 0) {
            return false;
        }
        if (rating < 1) rating = 1;
        if (rating > 5) rating = 5;

        Rating_24133049 r = new Rating_24133049(userId, bookId, rating, reviewText);
        return ratingDao.insertOrUpdate(r);
    }

    @Override
    public int getReviewCountByBookId(int bookId) {
        if (bookId <= 0) return 0;
        return ratingDao.countByBookId(bookId);
    }
}
