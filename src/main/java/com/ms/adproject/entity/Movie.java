package com.ms.adproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Date date;
    private String director;
    private Timestamp createdAt;

    @OneToMany(mappedBy = "movie")
    private List<Comment> comments;
    private double rating;

    public void updateRating() {
        if (comments == null || comments.isEmpty()) {
            this.rating = 0;
        } else {
            double totalScore = 0;
            for (Comment comment : comments) {
                totalScore += comment.getScore();
            }
            this.rating = totalScore / comments.size();
        }
    }

    public Double getRating() {
        if (rating == 0) {
            return null;
        }
        return Double.valueOf(String.format("%.2f", rating));
    }
}
