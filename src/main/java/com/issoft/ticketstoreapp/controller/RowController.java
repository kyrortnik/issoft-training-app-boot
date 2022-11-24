package com.issoft.ticketstoreapp.controller;

import com.issoft.ticketstoreapp.dto.RowDTO;
import com.issoft.ticketstoreapp.service.RowService;
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
@RequestMapping(value = "api/v1/rows", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RowController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RowController.class);

    private final RowService rowService;

    @Secured({"ROLE_CINEMA_ADMIN","ROLE_ADMIN"})
    @Operation(summary = "Create row")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Row created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RowDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @PostMapping
    public RowDTO createRow(@RequestBody RowDTO rowDTO) {
        LOGGER.debug("Entering RowController.createRow()");

        RowDTO savedOrUpdatedHall = this.rowService.saveRow(rowDTO);

        LOGGER.debug("Exiting RowController.createRow()");
        return savedOrUpdatedHall;
    }

    @Secured({"ROLE_CINEMA_ADMIN","ROLE_ADMIN"})
    @Operation(summary = "Delete row")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Row deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Incorrect request",
                    content = @Content)})
    @DeleteMapping("/{rowId}")
    public ResponseEntity<String> deleteRow(@PathVariable Long rowId) {
        LOGGER.debug("Entering RowController.deleteRow()");

        this.rowService.deleteRow(rowId);

        LOGGER.debug("Exiting RowController.deleteRow()");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}