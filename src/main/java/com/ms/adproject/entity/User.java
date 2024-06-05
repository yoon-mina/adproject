package com.ms.adproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Userid", nullable = false, unique = true)
    private String userid;

    private String username;
    private String email;
    private String password;
    private Timestamp createdAt;

}