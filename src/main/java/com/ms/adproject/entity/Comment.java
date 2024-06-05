package com.ms.adproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Userid", nullable = false)
    private String userid;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private String content;
    private Integer score;

    @Column(nullable = false)
    private Integer likes = 0;
    private Timestamp createdAt;
}
