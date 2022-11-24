package com.issoft.ticketstoreapp.service;

import com.issoft.ticketstoreapp.annotation.Audit;
import com.issoft.ticketstoreapp.dto.TicketDTO;
import com.issoft.ticketstoreapp.exception.MovieNotFoundException;
import com.issoft.ticketstoreapp.mapper.TicketMapper;
import com.issoft.ticketstoreapp.model.Ticket;
import com.issoft.ticketstoreapp.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketService.class);
    public static final String NO_TICKET_MESSAGE = "No ticket with id [%s] exists";
    private final TicketRepository ticketRepository;
    private final TicketMapper mapper;

    @Autowired
    public TicketService(TicketRepository ticketRepository, TicketMapper mapper) {
        this.ticketRepository = ticketRepository;
        this.mapper = mapper;
    }


    @Audit
    public TicketDTO findTicketById(Long ticketId) {
        LOGGER.debug("Entering TicketService.findTicketById()");

        TicketDTO ticketDTO = this.mapper.toDto(this.ticketRepository.findById(ticketId)
                .orElseThrow(() -> new MovieNotFoundException(String.format(NO_TICKET_MESSAGE, ticketId))));

        LOGGER.debug("Exiting TicketService.findTicketById()");
        return ticketDTO;
    }

    @Audit
    public List<TicketDTO> findTickets() {
        LOGGER.debug("Entering TicketService.findTickets()");

        List<TicketDTO> tickets = this.ticketRepository
                .findAll()
                .stream()
                .map(this.mapper::toDto)
                .collect(Collectors.toList());

        LOGGER.debug("Exiting TicketService.findTickets()");
        return tickets;
    }

    @Audit
    public TicketDTO saveTicket(TicketDTO ticketDTO) {
        LOGGER.debug("Entering TicketService.saveTicket()");
        Ticket ticket = this.mapper.toTicket(ticketDTO);

        TicketDTO savedOrUpdatedTicket = this.mapper.toDto(this.ticketRepository.save(ticket));

        LOGGER.debug("Exiting TicketService.saveTicket()");
        return savedOrUpdatedTicket;
    }

    @Transactional
    @Audit
    public boolean deleteTicket(Long ticketId, String username) {
        LOGGER.debug("Entering TicketService.deleteTicket()");
        int rowsDeleted;

        rowsDeleted = this.ticketRepository.deleteByIdWithUsername(ticketId, username);

        LOGGER.debug("Exiting TicketService.deleteTicket()");
        return rowsDeleted > 0;
    }
}