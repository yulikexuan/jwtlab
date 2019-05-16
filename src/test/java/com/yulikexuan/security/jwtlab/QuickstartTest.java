//: com.yulikexuan.security.jwtlab.QuickstartTest.java


package com.yulikexuan.security.jwtlab;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.security.Key;
import java.util.Base64;

import static org.assertj.core.api.Assertions.*;


public class QuickstartTest {

    /*
     * This key is being used to validate the signature of the JWT.
     */
    static Key key;
    static String encodedKey;
    static String subject;
    static Base64.Encoder encoder;

    @BeforeAll
    static void init() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        encoder = Base64.getEncoder();
        encodedKey = encoder.encodeToString(key.getEncoded());
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
        String actualSubject = null;
        try {
            actualSubject = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jws)
                    .getBody()
                    .getSubject();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        // Then
        assertThat(partsSize)
                .as("JWT should have three parts.")
                .isEqualTo(3);
        assertThat(actualSubject)
                .as("The subject in JWS should be %s", subject)
                .isEqualTo(subject);
    }

    @DisplayName("Test the invalid subject value in JWS - ")
    @Test
    void testInvalidSubjectValueInJWS() {

        // Given
        String jws = Jwts.builder()
                .setSubject(subject)
                .signWith(key)
                .compact();
        String[] parts = jws.split("\\.");

        String otherSubjectValue = "Joe";
        String otherJws = Jwts.builder()
                .setSubject(otherSubjectValue)
                .signWith(key)
                .compact();
        String[] otherParts = otherJws.split("\\.");

        System.out.printf("The original JWS is '%s'%n", jws);
        System.out.printf("The encoded key is '%s'%n", encodedKey);

        final String invalidJws = jws.replace(parts[1], otherParts[1]);

        // When
        Executable parser = () -> Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(invalidJws);

        ThrowableAssert.ThrowingCallable callable = () -> Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(invalidJws);

        Throwable thrown = catchThrowable(callable);

        // Then
        Assertions.assertThrows(SignatureException.class, parser);
        assertThat(thrown).isInstanceOf(SignatureException.class);
    }

}///:~