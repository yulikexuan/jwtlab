//: com.yulikexuan.utils.jwtlab.SigningKeyResolverTest.java


package com.yulikexuan.security.jwtlab;


import com.yulikexuan.security.jwtlab.utils.SigningUtil;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class SigningKeyResolverTest {

    private String subject;
    private String jws;
    private Long keyId;

    private SigningKeyResolver signingKeyResolver;

    @BeforeEach
    void setUp() {

        this.subject = UUID.randomUUID().toString();
        this.keyId = (System.currentTimeMillis() % 2) == 0 ?
                SigningUtil.getKeyIds()[0] : SigningUtil.getKeyIds()[1];
        this.signingKeyResolver = new SigningUtil.MySigningKeyResolver();

        this.jws = Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, this.keyId)
                .setSubject(this.subject)
                .signWith(SigningUtil.getKey(this.keyId)
                        .orElseThrow(IllegalArgumentException::new))
                .compact();
    }

    @Test
    void testSigningKeyResolverWithKeyId() {

        // Given & When
        Jws<Claims> parsedJws = Jwts.parser()
                .setSigningKeyResolver(this.signingKeyResolver)
                .parseClaimsJws(this.jws);

        String actualSubject = parsedJws.getBody().getSubject();

        // Then
        assertThat(actualSubject).isEqualTo(this.subject);
    }

}///:~