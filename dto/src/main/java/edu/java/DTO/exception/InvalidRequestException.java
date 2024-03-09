package edu.java.DTO.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidRequestException extends RuntimeException {
    private final HttpStatus httpStatus;
    public InvalidRequestException(String message){
        super(message);
        httpStatus = HttpStatus.BAD_REQUEST;
    }
}
