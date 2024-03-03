package edu.java.clients;

@FunctionalInterface
public interface JsonParser <T> {
    T convertJsonToObject(String data);
}
