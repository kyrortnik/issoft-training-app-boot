package com.issoft.ticketstoreapp.controller;

import com.issoft.ticketstoreapp.dto.MovieDTO;
import com.issoft.ticketstoreapp.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "api/v1/movies", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MovieController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);
    private final MovieService movieService;

    @Secured({"ROLE_MOVIE_ADMIN", "ROLE_USER"})
    @Operation(summary = "Get a movie by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie the cinema",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Movie not found",
                    content = @Content)})
    @GetMapping("/{movieId}")
    public MovieDTO getMovie(@PathVariable Long movieId) {
        LOGGER.debug("Entering MovieController.getMovie()");

        MovieDTO movieDTO = this.movieService.findMovieById(movieId);

        LOGGER.debug("Exiting MovieController.getMovie()");
        return movieDTO;
    }

    @Secured({"ROLE_MOVIE_ADMIN", "ROLE_USER"})
    @Operation(summary = "Get all movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found movies",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MovieDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Movies not found",
                    content = @Content)})
    @GetMapping
    public List<MovieDTO> getMovies() {
        LOGGER.debug("Entering MovieController.getMovies()");

        List<MovieDTO> movies = this.movieService.findMovies();

        LOGGER.debug("Exiting MovieController.getMovies()");
        return movies;
    }

    @Secured({"ROLE_ADMIN","ROLE_CINEMA_ADMIN"})
    @Operation(summary = "Create movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public MovieDTO createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        LOGGER.debug("Entering MovieController.createMovie()");

        MovieDTO createdMovie = this.movieService.saveMovie(movieDTO);

        LOGGER.debug("Exiting MovieController.createMovie()");
        return createdMovie;
    }

    @Secured("ROLE_MOVIE_ADMIN")
    @Operation(summary = "Update movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid body",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Movie not found",
                    content = @Content)})
    @PutMapping(value = "/{movieId}", consumes = APPLICATION_JSON_VALUE)
    public MovieDTO updateMovie(@Valid @RequestBody MovieDTO movieDTO, @PathVariable Long movieId) {
        LOGGER.debug("Entering MovieController.updateMovie()");

        MovieDTO updatedMovieDTO = this.movieService.updateMovie(movieDTO, movieId);

        LOGGER.debug("Exiting MovieController.updateMovie()");
        return updatedMovieDTO;
    }

    @Secured({"ROLE_MOVIE_ADMIN","ROLE_ADMIN"})
    @Operation(summary = "Delete movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Incorrect request",
                    content = @Content)})
    @DeleteMapping("/{movieId}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long movieId) {
        LOGGER.debug("Entering MovieController.deleteMovie()");

        this.movieService.deleteMovie(movieId);

        LOGGER.debug("Exiting MovieController.deleteMovie()");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}