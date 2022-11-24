package com.issoft.ticketstoreapp.service;

import com.issoft.ticketstoreapp.annotation.Audit;
import com.issoft.ticketstoreapp.dto.SeatDTO;
import com.issoft.ticketstoreapp.mapper.SeatMapper;
import com.issoft.ticketstoreapp.model.Seat;
import com.issoft.ticketstoreapp.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeatService.class);

    private final SeatRepository seatRepository;
    private final SeatMapper mapper;

    @Autowired
    public SeatService(SeatRepository seatRepository, SeatMapper mapper) {
        this.seatRepository = seatRepository;
        this.mapper = mapper;
    }

    @Audit
    public SeatDTO saveSeat(SeatDTO seatDTO) {
        LOGGER.debug("Entering SeatService.saveSeat()");
        Seat seat = this.mapper.toSeat(seatDTO);

        SeatDTO savedOrUpdatedSeat = this.mapper.toDto(this.seatRepository.save(seat));

        LOGGER.debug("Exiting SeatService.saveSeat()");
        return savedOrUpdatedSeat;
    }

    @Audit
    public void deleteSeat(Long seatId) {
        LOGGER.debug("Entering SeatService.deleteSeat()");

        this.seatRepository.deleteById(seatId);

        LOGGER.debug("Exiting SeatService.deleteSeat()");
    }
}