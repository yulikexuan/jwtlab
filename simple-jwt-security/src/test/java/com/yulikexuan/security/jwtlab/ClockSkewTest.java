//: com.yulikexuan.utils.jwtlab.ClockSkewTest.java


package com.yulikexuan.security.jwtlab;


import com.yulikexuan.security.jwtlab.utils.SigningUtil;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;


public class ClockSkewTest {

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
    void able_To_Allow_Clock_Skew() {

        // Given
        long seconds = 3 * 60;

        Jws<Claims> parsedJws = null;
        try {
            /*
             * When parsing a JWT, you might find that exp or nbf claim
             * assertions fail (throw exceptions) because the clock on the
             * parsing machine is not perfectly in sync with the clock on the
             * machine that created the JWT.
             *
             * This can cause obvious problems since exp and nbf are time-based
             * assertions, and clock times need to be reliably in sync for
             * shared assertions.
             *
             * You can account for these differences (usually no more than a
             * few minutes) when parsing using the JwtParser's
             * setAllowedClockSkewSeconds
             *
             * This ensures that clock differences between the machines can be
             * ignored.
             *
             * Two or three minutes should be more than enough;
             */
            parsedJws = Jwts.parser()
                    .setAllowedClockSkewSeconds(seconds)
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
    }

}///:~