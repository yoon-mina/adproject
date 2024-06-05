package com.ms.adproject.controller;

import com.ms.adproject.entity.Comment;
import com.ms.adproject.entity.Movie;
import com.ms.adproject.entity.User;
import com.ms.adproject.repository.CommentRepository;
import com.ms.adproject.repository.MovieRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + movieId));
        movie.setRating(avgScore);
        movieRepository.save(movie);
    }


    @GetMapping("/movies/{movieId}/comments")
    public String getComments(@PathVariable Long movieId, Model model, @RequestParam(name = "sort", required = false) String sort) {
        List<Comment> comments;
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + movieId));
        if (sort != null && sort.equals("score")) {
            comments = commentRepository.findByMovieIdOrderByScoreDesc(movieId);
        } else {
            comments = commentRepository.findByMovieIdOrderByCreatedAtAsc(movieId);
        }
        model.addAttribute("comments", comments);
        model.addAttribute("movie", movie);
        return "movies/comments";
    }

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

    @PostMapping("/movies/{movieId}/comments/delete")
    public String deleteComment(@RequestParam(name = "commentId") Long commentId, @PathVariable Long movieId) {
        commentRepository.deleteById(commentId);
        updateMovieRating(movieId);
        return "redirect:/movies/" + movieId + "/comments";
    }
}
