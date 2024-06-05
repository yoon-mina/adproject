package com.ms.adproject.repository;

import com.ms.adproject.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAllByOrderByDateDesc();
    List<Movie> findAllByOrderByCreatedAtAsc();
    List<Movie> findAllByOrderByRatingDesc();
    boolean existsByTitle(String title);
}
