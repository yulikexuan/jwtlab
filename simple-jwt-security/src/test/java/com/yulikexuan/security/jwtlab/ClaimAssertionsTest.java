//: com.yulikexuan.utils.jwtlab.ClaimAssertionsTest.java


package com.yulikexuan.security.jwtlab;

import com.yulikexuan.security.jwtlab.utils.SigningUtil;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ClaimAssertionsTest {

    private String issuer;
    private String subject;
    private String jws;
    private Long keyId;

    private SigningKeyResolver signingKeyResolver;

    @BeforeEach
    void setUp() {
        this.issuer = "www.tecsys.com";
        this.subject = UUID.randomUUID().toString();
        this.keyId = (System.currentTimeMillis() % 2) == 0 ?
                SigningUtil.getKeyIds()[0] : SigningUtil.getKeyIds()[1];
        this.signingKeyResolver = new SigningUtil.MySigningKeyResolver();

        this.jws = Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, this.keyId)
                .setIssuer(this.issuer)
                .setSubject(this.subject)
                .signWith(SigningUtil.getKey(this.keyId)
                        .orElseThrow(IllegalArgumentException::new))
                .compact();
    }

    @Test
    void able_To_Enforce_That_The_JWS_Being_Parsed_Conforms_To_Expectations() {

        // Given
        Jws<Claims> parsedJws = null;
        try {
            parsedJws = Jwts.parser()
                    .requireIssuer(this.issuer)
                    .requireSubject(this.subject)
                    .setSigningKeyResolver(this.signingKeyResolver)
                    .parseClaimsJws(this.jws);
        // } catch (InvalidClaimException  ice) {
            /*
             * If it is important to react to a missing vs an incorrect value,
             * instead of catching InvalidClaimException, you can catch either
             * MissingClaimException or IncorrectClaimException
             */
        } catch (MissingClaimException mce) {
            // the parsed JWT did not have the sub field
            System.out.println(mce.getMessage());
        } catch (IncorrectClaimException ice) {
            // the parsed JWT had a sub field, but its value was not equal to ???
            System.out.println(ice.getMessage());
        }

        // When
        Claims claims = parsedJws.getBody();

        // Then
        assertThat(claims.getSubject()).isEqualTo(this.subject);
        assertThat(claims.getIssuer()).isEqualTo(this.issuer);
    }

    @Test
    void able_To_Throw_InvalidClaimException_If_Cannot_Conform_Expectation() {

        // Given & When
        Throwable thrown = catchThrowable(() -> Jwts.parser()
                .requireSubject(this.issuer)
                // Require custom fields by using
                // the require(fieldName, requiredFieldValue) method
                .require("username", "yul")
                .setSigningKeyResolver(this.signingKeyResolver)
                .parseClaimsJws(this.jws));

        // Then
        assertThat(thrown)
                .isInstanceOf(InvalidClaimException.class);
        assertThat(thrown)
                .isInstanceOf(IncorrectClaimException.class);
    }

}///:~