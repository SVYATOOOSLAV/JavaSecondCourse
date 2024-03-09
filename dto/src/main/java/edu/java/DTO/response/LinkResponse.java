package edu.java.DTO.response;

import java.net.URI;

public record LinkResponse(int statusCode, URI link) {
}
