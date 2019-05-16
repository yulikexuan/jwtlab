//: com.yulikexuan.security.jwtlab.CreatingJwsTest.java


package com.yulikexuan.security.jwtlab;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.yulikexuan.security.jwtlab.utils.SigningUtil.getHS256SecretKey;
import static org.assertj.core.api.Assertions.assertThat;
import static com.yulikexuan.security.jwtlab.utils.DateUtil.*;


/*
 * Create a JWS as follows:
 *   1. Use the Jwts.builder() method to create a JwtBuilder instance.
 *   2. Call JwtBuilder methods to add header parameters and claims as
 *      desired.
 *   3. Specify the SecretKey or asymmetric PrivateKey you want to use to
 *      sign the JWT.
 *   4. Finally, call the compact() method to compact and sign, producing
 *      the final jws.
 */
public class CreatingJwsTest {

    /*
     * This key is being used to validate the signature of the JWT.
     */
    private Key key;
    private String keyId;
    private String subject;

    @BeforeEach
    void setUp() {
        key = getHS256SecretKey();
        keyId = UUID.randomUUID().toString();
        subject = "Yul";
    }

    /*
     * A JWT Header provides metadata about the contents, format and
     * cryptographic operations relevant to the JWT's Claims.
     *
     * You do not need to set the alg or zip header parameters as JJWT will set
     * them automatically depending on the signature algorithm or compression
     * algorithm used.
     */
    @DisplayName("Test header parameter setting - ")
    @Test
    void testHeaderSettingsWithSetHeaderParamMethod() {

        // Given
        String jws = Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, keyId)
                .setSubject(subject)
                .signWith(key)
                .compact();

        /*
         * The JWA (JSON Web Algorithms (JWA)) algorithm name constant.
         */
        String expectedAlgorithm = SignatureAlgorithm.HS256.getValue();

        // When
        Jws<Claims> parsedJws = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jws);

        JwsHeader header = parsedJws.getHeader();

        String actualAlgorithm = header.getAlgorithm();
        String actualKeyId = header.getKeyId();

        // Then
        assertThat(actualAlgorithm)
                .as("The algorithm used in the jws should be '%s'",
                        expectedAlgorithm)
                .isEqualTo(expectedAlgorithm);
        assertThat(actualKeyId)
                .as("The actual key id should be '%s'", keyId)
                .isEqualTo(keyId);
    }

    @DisplayName("Test header instance setting - ")
    @Test
    void testHeaderSettingsWithSetHeaderMethod() {

        // Given
        Header header = Jwts.header();
        header.put(JwsHeader.KEY_ID, keyId);

        /*
         * When using signWith JJWT will also automatically set the required alg
         * header with the associated algorithm identifier.
         */
        String jws = Jwts.builder()
                .setHeader((Map)header)
                .setSubject(subject)
                .signWith(key)
                .compact();

        /*
         * The JWA (JSON Web Algorithms (JWA) algorithm name constant.
         */
        String expectedAlgorithm = SignatureAlgorithm.HS256.getValue();

        // When
        Jws<Claims> parsedJws = null;
        try {
            parsedJws = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jws);
        } catch (JwtException e) {
            e.printStackTrace();
        }

        JwsHeader actualHeader = parsedJws.getHeader();

        String actualAlgorithm = actualHeader.getAlgorithm();
        String actualKeyId = actualHeader.getKeyId();

        // Then
        assertThat(actualAlgorithm)
                .as("The algorithm used in the jws should be '%s'",
                        expectedAlgorithm)
                .isEqualTo(expectedAlgorithm);
        assertThat(actualKeyId)
                .as("The actual key id should be '%s'", keyId)
                .isEqualTo(keyId);
    }

    /*
     * Claims are a JWT's 'body' and contain the information that the JWT
     * creator wishes to present to the JWT recipient(s).
     *
     * Standard Claims:
     *   1. setIssuer: sets the iss (Issuer) Claim
     *   2. setSubject: sets the sub (Subject) Claim
     *   3. setAudience: sets the aud (Audience) Claim
     *   4. setExpiration: sets the exp (Expiration Time) Claim
     *   5. setNotBefore: sets the nbf (Not Before) Claim
     *   6. setIssuedAt: sets the iat (Issued At) Claim
     *   7. setId: sets the jti (JWT ID) Claim
     */
    @DisplayName("Test standard registered claim settings - ")
    @Test
    void testStandardRegisteredClaimSettings() {

        // Given
        String issuer = "www.tecsys.com";
        String audience = "All";
        LocalDate localDate = LocalDate.of(2019, 12, 31);
        Date expiration = asDate(localDate);
        Date issuedAt = new Date();
        String jws = Jwts.builder()
                .setIssuer(issuer)
                .setSubject(subject)
                .setAudience(audience)
                .setExpiration(expiration)
                .setIssuedAt(issuedAt)
                .signWith(key)
                .compact();

        // When
        Claims parsedClaims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jws)
                .getBody();

        String actualIssuer = parsedClaims.getIssuer();
        String actualSubject = parsedClaims.getSubject();
        String actualAudience = parsedClaims.getAudience();
        Date actualExpiration = parsedClaims.getExpiration();
        Date actualIssuedAt = parsedClaims.getIssuedAt();

        // Then
        assertThat(actualIssuer).isEqualTo(issuer);
        assertThat(actualSubject).isEqualTo(subject);
        assertThat(actualAudience).isEqualTo(audience);
        assertThat(actualExpiration).isEqualTo(actualExpiration);
        assertThat(actualIssuedAt).hasDayOfMonth(
                LocalDate.now().getDayOfMonth());
    }

    @DisplayName("Test custom claim settings - ")
    @Test
    void testCustomClaimSettings() {

        // Given

        /*
         * Each time claim is called, it simply appends the key-value pair to
         * an internal Claims instance
         *
         * Potentially overwriting any existing identically-named key/value pair
         *
         * Can also specify all claims at once by using Jwts.claims()
         */
        String username = "tecuser";
        String jws = Jwts.builder()
                .setSubject(subject)
                .claim("username", username)
                .signWith(key)
                .compact();

        // When
        Claims parsedClaims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jws)
                .getBody();

        String actualUsername = (String) parsedClaims.get("username");

        // Then
        assertThat(actualUsername).isEqualTo(username);
    }

}///:~