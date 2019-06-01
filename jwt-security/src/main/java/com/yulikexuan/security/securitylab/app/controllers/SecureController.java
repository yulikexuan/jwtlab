//: com.yulikexuan.security.securitylab.app.controllers.SecureController.java


package com.yulikexuan.security.securitylab.app.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/vi/secured/greeting")
public class SecureController {

    @GetMapping
    public String securedMethod() {
        return "Hello JWT!";
    }

}///:~