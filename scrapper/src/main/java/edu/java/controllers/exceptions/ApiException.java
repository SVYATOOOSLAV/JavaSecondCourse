package edu.java.controllers.exceptions;

public class ApiException extends RuntimeException {
    public ApiException(String message){
        super(message);
    }
}
