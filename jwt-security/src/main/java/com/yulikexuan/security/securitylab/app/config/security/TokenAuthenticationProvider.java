//: com.yulikexuan.utils.securitylab.app.utils.TokenAuthenticationProvider.java


package com.yulikexuan.security.securitylab.app.config.security;


import com.yulikexuan.security.securitylab.app.config.security.utils.SigningUtil;
import com.yulikexuan.security.securitylab.domain.model.AuthenticationToken;
import com.yulikexuan.security.securitylab.domain.model.LoginUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


/*
 * A base AuthenticationProvider that allows subclasses to override and work
 * with UserDetails objects.
 *
 * The class is designed to respond to UsernamePasswordAuthenticationToken
 * authentication requests
 *
 * Upon successful validation, a UsernamePasswordAuthenticationToken will be
 * created and returned to the caller
 *
 * The token will include as its principal either a String representation of
 * the username, or the UserDetails that was returned from the authentication
 * repository
 */
@Slf4j
@Component
public class TokenAuthenticationProvider
        extends AbstractUserDetailsAuthenticationProvider {

    /*
     * Perform any additional checks of a returned (or cached) UserDetails for
     * a given authentication request
     *
     * Generally a subclass will at least compare the
     * Authentication.getCredentials() with a UserDetails.getPassword()
     *
     * If custom logic is needed to compare additional properties of
     * UserDetails and/or UsernamePasswordAuthenticationToken, these should
     * also appear in this method
     */
    @Override
    protected void additionalAuthenticationChecks(
            UserDetails userDetails,
            UsernamePasswordAuthenticationToken authenticationToken)
            throws AuthenticationException {

        log.info(">>>>>>> In TokenAuthenticationProvider - additional authentication checks ... ...");
    }

    /*
     * Allows subclasses to actually retrieve the UserDetails from an
     * implementation-specific location, with the option of throwing an
     * AuthenticationException immediately if the presented credentials are
     * incorrect (this is especially useful if it is necessary to bind to a
     * resource as the user in order to obtain or generate a UserDetails).
     */
    @Override
    protected UserDetails retrieveUser(
            String username,
            UsernamePasswordAuthenticationToken authenticationToken)
            throws AuthenticationException {

        log.info(">>>>>>> In TokenAuthenticationProvider - retrieve user details ... ...");

        AuthenticationToken jwtToken = (AuthenticationToken) authenticationToken;
        String token = jwtToken.getToken();

        Claims claim = Jwts.parser()
                .setSigningKey(SigningUtil.getHS256SecretKey())
                .parseClaimsJws(token)
                .getBody();

        return LoginUserDetails.builder()
                .username(claim.getSubject())
                .id(Long.parseLong((String)claim.get("id")))
                .token(token)
                .build();
    }

}///:~