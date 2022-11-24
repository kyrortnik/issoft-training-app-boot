package com.issoft.ticketstoreapp.controller;

import com.issoft.ticketstoreapp.dto.SeatDTO;
import com.issoft.ticketstoreapp.service.SeatService;
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
@RequestMapping(value = "api/v1/seats", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class SeatController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeatController.class);

    private final SeatService seatService;

    @Secured({"ROLE_CINEMA_ADMIN","ROLE_ADMIN"})
    @Operation(summary = "Create seat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seat created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SeatDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @PostMapping
    public SeatDTO createSeat(@RequestBody SeatDTO seatDTO) {
        LOGGER.debug("Entering RowController.createRow()");

        SeatDTO savedOrUpdatedHall = this.seatService.saveSeat(seatDTO);

        LOGGER.debug("Exiting RowController.createRow()");
        return savedOrUpdatedHall;
    }

    @Secured({"ROLE_CINEMA_ADMIN","ROLE_ADMIN"})
    @Operation(summary = "Delete seat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seat deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Incorrect request",
                    content = @Content)})
    @DeleteMapping("/{seatId}")
    public ResponseEntity<String> deleteSeat(@PathVariable Long seatId) {
        LOGGER.debug("Entering SeatController.deleteSeat()");

        this.seatService.deleteSeat(seatId);

        LOGGER.debug("Exiting SeatController.deleteSeat()");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}