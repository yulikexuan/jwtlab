//: com.yulikexuan.utils.securitylab.domain.model.AuthenticationToken.java


package com.yulikexuan.security.securitylab.domain.model;


import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.security.auth.Subject;


@Data
public class AuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = 1L;

    private String token;

    public AuthenticationToken(String token) {
        super(null, null, null);
        this.token = token;
    }

    /*
     * The credentials that prove the principal is correct.
     * This is usually a password, but could be anything relevant to the
     * AuthenticationManager.
     *
     * Callers are expected to populate the credentials.
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /*
     * The identity of the principal being authenticated.
     * In the case of an authentication request with username and password,
     * this would be the username.
     *
     * Callers are expected to populate the principal for an authentication
     * request.
     */
    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

}///:~