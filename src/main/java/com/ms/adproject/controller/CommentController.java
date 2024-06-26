package com.ms.adproject.controller;

import com.ms.adproject.entity.Comment;
import com.ms.adproject.entity.Movie;
import com.ms.adproject.entity.User;
import com.ms.adproject.repository.CommentRepository;
import com.ms.adproject.repository.MovieRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.List;

@Controller
public class CommentController {

    private final CommentRepository commentRepository;
    private final MovieRepository movieRepository;

    public CommentController(CommentRepository commentRepository, MovieRepository movieRepository) {
        this.commentRepository = commentRepository;
        this.movieRepository = movieRepository;
    }

    // 영화의 평균 평점 업데이트
    private void updateMovieRating(Long movieId) {
        List<Comment> comments = commentRepository.findByMovieId(movieId);
        double totalScore = 0;
        for (Comment c : comments) {
            totalScore += c.getScore();
        }
        double avgScore = 0;
        if (!comments.isEmpty()) {
            avgScore = totalScore / comments.size();
        }
        avgScore = Double.parseDouble(String.format("%.1f", avgScore));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + movieId));
        movie.setRating(avgScore);
        movieRepository.save(movie);
    }


    // 특정 영화의 댓글 목록 조회
    @GetMapping("/movies/{movieId}/comments")
    public String getComments(@PathVariable Long movieId, Model model, @RequestParam(name = "sort", required = false) String sort) {
        List<Comment> comments;
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + movieId));
        if (sort != null && sort.equals("score")) {
            comments = commentRepository.findByMovieIdOrderByScoreDesc(movieId);
        } else if (sort != null && sort.equals("likes")) {
            comments = commentRepository.findByMovieIdOrderByLikesDesc(movieId);
        } else {
            comments = commentRepository.findByMovieIdOrderByCreatedAtAsc(movieId);
        }
        model.addAttribute("comments", comments);
        model.addAttribute("movie", movie);
        return "movies/comments";
    }

    // 댓글 추가
    @PostMapping("/movies/{movieId}/comments")
    public String addComment(@PathVariable Long movieId, Comment comment, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + movieId));
        comment.setMovie(movie);
        comment.setUserid(loggedInUser.getUserid());
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        commentRepository.save(comment);

        updateMovieRating(movieId);
        movieRepository.save(movie);

        return "redirect:/movies/" + movieId + "/comments";
    }

    // 댓글 삭제
    @PostMapping("/movies/{movieId}/comments/delete")
    public String deleteComment(@RequestParam(name = "commentId") Long commentId, @PathVariable Long movieId) {
        commentRepository.deleteById(commentId);
        updateMovieRating(movieId);
        return "redirect:/movies/" + movieId + "/comments";
    }

    // 댓글 추천
    @PostMapping("/movies/{movieId}/comments/{commentId}/like")
    public ResponseEntity<String> likeComment(@PathVariable Long movieId, @PathVariable Long commentId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String likedCommentAttribute = "likedComment_" + commentId;

        Boolean alreadyLiked = (Boolean) session.getAttribute(likedCommentAttribute);
        if (alreadyLiked != null && alreadyLiked) {
            return ResponseEntity.badRequest().body("이미 추천한 댓글입니다.");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + commentId));

        if (comment.getLikes() == null) {
            comment.setLikes(0);
        }
        comment.setLikes(comment.getLikes() + 1);
        commentRepository.save(comment);

        session.setAttribute(likedCommentAttribute, true);
        return ResponseEntity.ok("Liked");
    }

}
