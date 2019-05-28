//: com.yulikexuan.security.jwtlab.api.v1.controllers.LoginController.java


package com.yulikexuan.security.jwtlab.api.v1.controllers;


import com.yulikexuan.security.jwtlab.domain.model.ApiToken;
import com.yulikexuan.security.jwtlab.domain.model.Client;
import com.yulikexuan.security.jwtlab.utils.SigningUtil;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@RequestMapping("/api/login")
public class LoginController {

    @PostMapping
    public ResponseEntity<ApiToken> login(@RequestBody Client client) {

        ApiToken apiToken = new ApiToken(Jwts.builder()
                .setSubject(client.getClientName())
                .claim("roles", "user")
                .setIssuedAt(new Date())
                .signWith(SigningUtil.getHS256SecretKey())
                .compact());

        ResponseEntity<ApiToken> responseEntity =
                new ResponseEntity<>(apiToken, HttpStatus.OK);

        return responseEntity;
    }

}///:~