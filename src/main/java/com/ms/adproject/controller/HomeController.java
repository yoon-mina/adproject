package com.ms.adproject.controller;

import com.ms.adproject.entity.Movie;
import com.ms.adproject.repository.MovieRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final MovieRepository movieRepository;

    public HomeController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Movie> movies = movieRepository.findAllByOrderByDateDesc();
        model.addAttribute("movies", movies);
        return "movies/home";
    }

}
