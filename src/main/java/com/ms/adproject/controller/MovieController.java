package com.ms.adproject.controller;
import com.ms.adproject.entity.Movie;
import com.ms.adproject.repository.MovieRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            movies = movieRepository.findAllByOrderByCreatedAtDesc();
        } else {
            movies = movieRepository.findAll();
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
    public String addNewMovie(@ModelAttribute Movie movie) {
        movie.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        movieRepository.save(movie);
        return "redirect:/movies";
    }


}
