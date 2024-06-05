package com.ms.adproject.entity;

import jakarta.persistence.*;
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


    @Column(name = "Userid", nullable = false)
    private String userid;

    @Column(unique = true)
    private String title;

    private Date date;
    private String director;
    private Timestamp createdAt;

    @OneToMany(mappedBy = "movie")
    private List<Comment> comments;
    private double rating;

}
