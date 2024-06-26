package com.ms.adproject.repository;

import com.ms.adproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMovieIdOrderByScoreDesc(Long movieId);

    List<Comment> findByMovieIdOrderByCreatedAtAsc(Long movieId);

    List<Comment> findByMovieId(Long movieId);

    List<Comment> findByMovieIdOrderByLikesDesc(Long movieId);
}
