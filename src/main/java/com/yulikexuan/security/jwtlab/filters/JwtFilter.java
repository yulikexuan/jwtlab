//: com.yulikexuan.security.jwtlab.filters.JWTFilter.java


package com.yulikexuan.security.jwtlab.filters;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


//@Component
public class JwtFilter extends GenericFilterBean {

    static final String HEADER_NAME_AUTHORIZATION = "authorization";
    static final String HEADER_VALUE_PREFIX_AUTHORIZATION_JWT = "Bearer ";

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final String authorization = request.getHeader(HEADER_NAME_AUTHORIZATION);

        if (authorization == null || !authorization.startsWith(
                HEADER_VALUE_PREFIX_AUTHORIZATION_JWT)) {

            filterChain.doFilter(servletRequest, servletResponse);
            // throw new ServletException("401 - UNAUTHORIZED");
        }

        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey("123#&*zcvAWEE999")
                    .parseClaimsJws(authorization.substring(7))
                    .getBody();
            request.setAttribute("claims", claims);
        } catch (final SignatureException e) {
            throw new ServletException("401 - UNAUTHORIZED");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}///:~