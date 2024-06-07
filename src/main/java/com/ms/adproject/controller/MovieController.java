package com.ms.adproject.controller;

import com.ms.adproject.entity.Movie;
import com.ms.adproject.entity.User;
import com.ms.adproject.repository.MovieRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;

@Controller
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/movies")
    public String getMovies(Model model,
                            @RequestParam(name = "sort", required = false) String sort,
                            @RequestParam(name = "search", required = false) String search,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<Movie> moviePage;

        if (sort != null) {
            switch (sort) {
                case "date":
                    pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
                    break;
                case "createdAt":
                    pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
                    break;
                case "rating":
                    pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "rating"));
                    break;
            }
        }

        if (search != null && !search.isEmpty()) {
            moviePage = movieRepository.searchMoviesAndComments(search, pageable);
        } else {
            moviePage = movieRepository.findAll(pageable);
        }

        int totalPages = moviePage.getTotalPages();
        int currentPage = page;
        int currentGroup = (int) Math.floor(currentPage / 10);
        int totalGroups = (int) Math.ceil((double) totalPages / 10);
        int startPage = currentGroup * 10;
        int endPage = Math.min(startPage + 9, totalPages - 1);

        model.addAttribute("movies", moviePage.getContent());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentGroup", currentGroup);
        model.addAttribute("totalGroups", totalGroups);
        return "movies/movies";
    }

    @GetMapping("/movies/new")
    public String showMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "movies/new_movie";
    }

    @PostMapping("/movies/new")
    public String addNewMovie(@ModelAttribute Movie movie, @RequestParam(name = "selectedGenres") String selectedGenres, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (movieRepository.existsByTitle(movie.getTitle())) {
            model.addAttribute("error", "이미 등록된 영화입니다.");
            return "movies/new_movie";
        }

        movie.setUserid(loggedInUser.getUserid());
        movie.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        movie.setRating(0);

        // 사용자가 선택한 장르들을 movie 객체에 설정합니다.
        movie.setGenre(selectedGenres);

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
