//: com.yulikexuan.security.jwtlab.api.v1.controllers.UserController.java


package com.yulikexuan.security.jwtlab.api.v1.controllers;


import com.yulikexuan.security.jwtlab.domain.model.Client;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping()
    public List<Client> getAllUsers() {

        Client client = new Client(1, "yul@tecsys.com",
                "");

        return List.of(client);
    }

    @GetMapping("/any")
    public List<Client> getAnyUser() {

        Client client = new Client(1, "any@tecsys.com",
                "");

        return List.of(client);
    }

}///:~