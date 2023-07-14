package com.springbootdemo.customer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();

        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getInt("id")).thenReturn(1);
        Mockito.when(resultSet.getInt("age")).thenReturn(20);
        Mockito.when(resultSet.getString("name")).thenReturn("name");
        Mockito.when(resultSet.getString("email")).thenReturn("email@email,com");

        // When
        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        // Then
        Customer expected = new Customer(1, "name", "email@email,com", 20);

        assertThat(actual).isEqualTo(expected);
    }
}