package com.issoft.ticketstoreapp.service;

import com.issoft.ticketstoreapp.annotation.Audit;
import com.issoft.ticketstoreapp.dto.CinemaDTO;
import com.issoft.ticketstoreapp.exception.CinemaNotFoundException;
import com.issoft.ticketstoreapp.mapper.CinemaMapper;
import com.issoft.ticketstoreapp.model.Cinema;
import com.issoft.ticketstoreapp.repository.CinemaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CinemaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CinemaService.class);
    private static final String NO_CINEMA_MESSAGE = "No cinema with id [%s] exists";
    private static final String CINEMA_ADMIN_SUFFIX = "ROLE_CINEMA_ADMIN_";
    private static final String NO_PERMISSION_MESSAGE = "Not enough permission for user [%s]";

    private final CinemaRepository cinemaRepository;
    private final CinemaMapper mapper;

    @Autowired
    public CinemaService(CinemaRepository cinemaRepository, CinemaMapper mapper) {
        this.cinemaRepository = cinemaRepository;
        this.mapper = mapper;
    }

    @Audit
    public CinemaDTO findCinemaById(Long cinemaId) {
        LOGGER.debug("Entering CinemaService.findCinemaById()");

        CinemaDTO cinemaDTO = this.mapper.toDto(this.cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new CinemaNotFoundException(String.format(NO_CINEMA_MESSAGE, cinemaId))));

        LOGGER.debug("Exiting CinemaService.findCinemaById");
        return cinemaDTO;
    }

    @Audit
    public List<CinemaDTO> findCinemas() {
        LOGGER.debug("Entering CinemaService.findCinema()");

        List<CinemaDTO> cinemas = this.cinemaRepository.findAll()
                .stream()
                .map(this.mapper::toDto)
                .collect(Collectors.toList());

        LOGGER.debug("Exiting CinemaService.findCinema()");
        return cinemas;
    }

    @Audit
    public CinemaDTO saveCinema(CinemaDTO cinemaDTO) {
        LOGGER.debug("Entering CinemaService.saveCinema()");
        Cinema cinema = this.mapper.toCinema(cinemaDTO);

        CinemaDTO savedOrUpdatedCinema = this.mapper.toDto(this.cinemaRepository.save(cinema));

        LOGGER.debug("Exiting CinemaService.saveCinema()");
        return savedOrUpdatedCinema;
    }

    @Audit
    public CinemaDTO updateCinema(CinemaDTO cinemaDTO, Long cinemaId, Authentication authentication) {
        LOGGER.debug("Entering CinemaService.updateCinema()");
        Cinema cinema = this.mapper.toCinema(cinemaDTO);
        CinemaDTO updatedCinema;

        if (hasAdminRoleForCinema(authentication, cinemaDTO.getName())) {
            cinema.setId(cinemaId);
            updatedCinema = this.mapper.toDto(this.cinemaRepository.save(cinema));
        } else {
            LOGGER.error("Bad credentials logger message");
            throw new BadCredentialsException(String.format(NO_PERMISSION_MESSAGE, authentication.getName()));
        }
        LOGGER.debug("Exiting CinemaService.updateCinema()");
        return updatedCinema;
    }

    @Audit
    public void deleteCinema(Long cinemaId) {
        LOGGER.debug("Entering CinemaService.deleteCinema()");

        this.cinemaRepository.deleteById(cinemaId);

        LOGGER.debug("Exiting CinemaService.deleteCinema()");
    }

    private boolean hasAdminRoleForCinema(Authentication authentication, String cinemaName) {
        return
                authentication.getAuthorities()
                        .stream()
                        .filter(grantedAuthority -> grantedAuthority
                                .getAuthority().equals(CINEMA_ADMIN_SUFFIX + cinemaName.toUpperCase())).count() == 1;
    }
}