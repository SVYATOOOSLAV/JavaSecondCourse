package edu.java.scrapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

public class DBTest extends IntegrationTest{
    @Test
    @DisplayName("Insert and Select row")
    void simpleTest() throws SQLException {
        int referentID = 1;
        String referentName = "testUser";
        try(Connection connection = DriverManager.getConnection(
            POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword())){

            String insertQuery = "insert into users values(?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "testUser");
            preparedStatement.executeUpdate();

            String selectQuery = "select * from users;";
            PreparedStatement preparedStatement2 = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement2.executeQuery();
            assertAll(
                () -> assertTrue(resultSet.next()),
                () -> assertEquals(resultSet.getInt("id"), referentID),
                () -> assertEquals(resultSet.getString("first_name"), referentName)
            );
        }
    }
}
