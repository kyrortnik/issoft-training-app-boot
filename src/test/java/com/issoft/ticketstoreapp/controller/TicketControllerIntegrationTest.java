package com.issoft.ticketstoreapp.controller;

import com.issoft.ticketstoreapp.dto.*;
import com.issoft.ticketstoreapp.model.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class TicketControllerIntegrationTest extends BaseIntegrationTest {
    private static final String CODE = "001",
            JSON_PATH_CODE = "code",
            SESSION_DATETIME = String.valueOf(LocalDateTime.now().plusDays(10)),
            PURCHASE_DATETIME = String.valueOf(LocalDateTime.now().minusDays(15)),
            CINEMA_NAME = "Aurora",
            HALL_NAME = "red",
            MOVIE_TITLE = "Test title",
            USER_LOGIN = "user_login",
            EMAIL = "test@gmail.com",
            FIRST_NAME = "John",
            LAST_NAME = "Smith",
            PHONE_NUMBER = "+123456789012",
            START_WORK_TIME = "10:00",
            CLOSE_WORK_TIME = "10:00",
            USER_PASSWORD = "12345678",
            ADMIN = "admin",
            ROLE_ADMIN = "ROLE_ADMIN",
            ROLE_CINEMA_ADMIN_AURORA = "ROLE_CINEMA_ADMIN_AURORA",
            CINEMA_ADMIN_AURORA = "cinemaAdminAurora";
    private TicketDTO ticketDTO;
    private CinemaDTO cinemaDTO;
    private MovieDTO movieDTO;
    private HallDTO hallDTO;
    private RowDTO rowDTO;
    private SeatDTO seatDTO;
    private UserDTO userDTO;
    private UserDTO adminUserDTO;
    private Role adminRole;
    private Role cinemaAdminAuroraRole;

    @BeforeEach
    void setUp() {

        this.cinemaDTO = CinemaDTO.builder()
                .name(CINEMA_NAME)
                .startWorkTime(START_WORK_TIME)
                .closeWorkTime(CLOSE_WORK_TIME)
                .build();

        this.hallDTO = HallDTO.builder()
                .name(HALL_NAME)
                .has3DSupport(true)
                .hasIMAXSupport(false)
                .build();

        this.rowDTO = RowDTO.builder()
                .number((short) 1)
                .build();

        this.seatDTO = SeatDTO.builder()
                .number((short) 1)
                .build();

        this.movieDTO = MovieDTO.builder()
                .title(MOVIE_TITLE)
                .length((short) 135)
                .daysInRotation((short) 45)
                .is3D(false)
                .isIMAX(false)
                .build();

        this.userDTO = UserDTO.builder()
                .login(USER_LOGIN)
                .password(USER_PASSWORD)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .phone(PHONE_NUMBER)

                .build();

        this.adminRole = Role
                .builder()
                .name(ROLE_ADMIN)
                .build();

        this.cinemaAdminAuroraRole = Role
                .builder()
                .name(ROLE_CINEMA_ADMIN_AURORA)
                .build();

        this.adminUserDTO = UserDTO.builder()
                .login("admin")
                .password("12345678")
                .email("admin@gmail.com")
                .firstName("First")
                .lastName("Last")
                .roles(Stream.of(this.adminRole, this.cinemaAdminAuroraRole).collect(Collectors.toSet()))
                .build();

        this.ticketDTO = TicketDTO.builder()
                .code(CODE)
                .sessionDateTime(SESSION_DATETIME)
                .purchaseDateTime(LocalDateTime.now().toString())
                .build();
    }

    @AfterEach
    void tearDown() {

        this.cinemaDTO = null;
        this.movieDTO = null;
        this.hallDTO = null;
        this.rowDTO = null;
        this.seatDTO = null;
        this.userDTO = null;
        this.adminUserDTO = null;
        this.ticketDTO = null;
        this.adminRole = null;
        this.cinemaAdminAuroraRole = null;
    }

    @Test
    void test_createTicket_isUnauthorized() throws Exception {
        this.ticketDTO = TicketDTO.builder()
                .code(CODE)
                .sessionDateTime(SESSION_DATETIME)
                .purchaseDateTime(PURCHASE_DATETIME)
                .build();

        String ticketAsJson = this.jsonParser.toJson(ticketDTO);
        this.mockMvc.perform(post(TICKETS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(ticketAsJson))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = CINEMA_ADMIN_AURORA, authorities = ROLE_CINEMA_ADMIN_AURORA)
    @Test
    void test_createTicket_isForbidden() throws Exception {
        this.ticketDTO = TicketDTO.builder()
                .code(CODE)
                .sessionDateTime(SESSION_DATETIME)
                .purchaseDateTime(PURCHASE_DATETIME)
                .build();

        String ticketAsJson = this.jsonParser.toJson(ticketDTO);
        this.mockMvc.perform(post(TICKETS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(ticketAsJson))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = ADMIN, authorities = {ROLE_ADMIN, ROLE_CINEMA_ADMIN_AURORA})
    @Test
    void test_createTicketAsCinemaAdmin_isOk() throws Exception {

        String movieAsJson = this.jsonParser.toJson(movieDTO);
        MvcResult movieResult = this.mockMvc.perform(post(MOVIES_URL)
                        .contentType(APPLICATION_JSON)
                        .content(movieAsJson))
                .andExpect(status().isOk()).andReturn();
        long createdMovieId = getGeneratedId(movieResult);
        movieDTO.setId(createdMovieId);

        String cinemaAsJson = this.jsonParser.toJson(cinemaDTO);
        MvcResult cinemaResult = this.mockMvc.perform(post(CINEMAS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(cinemaAsJson))
                .andExpect(status().isOk()).andReturn();
        long createdCinemaId = getGeneratedId(cinemaResult);
        cinemaDTO.setId(createdCinemaId);

        hallDTO.setCinema(cinemaDTO);
        String hallAsJson = this.jsonParser.toJson(hallDTO);
        MvcResult hallResult = this.mockMvc.perform(post(HALLS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(hallAsJson))
                .andExpect(status().isOk()).andReturn();
        long createdHallId = getGeneratedId(hallResult);
        hallDTO.setId(createdHallId);

        rowDTO.setHall(hallDTO);
        String rowAsJson = this.jsonParser.toJson(rowDTO);
        MvcResult rowResult = this.mockMvc.perform(post(ROWS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(rowAsJson))
                .andExpect(status().isOk()).andReturn();
        long createdRowId = getGeneratedId(rowResult);
        rowDTO.setId(createdRowId);

        seatDTO.setRow(rowDTO);
        String seatAsJson = this.jsonParser.toJson(seatDTO);
        MvcResult seatResult = this.mockMvc.perform(post(SEATS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(seatAsJson))
                .andExpect(status().isOk()).andReturn();
        long createdSeatId = getGeneratedId(seatResult);
        seatDTO.setId(createdSeatId);

        String userAsJson = this.jsonParser.toJson(userDTO);
        MvcResult userResult = this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(userAsJson))
                .andExpect(status().isOk()).andReturn();
        long createdUserId = getGeneratedId(userResult);
        userDTO.setId(createdUserId);

        String adminUserAsJson = this.jsonParser.toJson(adminUserDTO);
        MvcResult adminUserResult = this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(adminUserAsJson))
                .andExpect(status().isOk()).andReturn();
        long createdUserAdminId = getGeneratedId(adminUserResult);
        adminUserDTO.setId(createdUserAdminId);

        ticketDTO.setBuyer(userDTO);
        ticketDTO.setMovie(movieDTO);
        ticketDTO.setHall(hallDTO);
        ticketDTO.setSeat(seatDTO);

        String ticketAsJson = this.jsonParser.toJson(ticketDTO);
        MvcResult ticketResult = this.mockMvc.perform(post(TICKETS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(ticketAsJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_CODE).value(CODE)).andReturn();
        long createdTicketId = getGeneratedId(ticketResult);

        this.mockMvc.perform(delete(TICKETS_URL + createdTicketId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(USERS_URL + createdUserId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(USERS_URL + createdUserAdminId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(SEATS_URL + createdSeatId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(ROWS_URL + createdRowId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(HALLS_URL + createdHallId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(MOVIES_URL + createdMovieId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(CINEMAS_URL + createdCinemaId))
                .andExpect(status().isOk());
    }
}