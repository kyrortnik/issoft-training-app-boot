package com.issoft.ticketstoreapp.controller;

import com.issoft.ticketstoreapp.dto.*;
import com.issoft.ticketstoreapp.model.Role;
import com.issoft.ticketstoreapp.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerIntegrationTest extends BaseIntegrationTest {
    private static final long USER_ID = 1;
    private static final String

            JSON_PATH_LOGIN = "login",
            JSON_PATH_USER_LOGIN = "user.login",
            JSON_PATH_HALL_NAME = "hall.name",
            JSON_PATH_EMAIL = "email",
            USER_LOGIN = "admin",
            USER_PASSWORD = "12345678",
            USER_EMAIL = "first@mail.com",
            PHONE = "+123456789012",
            FIRST_NAME = "John",
            LAST_NAME = "Smith",
            JSON_PATH_PHONE = "phone",
            JSON_PATH_FIRST_NAME = "firstName",
            JSON_PATH_LAST_NAME = "lastName",
            INVALID_EMAIL = "invalid_email",
            USER_MESSAGE_INVALID_EMAIL_ADDRESS = "invalid email address",
            DEV_MESSAGE_INVALID_EMAIL = "userDTO.email.Email",
            INVALID_PHONE = "+375DSGD221SDF",
            USER_MESSAGE_INVALID_PHONE = "phone should contain 12 digits and start with '+'",
            DEV_MESSAGE_INVALID_PHONE = "userDTO.phone.Pattern",
            USER_MESSAGE_INVALID_LOGIN = "login can not be empty",
            DEV_MESSAGE_INVALID_LOGIN = "userDTO.login.NotBlank",
            SPACE = " ",
            DEV_MESSAGE_USER_NOT_FOUND = "User.id.NotFound",
            USER_MESSAGE_USER_NOT_FOUND = "No user with name [%s] exists",
            UPDATED_EMAIL = "updateEmail@email.com",
            UPDATED_PHONE = "+995590000000",
            ERRORS_PATH = "$['errors'][0]",
            LENGTH = "$.length()",
            SESSION_DATETIME = String.valueOf(LocalDateTime.now().plusDays(10)),
            PURCHASE_DATETIME = String.valueOf(LocalDateTime.now().minusDays(15)),
            CINEMA_NAME = "Aurora",
            HALL_NAME = "red",
            MOVIE_TITLE = "Test title",
            START_WORK_TIME = "10:00",
            CLOSE_WORK_TIME = "10:00",
            LAST_MONTH_MOST_ACTIVE_USER_URL = "/api/v1/users/lastMonthMostActiveUserForHall?hallName=",
            ADMIN = "admin",
            ROLE_ADMIN = "ROLE_ADMIN",
            ROLE_CINEMA_ADMIN_AURORA = "ROLE_CINEMA_ADMIN_AURORA",
            USER = "user",
            ROLE_USER = "ROLE_USER";
    private UserDTO userDTO;
    private Map<String, String> errorMessage;
    private CinemaDTO cinemaDTO;
    private MovieDTO movieDTO;
    private HallDTO hallDTO;
    private RowDTO rowDTO;

    private UserDTO adminUserDTO;
    private Role adminRole;
    private Role cinemaAdminAuroraRole;

    //todo -- refactor
    @BeforeEach
    void setUp() {
        this.adminRole = Role
                .builder()
                .name(ROLE_ADMIN)
                .build();

        this.cinemaAdminAuroraRole = Role
                .builder()
                .name(ROLE_CINEMA_ADMIN_AURORA)
                .build();

        this.userDTO = UserDTO.builder()
                .login(USER_LOGIN)
                .password(USER_PASSWORD)
                .email(USER_EMAIL)
                .phone(PHONE)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();

        this.adminUserDTO = UserDTO.builder()
                .login(ADMIN)
                .password("12345678")
                .email("admin@gmail.com")
                .firstName("First")
                .lastName("Last")
//                .roles(Stream.of(this.adminRole, this.cinemaAdminAuroraRole).collect(Collectors.toSet()))
                .build();
        this.errorMessage = new HashMap<>();

        this.cinemaDTO = CinemaDTO.builder()
                .name(CINEMA_NAME)
                .startWorkTime(START_WORK_TIME)
                .closeWorkTime(CLOSE_WORK_TIME)
                .build();

        this.hallDTO = HallDTO.builder()
                .name(HALL_NAME)
                .has3DSupport(true)
                .hasIMAXSupport(false)
                .cinema(this.cinemaDTO)
                .build();

        this.rowDTO = RowDTO.builder()
                .hall(this.hallDTO)
                .number((short) 1)
                .build();

        this.movieDTO = MovieDTO.builder()
                .title(MOVIE_TITLE)
                .length((short) 135)
                .daysInRotation((short) 45)
                .is3D(false)
                .isIMAX(false)
                .build();
    }

    @AfterEach
    void tearDown() {

        this.userDTO = null;
        this.errorMessage = null;
        this.cinemaDTO = null;
        this.movieDTO = null;
        this.hallDTO = null;
        this.rowDTO = null;
    }


    @Test
    void test_createdUser_isUnauthorized() throws Exception {

        String userAsJson = this.jsonParser.toJson(this.userDTO);
        this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(userAsJson))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = USER, authorities = ROLE_USER)
    @Test
    void test_createUser_isForbidden() throws Exception {

        String userAsJson = this.jsonParser.toJson(this.userDTO);
        this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(userAsJson))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = ADMIN, authorities = ROLE_ADMIN)
    @Test
    void test_createUser_isOk() throws Exception {

        String userAsJson = this.jsonParser.toJson(this.userDTO);
        MvcResult result = this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(userAsJson))

                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_LOGIN).value(USER_LOGIN))
                .andExpect(jsonPath(JSON_PATH_EMAIL).value(USER_EMAIL))
                .andExpect(jsonPath(JSON_PATH_PHONE).value(PHONE))
                .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value(FIRST_NAME))
                .andExpect(jsonPath(JSON_PATH_LAST_NAME).value(LAST_NAME)).andReturn();

        long createdUserId = getGeneratedId(result);

        this.mockMvc.perform(delete(USERS_URL + createdUserId))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = ADMIN, authorities = ROLE_ADMIN)
    @Test
    void test_createUser_invalidEmail_isBadRequest() throws Exception {

        this.userDTO.setEmail(INVALID_EMAIL);
        String userAsJson = this.jsonParser.toJson(this.userDTO);
        this.errorMessage.put(DEV_MESSAGE_INVALID_EMAIL, USER_MESSAGE_INVALID_EMAIL_ADDRESS);

        this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(userAsJson))

                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath(ERRORS_PATH).value(errorMessage));
    }

    @WithMockUser(username = ADMIN, authorities = ROLE_ADMIN)
    @Test
    void test_createUser_invalidPhone_isBadRequest() throws Exception {

        this.userDTO.setPhone(INVALID_PHONE);
        String userAsJson = this.jsonParser.toJson(this.userDTO);
        this.errorMessage.put(DEV_MESSAGE_INVALID_PHONE, USER_MESSAGE_INVALID_PHONE);

        this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(userAsJson))

                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath(ERRORS_PATH).value(errorMessage));
    }

    @WithMockUser(username = ADMIN, authorities = ROLE_ADMIN)
    @Test
    void test_createUser_emptyLogin_isBadRequest() throws Exception {

        this.userDTO.setLogin(SPACE);
        String userAsJson = this.jsonParser.toJson(this.userDTO);
        this.errorMessage.put(DEV_MESSAGE_INVALID_LOGIN, USER_MESSAGE_INVALID_LOGIN);

        this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(userAsJson))

                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath(ERRORS_PATH).value(errorMessage));
    }

    @WithMockUser(username = ADMIN, authorities = ROLE_ADMIN)
    @Test
    void test_getUserById_isOk() throws Exception {

        String userAsJson = this.jsonParser.toJson(this.userDTO);
        MvcResult result = this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(userAsJson))
                .andExpect(status().isOk()).andReturn();

        long createdUserId = getGeneratedId(result);

        this.mockMvc.perform(get(USERS_URL + createdUserId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_LOGIN).value(USER_LOGIN))
                .andExpect(jsonPath(JSON_PATH_EMAIL).value(USER_EMAIL));

        this.mockMvc.perform(delete(USERS_URL + createdUserId))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = ADMIN, authorities = ROLE_ADMIN)
    @Test
    void test_getUserById_isNotFound() throws Exception {

        this.mockMvc.perform(get(USERS_URL + USER_ID)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @WithMockUser(username = ADMIN, authorities = ROLE_ADMIN)
    @Test
    void test_putUser_isOk() throws Exception {

        String userAsJson = this.jsonParser.toJson(this.userDTO);
        MvcResult result = this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(userAsJson))
                .andExpect(status().isOk()).andReturn();

        long createdUserId = getGeneratedId(result);
        User userToUpdate = User
                .builder()
                .login(ADMIN)
                .email(UPDATED_EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .phone(UPDATED_PHONE)
                .build();
        String userToUpdateAsJson = this.jsonParser.toJson(userToUpdate);

        this.mockMvc.perform(put(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(userToUpdateAsJson))
                .andExpect(status().isOk());

        this.mockMvc.perform(delete(USERS_URL + createdUserId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = ADMIN, authorities = ROLE_ADMIN)
    @Test
    void test_putUser_noSuchUserExists_isNotFound() throws Exception {

        this.errorMessage.put(DEV_MESSAGE_USER_NOT_FOUND, String.format(USER_MESSAGE_USER_NOT_FOUND, USER_LOGIN));
        User userToUpdate = User
                .builder()
                .login(USER_LOGIN)
                .email(UPDATED_EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .phone(UPDATED_PHONE)
                .build();

        String userToUpdateAsJson = this.jsonParser.toJson(userToUpdate);

        this.mockMvc.perform(put(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(userToUpdateAsJson))

                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath(ERRORS_PATH).value(errorMessage));
    }

    @WithMockUser(username = ADMIN, authorities = ROLE_ADMIN)
    @Test
    void test_deleteUser_isOk() throws Exception {

        String userAsJson = this.jsonParser.toJson(this.userDTO);
        MvcResult userResult = this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(userAsJson))
                .andExpect(status().isOk()).andReturn();

        long createdUserId = getGeneratedId(userResult);

        this.mockMvc.perform(delete(USERS_URL + createdUserId))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = ADMIN, authorities = ROLE_ADMIN)
    @Test
    void test_getUsers_isOk() throws Exception {

        User secondUser = User.builder()
                .login("second login")
                .password("12345678")
                .email("second@email.com")
                .firstName("Alice")
                .lastName("Soyer")
                .phone("+123456789999")
                .build();
        String firstUserAsJson = jsonParser.toJson(userDTO);
        this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(firstUserAsJson))
                .andExpect(status().isOk());
        String secondUserAsJson = jsonParser.toJson(secondUser);
        this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(secondUserAsJson))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get(USERS_URL)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath(LENGTH).value(2)).andReturn();

        List<Long> generatedIds = getGeneratedIds(result);


        mockMvc.perform(delete(USERS_URL + generatedIds.get(0)))
                .andExpect(status().isOk());

        mockMvc.perform(delete(USERS_URL + generatedIds.get(1)))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = ADMIN, authorities = {ROLE_ADMIN, ROLE_CINEMA_ADMIN_AURORA})
    @Test
    void test_findLastMonthMostActiveUserForHall_isOk() throws Exception {

        UserDTO mostActiveUser = UserDTO.builder()
                .login("most active login")
                .password("12345678")
                .email("active@email.com")
                .firstName("Harry")
                .lastName("Kane")
                .phone("+123456789999")
                .build();

        UserDTO notSoActiveUser = UserDTO.builder()
                .login("not so active login")
                .password("1234567")
                .email("so@email.com")
                .firstName("Kilian")
                .lastName("Mbappe")
                .phone("+123456789998")
                .build();

        String cinemaAsJson = this.jsonParser.toJson(cinemaDTO);
        MvcResult cinemaResult = this.mockMvc.perform(post(CINEMAS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(cinemaAsJson))
                .andExpect(status().isOk()).andReturn();
        long createdCinemaId = getGeneratedId(cinemaResult);
        cinemaDTO.setId(createdCinemaId);

        String movieAsJson = this.jsonParser.toJson(movieDTO);
        MvcResult movieResult = this.mockMvc.perform(post(MOVIES_URL)
                        .contentType(APPLICATION_JSON)
                        .content(movieAsJson))
                .andExpect(status().isOk()).andReturn();
        long createdMovieId = getGeneratedId(movieResult);
        movieDTO.setId(createdMovieId);

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

        SeatDTO seat1 = SeatDTO.builder()
                .row(rowDTO)
                .number((short) 1)
                .build();

        SeatDTO seat2 = SeatDTO.builder()
                .row(rowDTO)
                .number((short) 2)
                .build();

        SeatDTO seat3 = SeatDTO.builder()
                .row(rowDTO)
                .number((short) 3)
                .build();

        String seat1AsJson = this.jsonParser.toJson(seat1);
        MvcResult seat1Result = this.mockMvc.perform(post(SEATS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(seat1AsJson))
                .andExpect(status().isOk()).andReturn();
        long createdSeat1Id = getGeneratedId(seat1Result);
        seat1.setId(createdSeat1Id);

        String seat2AsJson = this.jsonParser.toJson(seat2);
        MvcResult seat2Result = this.mockMvc.perform(post(SEATS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(seat2AsJson))
                .andExpect(status().isOk()).andReturn();
        long createdSeat2Id = getGeneratedId(seat2Result);
        seat2.setId(createdSeat2Id);

        String seat3AsJson = this.jsonParser.toJson(seat3);
        MvcResult seat3Result = this.mockMvc.perform(post(SEATS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(seat3AsJson))
                .andExpect(status().isOk()).andReturn();
        long createdSeat3Id = getGeneratedId(seat3Result);
        seat3.setId(createdSeat3Id);

        String adminUserAsJson = this.jsonParser.toJson(adminUserDTO);
        MvcResult adminUserResult = this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(adminUserAsJson))
                .andExpect(status().isOk()).andReturn();
        long createdUserAdminId = getGeneratedId(adminUserResult);
        adminUserDTO.setId(createdUserAdminId);

        String mostActiveUserJson = this.jsonParser.toJson(mostActiveUser);
        MvcResult mostActiveUserResult = this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(mostActiveUserJson))
                .andExpect(status().isOk()).andReturn();
        long mostActiveUserId = getGeneratedId(mostActiveUserResult);
        mostActiveUser.setId(mostActiveUserId);

        String notSoActiveUserJson = this.jsonParser.toJson(notSoActiveUser);
        MvcResult notSoActiveUserResult = this.mockMvc.perform(post(USERS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(notSoActiveUserJson))
                .andExpect(status().isOk()).andReturn();
        long notSoActiveUserId = getGeneratedId(notSoActiveUserResult);
        notSoActiveUser.setId(notSoActiveUserId);


        TicketDTO firstTicket = TicketDTO.builder()
                .code("001")
                .purchaseDateTime(PURCHASE_DATETIME)
                .hall(hallDTO)
                .movie(movieDTO)
                .seat(seat1)
                .buyer(mostActiveUser)
                .sessionDateTime(SESSION_DATETIME)
                .build();

        TicketDTO secondTicket = TicketDTO.builder()
                .code("002")
                .purchaseDateTime(PURCHASE_DATETIME)
                .hall(hallDTO)
                .movie(movieDTO)
                .seat(seat2)
                .buyer(mostActiveUser)
                .sessionDateTime(SESSION_DATETIME)
                .build();

        TicketDTO thirdTicket = TicketDTO.builder()
                .code("003")
                .purchaseDateTime(PURCHASE_DATETIME)
                .hall(hallDTO)
                .movie(movieDTO)
                .seat(seat3)
                .buyer(notSoActiveUser)
                .sessionDateTime(SESSION_DATETIME)
                .build();

        String firstTicketJson = this.jsonParser.toJson(firstTicket);
        MvcResult firstTicketResult = this.mockMvc.perform(post(TICKETS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(firstTicketJson))
                .andExpect(status().isOk()).andReturn();
        long firstTicketId = getGeneratedId(firstTicketResult);

        String secondTicketJson = this.jsonParser.toJson(secondTicket);
        MvcResult secondTicketResult = this.mockMvc.perform(post(TICKETS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(secondTicketJson))
                .andExpect(status().isOk()).andReturn();
        long secondTicketId = getGeneratedId(secondTicketResult);

        String thirdTicketJson = this.jsonParser.toJson(thirdTicket);
        MvcResult thirdTicketResult = this.mockMvc.perform(post(TICKETS_URL)
                        .contentType(APPLICATION_JSON)
                        .content(thirdTicketJson))
                .andExpect(status().isOk()).andReturn();
        long thirdTicketId = getGeneratedId(thirdTicketResult);

        this.mockMvc.perform(get(LAST_MONTH_MOST_ACTIVE_USER_URL + HALL_NAME)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_USER_LOGIN).value(mostActiveUser.getLogin()))
                .andExpect(jsonPath(JSON_PATH_HALL_NAME).value(HALL_NAME));

        this.mockMvc.perform(delete(TICKETS_URL + firstTicketId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(TICKETS_URL + secondTicketId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(TICKETS_URL + thirdTicketId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(MOVIES_URL + createdMovieId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(USERS_URL + mostActiveUserId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(USERS_URL + notSoActiveUserId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(USERS_URL + createdUserAdminId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(SEATS_URL + createdSeat1Id))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(SEATS_URL + createdSeat2Id))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(SEATS_URL + createdSeat3Id))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(ROWS_URL + createdRowId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(HALLS_URL + createdHallId))
                .andExpect(status().isOk());
        this.mockMvc.perform(delete(CINEMAS_URL + createdCinemaId))
                .andExpect(status().isOk());
    }
}