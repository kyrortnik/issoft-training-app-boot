package com.issoft.ticketstoreapp.controller;

import com.issoft.ticketstoreapp.dto.HallDTO;
import com.issoft.ticketstoreapp.service.HallService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "api/v1/halls", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class HallController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HallController.class);
    private final HallService hallService;

    @Secured({"ROLE_CINEMA_ADMIN","ROLE_ADMIN"})
    @Operation(summary = "Create hall")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hall created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HallDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @PostMapping
    public HallDTO createHall(@RequestBody HallDTO hallDTO) {
        LOGGER.debug("Entering HallController.createHall()");

        HallDTO savedOrUpdatedHall = this.hallService.saveHall(hallDTO);

        LOGGER.debug("Exiting HallController.createHall()");
        return savedOrUpdatedHall;
    }

    @Secured({"ROLE_CINEMA_ADMIN","ROLE_ADMIN"})
    @Operation(summary = "Delete hall")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hall deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Incorrect request",
                    content = @Content)})
    @DeleteMapping("/{hallId}")
    public ResponseEntity<String> deleteHall(@PathVariable Long hallId) {
        LOGGER.debug("Entering HallController.deleteHall()");

        this.hallService.deleteHall(hallId);

        LOGGER.debug("Exiting HallController.deleteHall()");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}