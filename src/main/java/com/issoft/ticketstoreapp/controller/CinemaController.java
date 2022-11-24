package com.issoft.ticketstoreapp.controller;

import com.issoft.ticketstoreapp.dto.CinemaDTO;
import com.issoft.ticketstoreapp.service.CinemaService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "api/v1/cinemas", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CinemaController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CinemaController.class);

    private final CinemaService cinemaService;

    @Secured({"ROLE_CINEMA_ADMIN", "ROLE_USER"})
    @Operation(summary = "Get a cinema by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the cinema",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CinemaDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Cinema not found",
                    content = @Content)})
    @GetMapping("/{cinemaId}")
    public CinemaDTO getCinema(@PathVariable Long cinemaId) {
        LOGGER.debug("Entering CinemaController.getCinema()");

        CinemaDTO foundCinema = this.cinemaService.findCinemaById(cinemaId);

        LOGGER.debug("Exiting CinemaController.getCinema()");
        return foundCinema;
    }

    @Secured({"ROLE_CINEMA_ADMIN", "ROLE_USER"})
    @Operation(summary = "Get all cinemas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found cinemas",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CinemaDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Cinemas not found",
                    content = @Content)})
    @GetMapping
    public List<CinemaDTO> getCinemas() {
        LOGGER.debug("Entering CinemaController.getCinemas()");

        List<CinemaDTO> cinemas = this.cinemaService.findCinemas();

        LOGGER.debug("Exiting CinemaController.getCinemas()");
        return cinemas;
    }

    @Secured({"ROLE_CINEMA_ADMIN", "ROLE_ADMIN"})
    @Operation(summary = "Create cinema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cinema created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CinemaDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public CinemaDTO createCinema(@Valid @RequestBody CinemaDTO cinemaDTO) {
        LOGGER.debug("Entering CinemaController.createCinema()");

        CinemaDTO createdCinema = this.cinemaService.saveCinema(cinemaDTO);

        LOGGER.debug("Exiting CinemaController.createCinema()");
        return createdCinema;
    }

    @Secured("ROLE_CINEMA_ADMIN")
    @Operation(summary = "Update cinema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cinema updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CinemaDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid body",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Cinema not found",
                    content = @Content)})
    @PutMapping(value = "/{cinemaId}", consumes = APPLICATION_JSON_VALUE)
    public CinemaDTO updateCinema(@Valid @RequestBody CinemaDTO cinemaDTO,
                                  @PathVariable Long cinemaId,
                                  Authentication authentication) {
        LOGGER.debug("Entering CinemaController.updateCinema()");

        CinemaDTO updatedCinema = this.cinemaService.updateCinema(cinemaDTO, cinemaId, authentication);

        LOGGER.debug("Exiting CinemaController.updateCinema()");
        return updatedCinema;
    }

    @Secured({"ROLE_CINEMA_ADMIN", "ROLE_ADMIN"})
    @Operation(summary = "Delete cinema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cinema deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Incorrect request",
                    content = @Content)})
    @DeleteMapping("/{cinemaId}")
    public ResponseEntity<String> deleteCinema(@PathVariable Long cinemaId) {
        LOGGER.debug("Entering CinemaController.deleteCinema()");

        this.cinemaService.deleteCinema(cinemaId);

        LOGGER.debug("Exiting CinemaController.deleteCinema()");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}