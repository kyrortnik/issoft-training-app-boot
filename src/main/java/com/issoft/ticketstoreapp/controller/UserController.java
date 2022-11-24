package com.issoft.ticketstoreapp.controller;

import com.issoft.ticketstoreapp.dto.UserDTO;
import com.issoft.ticketstoreapp.dto.UserHallDTO;
import com.issoft.ticketstoreapp.service.UserService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "api/v1/users", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Secured({"ROLE_CINEMA_ADMIN","ROLE_ADMIN"})
    @Operation(summary = "Get a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable Long userId) {
        LOGGER.debug("Entering UserController.getUser()");

        UserDTO foundUser = this.userService.findUserById(userId);

        LOGGER.debug("Exiting UserController.getUser()");
        return foundUser;
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found users",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Users not found",
                    content = @Content)})
    @GetMapping
    public List<UserDTO> getUsers() {
        LOGGER.debug("Entering UserController.getUsers()");

        List<UserDTO> users = this.userService.findUsers();

        LOGGER.debug("Exiting UserController.getUsers()");
        return users;
    }

    @Secured({"ROLE_ADMIN"})
    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content)})
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
        LOGGER.debug("Entering UserController.createUser()");

        UserDTO createdUser = this.userService.saveUser(userDTO);

        LOGGER.debug("Exiting UserController.createUser()");
        return createdUser;
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid body",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public UserDTO updateUser(@Valid @RequestBody UserDTO userDTO, Principal currentUser) {
        LOGGER.debug("Entering UserController.updateUser()");

        UserDTO updatedUserDTO = this.userService.updateUser(userDTO, currentUser.getName());

        LOGGER.debug("Exiting UserController.updateUser()");
        return updatedUserDTO;
    }

    @Secured("ROLE_ADMIN")
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Incorrect request",
                    content = @Content)})
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        LOGGER.debug("Entering UserController.deleteUser()");

        this.userService.deleteUser(userId);

        LOGGER.debug("Exiting UserController.deleteUser()");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_CINEMA_ADMIN"})
    @Operation(summary = "Get a user who bought the most tickets for provided hall last month")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserHallDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @GetMapping("/lastMonthMostActiveUserForHall")
    public UserHallDTO getLastMonthMostActiveUserForHall(@RequestParam String hallName) {
        LOGGER.debug("Entering UserController.getMostActiveUser()");

        UserHallDTO userHallDTO = this.userService.findMostActiveUserForHall(hallName);

        LOGGER.debug("Exiting UserController.getMostActiveUser()");
        return userHallDTO;
    }
}