//: com.yulikexuan.security.jwtlab.JwtCompressionTest.java


package com.yulikexuan.security.jwtlab;


import com.yulikexuan.security.jwtlab.utils.SigningUtil;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;


public class JwtCompressionTest {

    private String issuer;
    private String subject;
    private Long keyId;
    private Key key;
    private String payload;

    private String jws;
    private String compressedJws;

    private SigningKeyResolver signingKeyResolver;

    @BeforeEach
    void setUp() {
        this.issuer = "www.tecsys.com";
        this.subject = UUID.randomUUID().toString();
        this.keyId = (System.currentTimeMillis() % 2) == 0 ?
                SigningUtil.getKeyIds()[0] : SigningUtil.getKeyIds()[1];
        this.key = SigningUtil.getKey(this.keyId)
                .orElseThrow(IllegalArgumentException::new);
        this.signingKeyResolver = new SigningUtil.MySigningKeyResolver();

        this.payload = "MdLj6fnMYQ1epk5WhiA61AOxWO1v9aqWJ7yMdPygwB8da4k0s" +
                "32XOLOujCl4ZFGaETGYdvEnAv2TBW7mKtHt8qOhCJLSFGkvQDgaXb1rS" +
                "ZMQikqdxISVYB0PfS5xUXkXI4TpR9AHVSLuGwYswzQ1sgSu3DXP4ilID" +
                "PhYFmv8K620RXnRQjFDsfwItQkoUGXrAUHzWchOnt7aQZUI2YoyiWNMM" +
                "Vr0rBwHRHGDhqEh5xIBemuwNJPvUEdswHjEpIXih5iA4T7Kq6Xi87v1P" +
                "CDr1zJgbI5GVHejFL9li7k6PNLFmJWVzz4kkRg0WFztqpCPbZI8LV9dY" +
                "DM66n3HrkvyPRcxvf1wOwMwnu4xKblOQXGpURNId3HFKUO4yQ3iLZo4S" +
                "i1P8phaXr4CFS11";

        this.jws = Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, this.keyId)
                .setIssuer(this.issuer)
                .setSubject(this.subject)
                .claim("payload", this.payload)
                .signWith(this.key)
                .compact();

        this.compressedJws = Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, this.keyId)
                .setIssuer(this.issuer)
                .setSubject(this.subject)
                .claim("payload", this.payload)
                .signWith(this.key)
                .compressWith(CompressionCodecs.DEFLATE)
                .compact();
    }

    @DisplayName("JWS should be able to be compressed - ")
    @Test
    void testCompressedJwsSize() {

        // Given

        // When
        int compressedJwsSize = compressedJws.length();
        int jwsSize = this.jws.length();

        // Then
        assertThat(compressedJwsSize)
                .as("The length of compressed jws should be " +
                        "less than the regular one.")
                .isLessThan(jwsSize);
    }

    @DisplayName("Compressed JWS Can Still Be Parsed normally - ")
    @Test
    void testCompressedJwsCanStillBeParsed() {

        // When
        Jws<Claims> parsedJws = Jwts.parser()
                .require("payload", this.payload)
                .setSigningKeyResolver(this.signingKeyResolver)
                .parseClaimsJws(this.compressedJws);

        // Then
        assertThat(parsedJws).isNotNull();
    }


}///:~