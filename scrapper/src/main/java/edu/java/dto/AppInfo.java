package edu.java.dto;

public record AppInfo(int statusCode, String message) {
    public AppInfo(int statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }
}
