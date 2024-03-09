package edu.java.DTO.request;

public record LinkUpdate(long id, String url, String description, long[] tgChatIds) {
}
