package edu.java.DTO.response;

public record ListLinksResponse(LinkResponse[] links, int sizeArr) {
    public ListLinksResponse(LinkResponse[] links) {
        this(links, links.length);
    }
}
