//: com.yulikexuan.security.securitylab.app.controllers.TokenController.java


package com.yulikexuan.security.securitylab.app.controllers;


import com.yulikexuan.security.securitylab.app.config.security.utils.SigningUtil;
import com.yulikexuan.security.securitylab.domain.model.AuthenticationToken;
import io.jsonwebtoken.Jwts;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@RequestMapping("/api/v1/token")
public class TokenController {

    private static final long EXPIRATION_SECONDS = 60;
    private static final String SECURITY_KEY = "!@asdsadJAS780";

    @PostMapping
    public AuthenticationToken generate(
            @RequestHeader("clientTxt") String clientTxt,
            @RequestHeader("clientId") String clientId) {

        String jws = Jwts.builder()
                .setSubject(clientTxt)
                .claim("role", "USER")
                .claim("Id", clientId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +
                        EXPIRATION_SECONDS * 1000))
                .signWith(SigningUtil.getHS256SecretKey())
                .compact();

        return new AuthenticationToken(jws);
    }

}///:~