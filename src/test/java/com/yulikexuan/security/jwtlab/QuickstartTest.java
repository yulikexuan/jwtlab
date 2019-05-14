//: com.yulikexuan.security.jwtlab.QuickstartTest.java


package com.yulikexuan.security.jwtlab;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Base64;

import static org.assertj.core.api.Assertions.*;


public class QuickstartTest {

    static Key key;
    static String encodedKey;
    static String subject;

    @BeforeAll
    static void init() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        subject = "Yul";
    }

    @BeforeEach
    void setUp() {

    }

    @DisplayName("Test the JWT has three parts - ")
    @Test
    void testJwtStructure() {

        // Given
        String jws = Jwts.builder()
                .setSubject(subject)
                .signWith(key)
                .compact();

        String[] parts = jws.split("\\.");

        // When
        int partsSize = parts.length;

        System.out.printf("The JWS is '%s'%n", jws);
        System.out.printf("The encoded key is '%s'%n", encodedKey);

        // Then
        assertThat(partsSize)
                .as("JWT should have three parts.")
                .isEqualTo(3);
    }

    @DisplayName("Test the actual subject value in JWS - ")
    @Test
    void testSubjectValueInJWS() {

        // Given
        String jws = Jwts.builder()
                .setSubject(subject)
                .signWith(key)
                .compact();

        String[] parts = jws.split("\\.");

        int partsSize = parts.length;

        System.out.printf("The JWS is '%s'%n", jws);
        System.out.printf("The encoded key is '%s'%n", encodedKey);

        // When
        String actualSubject = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jws)
                .getBody()
                .getSubject();

        // Then
        assertThat(partsSize)
                .as("JWT should have three parts.")
                .isEqualTo(3);
        assertThat(actualSubject)
                .as("The subject in JWS should be %s", subject)
                .isEqualTo(subject);
    }

}///:~