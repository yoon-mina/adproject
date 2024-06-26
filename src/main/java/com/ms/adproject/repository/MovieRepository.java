package com.ms.adproject.repository;

import com.ms.adproject.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findAllByOrderByDateDesc();
    boolean existsByTitle(String title);

    @Query("SELECT m FROM Movie m WHERE lower(m.title) LIKE lower(concat('%', :keyword, '%')) " +
            "OR lower(m.director) LIKE lower(concat('%', :keyword, '%')) " +
            "OR EXISTS (SELECT c FROM Comment c WHERE c.movie = m AND lower(c.content) LIKE lower(concat('%', :keyword, '%')))" +
            "OR lower(m.genre) LIKE lower(concat('%', :keyword, '%'))")

    Page<Movie> searchMoviesAndComments(@Param("keyword") String keyword, Pageable pageable);

    Page<Movie> findAll(Pageable pageable);
}

