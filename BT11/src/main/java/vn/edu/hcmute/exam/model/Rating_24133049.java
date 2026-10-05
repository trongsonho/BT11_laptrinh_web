package vn.edu.hcmute.exam.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Rating_24133049 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;
    private int bookId;
    private int rating;
    private String reviewText;
    private Timestamp reviewDate;

    // Phục vụ hiển thị chi tiết sách (Câu 4: [users]: [review_text])
    private String userName;
    private String userEmail;
    private String bookTitle;

    public Rating_24133049() {
    }

    public Rating_24133049(int userId, int bookId, int rating, String reviewText) {
        this.userId = userId;
        this.bookId = bookId;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Timestamp getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Timestamp reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}
