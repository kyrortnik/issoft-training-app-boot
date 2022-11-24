package com.issoft.ticketstoreapp.controller;

import com.issoft.ticketstoreapp.dto.TicketDTO;
import com.issoft.ticketstoreapp.service.TicketService;
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
@RequestMapping(value = "api/v1/tickets", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TicketController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketController.class);
    public static final String NO_PERMISSION_MESSAGE = "You don't have permission to perform this operation";

    private final TicketService ticketService;

    @Secured({"ROLE_CINEMA_ADMIN", "ROLE_USER"})
    @Operation(summary = "Get a ticket by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found ticket",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Ticket not found",
                    content = @Content)})
    @GetMapping("/{ticketId}")
    public TicketDTO getTicket(@PathVariable Long ticketId) {
        LOGGER.debug("Entering TicketController.getTicket()");

        TicketDTO ticketDTO = this.ticketService.findTicketById(ticketId);

        LOGGER.debug("Exiting TicketController.getTicket()");
        return ticketDTO;
    }

    @Secured({"ROLE_CINEMA_ADMIN", "ROLE_USER"})
    @Operation(summary = "Get all tickets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found tickets",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TicketDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Tickets not found",
                    content = @Content)})
    @GetMapping
    public List<TicketDTO> getTickets() {
        LOGGER.debug("Entering TicketController.getTickets()");

        List<TicketDTO> tickets = this.ticketService.findTickets();

        LOGGER.debug("Exiting TicketController.getTickets()");
        return tickets;
    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @Operation(summary = "Create ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public TicketDTO createTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        LOGGER.debug("Entering TicketController.createTicket()");

        TicketDTO createdTicket = this.ticketService.saveTicket(ticketDTO);

        LOGGER.debug("Exiting TicketController.createTicket()");
        return createdTicket;
    }

    @Secured({"ROLE_CINEMA_ADMIN", "ROLE_USER","ROLE_ADMIN"})
    @Operation(summary = "Delete ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Incorrect request",
                    content = @Content)})
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<String> deleteTicket(@PathVariable Long ticketId, Authentication authentication) {
        LOGGER.debug("Entering TicketController.deleteTicket()");
        ResponseEntity<String> response =
                this.ticketService.deleteTicket(ticketId, authentication.getName())
                        ? new ResponseEntity<>(HttpStatus.OK)
                        : new ResponseEntity<>(NO_PERMISSION_MESSAGE, HttpStatus.FORBIDDEN);

        LOGGER.debug("Exiting TicketController.deleteTicket()");
        return response;
    }
}