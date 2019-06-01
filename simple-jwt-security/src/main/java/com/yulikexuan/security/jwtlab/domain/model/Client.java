//: com.yulikexuan.utils.jwtlab.domain.model.Client.java


package com.yulikexuan.security.jwtlab.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    private int clientId;
    private String clientName;
    private String passwordTxt;

}///:~