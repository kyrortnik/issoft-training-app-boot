package com.issoft.ticketstoreapp.service;

import com.issoft.ticketstoreapp.annotation.Audit;
import com.issoft.ticketstoreapp.dto.MovieDTO;
import com.issoft.ticketstoreapp.exception.MovieNotFoundException;
import com.issoft.ticketstoreapp.mapper.MovieMapper;
import com.issoft.ticketstoreapp.model.Movie;
import com.issoft.ticketstoreapp.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MovieService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieService.class);
    private static final String NO_MOVIE_MESSAGE = "No movie with id [%s] exists";

    private final MovieRepository movieRepository;
    private final MovieMapper mapper;
//    @Autowired
//    public MovieService(MovieRepository movieRepository, MovieMapper mapper) {
//        this.movieRepository = movieRepository;
//        this.mapper = mapper;
//    }

    @Audit
    public MovieDTO findMovieById(Long movieId) {
        LOGGER.debug("Entering MovieService.findMovieById()");

        MovieDTO movieDTO = this.mapper.toDto(this.movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(String.format(NO_MOVIE_MESSAGE, movieId))));

        LOGGER.debug("Exiting MovieService.findMovieById");
        return movieDTO;
    }

    @Audit
    public List<MovieDTO> findMovies() {
        LOGGER.debug("Entering MovieService.findMovies()");

        List<MovieDTO> movies = this.movieRepository
                .findAll()
                .stream()
                .map(this.mapper::toDto)
                .collect(Collectors.toList());

        LOGGER.debug("Exiting MovieService.findMovies()");
        return movies;
    }

    @Audit
    public MovieDTO saveMovie(MovieDTO movieDTO) {
        LOGGER.debug("Entering MovieService.saveMovie()");
        Movie movie = this.mapper.toMovie(movieDTO);

        MovieDTO savedOrUpdatedMovie = this.mapper.toDto(this.movieRepository.save(movie));

        LOGGER.debug("Exiting MovieService.saveMovie()");
        return savedOrUpdatedMovie;
    }

    @Audit
    public MovieDTO updateMovie(MovieDTO movieDTO, Long movieId) {
        LOGGER.debug("Entering MovieService.updateMovie()");
        Movie movie = this.mapper.toMovie(movieDTO);
        MovieDTO updatedMovie;

        if (this.movieRepository.existsById(movieId)) {
            movie.setId(movieId);
            updatedMovie = this.mapper.toDto(this.movieRepository.save(movie));
        } else {
            LOGGER.error(String.format(NO_MOVIE_MESSAGE, movieId));
            throw new MovieNotFoundException(String.format(NO_MOVIE_MESSAGE, movieId));
        }
        LOGGER.debug("Exiting MovieService.updateMovie()");
        return updatedMovie;
    }

    @Audit
    public void deleteMovie(Long movieId) {
        LOGGER.debug("Entering MovieService.deleteMovie()");

        this.movieRepository.deleteById(movieId);

        LOGGER.debug("Exiting MovieService.deleteMovie()");
    }
}