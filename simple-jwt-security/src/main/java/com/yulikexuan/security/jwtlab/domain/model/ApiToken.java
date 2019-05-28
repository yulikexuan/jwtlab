//: com.yulikexuan.security.jwtlab.domain.model.ApiToken.java


package com.yulikexuan.security.jwtlab.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiToken implements Serializable {

    private String token;

}///:~