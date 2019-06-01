//: com.yulikexuan.utils.jwtlab.JwtSigningTest.java


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

    private String subject;
    private String encodedKey;
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

        /*
         * 1. Assume the json header and json claims are ready for JWT
         * 2. Remove all unnecessary whitespace in the json
         *    header = "{\"alg\":\"HS256\"}";
         *    claims = "{\"sub\":\"Yul\"}";
         * 3. Get the UTF-8 bytes and Base64URL-encode each
         */
        String encodedHeader = base64URLEncode(
                header.getBytes(StandardCharsets.UTF_8));
        String encodedClaims = base64URLEncode(
                claims.getBytes(StandardCharsets.UTF_8));

        /*
         * 4. Concatenate the encoded header and claims with a period character
         *    between them
         */
        String concatenated = encodedHeader + '.' + encodedClaims;

        /*
         * 5. Use a sufficiently strong cryptographic secret or private key
         *    along with a signing algorithm and sign the concatenated string
         *
         * 6. Because signatures are always byte arrays, Base64URL-encode the
         *    signature and append it to the concatenated string with a period
         *    "."
         */
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