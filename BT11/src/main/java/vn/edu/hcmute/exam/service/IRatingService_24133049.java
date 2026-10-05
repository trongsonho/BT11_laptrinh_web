package vn.edu.hcmute.exam.service;

import vn.edu.hcmute.exam.model.Rating_24133049;

import java.util.List;

public interface IRatingService_24133049 {
    List<Rating_24133049> getRatingsByBookId(int bookId);
    Rating_24133049 getUserRatingForBook(int userId, int bookId);
    boolean submitRating(int userId, int bookId, int rating, String reviewText);
    int getReviewCountByBookId(int bookId);
}
