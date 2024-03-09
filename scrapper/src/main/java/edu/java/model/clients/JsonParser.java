package edu.java.model.clients;

@FunctionalInterface
public interface JsonParser <T> {
    T convertJsonToObject(String data);
}
