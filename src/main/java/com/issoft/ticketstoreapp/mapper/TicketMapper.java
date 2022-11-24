package com.issoft.ticketstoreapp.mapper;

import com.issoft.ticketstoreapp.dto.TicketDTO;
import com.issoft.ticketstoreapp.model.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    TicketDTO toDto(Ticket ticket);

    Ticket toTicket(TicketDTO ticketDTO);
}