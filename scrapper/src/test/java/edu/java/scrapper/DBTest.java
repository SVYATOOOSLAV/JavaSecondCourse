package edu.java.scrapper;

import org.junit.jupiter.api.Test;

public class DBTest extends IntegrationTest{
    @Test
    void simpleTest(){
        String query = "insert into users values(1, 'testUserName')";

    }
}
