package vn.edu.hcmute.exam.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Book_24133049 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int bookId;
    private String isbn;
    private String title;
    private String publisher;
    private BigDecimal price;
    private String description;
    private Date publishDate;
    private String coverImage;
    private int quantity;

    // Các thuộc tính mở rộng phục vụ hiển thị
    private int reviewCount;
    private double averageRating;
    private List<Author_24133049> authors = new ArrayList<>();
    private String authorNames;
    private int primaryAuthorId;

    public Book_24133049() {
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public List<Author_24133049> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author_24133049> authors) {
        this.authors = authors;
    }

    public String getAuthorNames() {
        if (authorNames != null && !authorNames.isEmpty()) {
            return authorNames;
        }
        if (authors != null && !authors.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < authors.size(); i++) {
                sb.append(authors.get(i).getAuthorName());
                if (i < authors.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
        return "";
    }

    public void setAuthorNames(String authorNames) {
        this.authorNames = authorNames;
    }

    public int getPrimaryAuthorId() {
        return primaryAuthorId;
    }

    public void setPrimaryAuthorId(int primaryAuthorId) {
        this.primaryAuthorId = primaryAuthorId;
    }
}
