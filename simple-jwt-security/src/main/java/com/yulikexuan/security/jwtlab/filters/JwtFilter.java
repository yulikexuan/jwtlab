//: com.yulikexuan.security.jwtlab.filters.JWTFilter.java


package com.yulikexuan.security.jwtlab.filters;


import com.yulikexuan.security.jwtlab.utils.SigningUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/*
 * In order for Spring to be able to recognize a filter, we needed to define it
 * as a bean with the @Component annotation
 *
 * However, if want this filter to only apply to certain URL patterns,
 * annotation @Component has to be removed and this filter has to be registered
 * in a FilterRegistrationBean
 *
 * To have the filters fire in the right order – we needed to use the @Order
 * annotation
 */
@Slf4j
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

            logger.error(">>>>>>> !!! Unauthorized !!! <<<<<<<");

            // throw new ServletException("401 - UNAUTHORIZED");

        } else {
            try {
                final Claims claims = Jwts.parser()
                        .setSigningKey(SigningUtil.getHS256SecretKey())
                        .parseClaimsJws(authorization.substring(7))
                        .getBody();
                request.setAttribute("claims", claims);
            } catch (final SignatureException e) {
                logger.error(">>>>>>> !!! Unauthorized !!! <<<<<<<");
                throw new ServletException("401 - UNAUTHORIZED");
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}///:~