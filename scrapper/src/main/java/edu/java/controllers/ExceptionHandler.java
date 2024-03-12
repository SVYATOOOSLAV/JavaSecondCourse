package edu.java.controllers;

import edu.java.DTO.exception.DataNotFoundException;
import edu.java.DTO.exception.InvalidRequestException;
import edu.java.DTO.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiErrorResponse> catchInvalidRequestException(InvalidRequestException e) {
        log.error(e.getMessage(), e);
        String[] httpStatus = HttpStatus.BAD_REQUEST.toString().split(" ");
        String description = httpStatus[1];
        String code = httpStatus[0];
        return new ResponseEntity<>(
            new ApiErrorResponse(
                description,
                code,
                InvalidRequestException.class.getName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toArray(String[]::new)
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> catchDataNotFoundException(DataNotFoundException e) {
        log.error(e.getMessage(), e);
        String[] httpStatus = HttpStatus.NOT_FOUND.toString().split(" ");
        String description = httpStatus[1];
        String code = httpStatus[0];
        return new ResponseEntity<>(
            new ApiErrorResponse(
                description,
                code,
                InvalidRequestException.class.getName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toArray(String[]::new)
            ),
            HttpStatus.NOT_FOUND
        );
    }
}
