package com.issoft.ticketstoreapp.handler;

import com.issoft.ticketstoreapp.exception.CinemaNotFoundException;
import com.issoft.ticketstoreapp.exception.HallNotFoundException;
import com.issoft.ticketstoreapp.exception.MovieNotFoundException;
import com.issoft.ticketstoreapp.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;


@RestControllerAdvice
public class ApplicationExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationExceptionHandler.class);
    private static final String
            DOT = ".",
            ERRORS = "errors",
            DEV_MESSAGE_CINEMA_ID_NOT_FOUND = "Cinema.id.NotFound",
            DEV_MESSAGE_HALL_NAME_NOT_FOUND = "Hall.name.NotFound",
            DEV_MESSAGE_MOVIE_NAME_NOT_FOUND = "Movie.id.NotFound",
            DEV_MESSAGE_USER_ID_NOT_FOUND = "User.id.NotFound",
            DEV_MESSAGE_AUTHENTICATION_FAILURE = "FailedAuthentication",
            DEV_MESSAGE_DATA_ACCESS_EXCEPTION = "DataAccessException";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Map<String, List<Map.Entry<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, List<Map.Entry<String, String>>> responseParams = new HashMap<>();
        responseParams.put(ERRORS, getValidationErrorMessages(ex));

        LOGGER.error(ex.getMessage());
        return responseParams;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public Map<String, List<Map.Entry<String, String>>> handleCinemaNotFoundException(CinemaNotFoundException ex) {

        Map<String, List<Map.Entry<String, String>>> responseParams = new HashMap<>();
        responseParams.put(ERRORS, getExceptionMessage(ex, DEV_MESSAGE_CINEMA_ID_NOT_FOUND));

        LOGGER.error(ex.getMessage());
        return responseParams;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public Map<String, List<Map.Entry<String, String>>> handleHallNotFoundException(HallNotFoundException ex) {

        Map<String, List<Map.Entry<String, String>>> responseParams = new HashMap<>();
        responseParams.put(ERRORS, getExceptionMessage(ex, DEV_MESSAGE_HALL_NAME_NOT_FOUND));

        LOGGER.error(ex.getMessage());
        return responseParams;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public Map<String, List<Map.Entry<String, String>>> handleMovieNotFoundException(MovieNotFoundException ex) {

        Map<String, List<Map.Entry<String, String>>> responseParams = new HashMap<>();
        responseParams.put(ERRORS, getExceptionMessage(ex, DEV_MESSAGE_MOVIE_NAME_NOT_FOUND));

        LOGGER.error(ex.getMessage());
        return responseParams;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public Map<String, List<Map.Entry<String, String>>> handleUserNotFoundException(UserNotFoundException ex) {

        Map<String, List<Map.Entry<String, String>>> responseParams = new HashMap<>();
        responseParams.put(ERRORS, getExceptionMessage(ex, DEV_MESSAGE_USER_ID_NOT_FOUND));

        LOGGER.error(ex.getMessage());
        return responseParams;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public Map<String, List<Map.Entry<String, String>>> handleAuthenticationException(AuthenticationException ex) {

        Map<String, List<Map.Entry<String, String>>> responseParams = new HashMap<>();
        responseParams.put(ERRORS, getExceptionMessage(ex, DEV_MESSAGE_AUTHENTICATION_FAILURE));

        LOGGER.error(ex.getMessage());
        return responseParams;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public Map<String, List<Map.Entry<String, String>>> handleEmptyResultDataAccessException(DataAccessException ex) {

        Map<String, List<Map.Entry<String, String>>> responseParams = new HashMap<>();
        responseParams.put(ERRORS, getExceptionMessage(ex, DEV_MESSAGE_DATA_ACCESS_EXCEPTION));

        LOGGER.error(ex.getMessage());
        return responseParams;
    }

    private List<Map.Entry<String, String>> getValidationErrorMessages(MethodArgumentNotValidException ex) {

        List<Map.Entry<String, String>> errorMessages = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String userErrorMessage = error.getDefaultMessage();
            String devErrorMessage = getDevErrorMessage((FieldError) error);
            errorMessages.add(new AbstractMap.SimpleEntry<>(devErrorMessage, userErrorMessage));
        }
        return errorMessages;
    }

    private String getDevErrorMessage(FieldError error) {
        String objetName = error.getObjectName();
        String fieldName = error.getField();
        String errorCode = error.getCode();
        return objetName + DOT + fieldName + DOT + errorCode;
    }

    private List<Map.Entry<String, String>> getExceptionMessage(Exception ex, String devMessage) {
        List<Map.Entry<String, String>> errorMessage = new ArrayList<>();
        String exErrorMessage = ex.getMessage();
        errorMessage.add(new AbstractMap.SimpleEntry<>(devMessage, exErrorMessage));
        return errorMessage;
    }
}