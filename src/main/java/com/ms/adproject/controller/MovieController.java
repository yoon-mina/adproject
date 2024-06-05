package com.ms.adproject.controller;
import com.ms.adproject.entity.Movie;
import com.ms.adproject.entity.User;
import com.ms.adproject.repository.MovieRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@Controller
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/movies")
    public String getMovies(Model model, @RequestParam(name = "sort", required = false) String sort) {
        List<Movie> movies;
        if (sort != null && sort.equals("date")) {
            movies = movieRepository.findAllByOrderByDateDesc();
        } else if (sort != null && sort.equals("createdAt")) {
            movies = movieRepository.findAllByOrderByCreatedAtAsc();
        } else if (sort != null && sort.equals("rating")) {
            movies = movieRepository.findAllByOrderByRatingDesc();
        } else {
            movies = movieRepository.findAllByOrderByDateDesc();
        }
        model.addAttribute("movies", movies);
        return "movies/movies";
    }


    @GetMapping("/movies/new")
    public String showMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "movies/new_movie";
    }

    @PostMapping("/movies/new")
    public String addNewMovie(@ModelAttribute Movie movie, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        if (movieRepository.existsByTitle(movie.getTitle())) {
            model.addAttribute("error", "이미 등록된 영화입니다.");
            return "movies/new_movie";
        }
        movie.setUserid(loggedInUser.getUserid());
        movie.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        movie.setRating(0);
        movieRepository.save(movie);
        return "redirect:/movies";
    }

    @PostMapping("/movies/{movieId}/delete")
    public String deleteMovie(@PathVariable Long movieId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + movieId));
        movieRepository.delete(movie);
        return "redirect:/movies";
    }
}
