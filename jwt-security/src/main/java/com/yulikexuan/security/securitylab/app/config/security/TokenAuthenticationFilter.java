//: com.yulikexuan.utils.securitylab.app.utils.TokenAuthenticationFilter.java


package com.yulikexuan.security.securitylab.app.config.security;


import com.yulikexuan.security.securitylab.domain.model.AuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/*
 * Authentication Process:
 * AbstractAuthenticationProcessingFilter filter requires that you set the
 * authenticationManager property
 * An AuthenticationManager is required to process the authentication request
 * tokens created by implementing classes
 *
 * This filter will intercept a request and attempt to perform authentication
 * from that request if the request matches the
 * setRequiresAuthenticationRequestMatcher(RequestMatcher)
 *
 * Authentication is performed by the attemptAuthentication method, which must
 * be implemented by subclasses
 *
 * Authentication Success:
 * If authentication is successful, the resulting Authentication object will be
 * placed into the SecurityContext for the current thread, which is guaranteed
 * to have already been created by an earlier filter
 *
 * The configured AuthenticationSuccessHandler will then be called to take the
 * redirect to the appropriate destination after a successful login
 *
 * The default behaviour is implemented in a
 * SavedRequestAwareAuthenticationSuccessHandler which will make use of any
 * DefaultSavedRequest set by the ExceptionTranslationFilter and redirect the
 * user to the URL contained therein
 *
 * Otherwise it will redirect to the webapp root "/"
 * You can customize this behaviour by injecting a differently configured
 * instance of this class, or by using a different implementation
 *
 * See the successfulAuthentication(HttpServletRequest, HttpServletResponse,
 * FilterChain, Authentication) method for more information.
 *
 * Authentication Failure
 * If authentication fails, it will delegate to the configured
 * AuthenticationFailureHandler to allow the failure information to be conveyed
 * to the client
 * The default implementation is SimpleUrlAuthenticationFailureHandler , which
 * sends a 401 error code to the client.
 * It may also be configured with a failure URL as an alternative.
 * Again you can inject whatever behaviour you require here.
 *
 * Event Publication
 * If authentication is successful, an InteractiveAuthenticationSuccessEvent
 * will be published via the application context.
 * No events will be published if authentication was unsuccessful, because this
 * would generally be recorded via an AuthenticationManager-specific application
 * event
 *
 * Session Authentication
 * The class has an optional SessionAuthenticationStrategy which will be invoked
 * immediately after a successful call to attemptAuthentication().
 * Different implementations can be injected to enable things like
 * session-fixation attack prevention or to control the number of simultaneous
 * sessions a principal may have
 *
 */
@Slf4j
public class TokenAuthenticationFilter
        extends AbstractAuthenticationProcessingFilter {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "qwerty ";

    public TokenAuthenticationFilter() {
        super("/api/secured/**");
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException, ServletException {

        log.info(">>>>>>> In TokenAuthenticationFilter - attempt authenticate ... ...");

        AuthenticationToken token = validateHeader(
                httpServletRequest.getHeader(TOKEN_HEADER));
        if (ObjectUtils.isEmpty(token)) {
            throw new ServletException("401 - UNAUTHORIZED");
        }
        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        log.info(">>>>>>> In TokenAuthenticationFilter - successful authentication ... ...");
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

    private AuthenticationToken validateHeader(String authenticationHeader) {
        if (StringUtils.isBlank(authenticationHeader) ||
                !authenticationHeader.startsWith(TOKEN_PREFIX)) {
            return null;
        }
        return new AuthenticationToken(authenticationHeader.replace(
                TOKEN_PREFIX, ""));
    }

}///:~