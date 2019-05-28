//: com.yulikexuan.security.jwtlab.domain.model.ClientTest.java


package com.yulikexuan.security.jwtlab.domain.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ClientTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void able_To_Map_To_Json() throws JsonProcessingException {

        // Given
        ObjectMapper objectMapper = new ObjectMapper();

        Client client = new Client(1, "yul", "123456");

        // When
        String json = objectMapper.writeValueAsString(client);

        // Then
        System.out.println(json);
    }

}///:~