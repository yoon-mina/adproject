package com.ms.adproject.controller;
import com.ms.adproject.entity.Movie;
import com.ms.adproject.repository.MovieRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.util.List;

@Controller
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/movies")
    public String getMovies(Model model) {
        List<Movie> movies = movieRepository.findAll();
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
        movie.setCreatedAt(new Timestamp(System.currentTimeMillis())); // 현재 시간을 설정
        movieRepository.save(movie); // 영화 저장
        return "redirect:/movies"; // 영화 목록 페이지로 리다이렉트
    }
}
