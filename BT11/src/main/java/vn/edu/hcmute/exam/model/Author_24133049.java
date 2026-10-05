package vn.edu.hcmute.exam.model;

import java.io.Serializable;
import java.sql.Date;

public class Author_24133049 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int authorId;
    private String authorName;
    private Date dateOfBirth;

    public Author_24133049() {
    }

    public Author_24133049(int authorId, String authorName, Date dateOfBirth) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.dateOfBirth = dateOfBirth;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "Author_24133049{" +
                "authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
