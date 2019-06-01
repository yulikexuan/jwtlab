//: com.yulikexuan.utils.jwtlab.api.v1.controllers.SecureControl.java


package com.yulikexuan.security.jwtlab.api.v1.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/api/secured")
public class SecureControl {

    @GetMapping
    public String getSecureUser() {
        return UUID.randomUUID().toString();
    }

}///:~