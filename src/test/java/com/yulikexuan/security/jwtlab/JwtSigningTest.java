//: com.yulikexuan.security.jwtlab.JwtSigningTest.java


package com.yulikexuan.security.jwtlab;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.yulikexuan.security.jwtlab.utils.EncodeUtil.*;
import static com.yulikexuan.security.jwtlab.utils.SigningUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class JwtSigningTest {

    /*
     * This key is being used to validate the signature of the JWT.
     */
    private Key key;

    private String encodedKey;
    private String subject;
    private Base64.Encoder encoder;

    private String header;
    private String claims;

    @BeforeEach
    void setUp() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        encoder = Base64.getEncoder();
        encodedKey = encoder.encodeToString(key.getEncoded());
        subject = "Yul";
        header = "{\"alg\":\"HS256\"}";
        claims = "{\"sub\":\"Yul\"}";
    }

    @Test
    void testJwtSigning() throws InvalidKeyException, NoSuchAlgorithmException {

        // Givne
        String encodedHeader = base64URLEncode(
                header.getBytes(StandardCharsets.UTF_8));
        String encodedClaims = base64URLEncode(
                claims.getBytes(StandardCharsets.UTF_8));

        String concatenated = encodedHeader + '.' + encodedClaims;

        Key key = getHS256SecretKey();
        byte[] data = concatenated.getBytes(StandardCharsets.UTF_8);
        byte[] signedData = hmacSha256(data, key);
        String jws = concatenated + "." + base64URLEncode(signedData);

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
        System.out.println(encodedHeader);
        System.out.println(encodedClaims);
        System.out.println(jws);
        assertThat(actualSubject)
                .as("The subject in the jws should be %s", actualSubject)
                .isEqualTo(actualSubject);
    }

}///:~