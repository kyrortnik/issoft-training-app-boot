package com.issoft.ticketstoreapp.service;

import com.issoft.ticketstoreapp.annotation.Audit;
import com.issoft.ticketstoreapp.dto.HallDTO;
import com.issoft.ticketstoreapp.exception.HallNotFoundException;
import com.issoft.ticketstoreapp.mapper.HallMapper;
import com.issoft.ticketstoreapp.model.Hall;
import com.issoft.ticketstoreapp.repository.HallRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HallService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HallService.class);
    private static final String NO_HALL_MESSAGE = "no hall with name [%s] exists";
    private static final String NO_HALL_WITH_ID_MESSAGE = "no hall with id [%d] exists";

    private final HallRepository hallRepository;
    private final HallMapper mapper;

    @Audit
    public HallDTO saveHall(HallDTO hallDTO) {
        LOGGER.debug("Entering HallService.saveHall()");
        Hall hall = this.mapper.toHall(hallDTO);

        HallDTO savedOrUpdatedCinema = this.mapper.toDto(this.hallRepository.save(hall));

        LOGGER.debug("Exiting HallService.saveHall()");
        return savedOrUpdatedCinema;
    }

    @Audit
    public HallDTO findHallById(Long hallId) {
        LOGGER.debug("Entering HallService.findHallById()");

        HallDTO hallDTO = this.mapper.toDto(this.hallRepository.findById(hallId)
                .orElseThrow(() -> new HallNotFoundException(String.format(NO_HALL_WITH_ID_MESSAGE, hallId))));

        LOGGER.debug("Exiting HallService.findHallById()");
        return hallDTO;

    }

    @Audit
    public List<HallDTO> findHalls() {
        LOGGER.debug("Entering HallService.findHalls()");

        List<HallDTO> movies = this.hallRepository
                .findAll()
                .stream()
                .map(this.mapper::toDto)
                .collect(Collectors.toList());

        LOGGER.debug("Exiting findHalls.findHalls()");
        return movies;
    }

    @Audit
    public HallDTO findHallByName(String hallName) {
        LOGGER.debug("Entering HallService.findHallByName()");
        HallDTO foundHallDTO;

        Optional<Hall> foundHall = this.hallRepository.findByName(hallName);
        if (foundHall.isPresent()) {
            foundHallDTO = this.mapper.toDto(foundHall.get());
        } else throw new HallNotFoundException(String.format(NO_HALL_MESSAGE, hallName));

        LOGGER.debug("Exiting HallService.findHallByName()");
        return foundHallDTO;
    }

    @Audit
    public void deleteHall(Long hallId) {
        LOGGER.debug("Entering HallService.deleteHall()");

        this.hallRepository.deleteById(hallId);

        LOGGER.debug("Exiting HallService.deleteHall()");
    }

    @Audit
    public List<HallDTO> findHallsForCinema(Long cinemaId) {
        LOGGER.debug("Entering HallService.findHallsForCinema()");
        List<HallDTO> foundHallDTOs;

        foundHallDTOs = this.hallRepository.findHallsByCinemaId(cinemaId)
                .stream()
                .map(this.mapper::toDto)
                .collect(Collectors.toList());

        LOGGER.debug("Exiting HallService.findHallsForCinema()");
        return foundHallDTOs;
    }
}