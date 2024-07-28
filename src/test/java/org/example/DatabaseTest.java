package org.example;

import org.testng.Assert;
import org.testng.annotations.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseTest {

    private Connection connection;

    @BeforeClass
    public void setup() throws SQLException {
        connection = DatabaseUtil.getConnection();
    }

    @AfterClass
    public void teardown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testInsert() throws SQLException {
        String insertSQL = "INSERT INTO test_table (id, name) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "John Doe");
            int rowsInserted = preparedStatement.executeUpdate();
            Assert.assertEquals(rowsInserted, 1, "One row should be inserted.");
        }
    }

    @Test(dependsOnMethods = "testInsert")
    public void testSelect() throws SQLException {
        String selectSQL = "SELECT * FROM test_table WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            Assert.assertTrue(resultSet.next(), "Result should be found.");
            Assert.assertEquals(resultSet.getString("name"), "John Doe", "Name should be 'John Doe'.");
        }
    }

    @Test(dependsOnMethods = "testSelect")
    public void testUpdate() throws SQLException {
        String updateSQL = "UPDATE test_table SET name = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, "Jane Doe");
            preparedStatement.setInt(2, 1);
            int rowsUpdated = preparedStatement.executeUpdate();
            Assert.assertEquals(rowsUpdated, 1, "One row should be updated.");
        }
    }

    @Test(dependsOnMethods = "testUpdate")
    public void testDelete() throws SQLException {
        String deleteSQL = "DELETE FROM test_table WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, 1);
            int rowsDeleted = preparedStatement.executeUpdate();
            Assert.assertEquals(rowsDeleted, 1, "One row should be deleted.");
        }
    }
}


