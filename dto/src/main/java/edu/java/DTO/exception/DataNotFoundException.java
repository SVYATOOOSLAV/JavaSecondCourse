package edu.java.DTO.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DataNotFoundException extends RuntimeException{
    private final HttpStatus httpStatus;

    public DataNotFoundException(String message){
        super(message);
        httpStatus = HttpStatus.NOT_FOUND;
    }
}
