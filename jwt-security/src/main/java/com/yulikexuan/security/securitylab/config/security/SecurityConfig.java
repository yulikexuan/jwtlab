//: com.yulikexuan.security.securitylab.config.security.SecurityConfig.java


package com.yulikexuan.security.securitylab.config.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;


/*
 * WebSecurityConfigurer<T extends SecurityBuilder<javax.servlet.Filter>>:
 * Allows customization to the WebSecurity.
 * In most instances users will use EnableWebSecurity and a create Configuration
 * that extends WebSecurityConfigurerAdapter which will automatically be applied
 * to the WebSecurity by the EnableWebSecurity annotation
 *
 * WebSecurityConfigurerAdapter:
 * Provides a convenient base class for creating a WebSecurityConfigurer instance
 *
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenAuthenticationProvider tokenAuthenticationProvider;

    @Autowired
    public SecurityConfig(TokenAuthenticationProvider tokenAuthenticationProvider) {
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
    }

    /*
     * Override this method to expose the AuthenticationManager from
     * configure(AuthenticationManagerBuilder) to be exposed as a Bean
     *
     * AuthenticationManager:
     * Processes an Authentication request
     * Authentication authenticate(Authentication authentication)
     *     throws AuthenticationException
     * Attempts to authenticate the passed Authentication object,
     * returning a fully populated Authentication object
     * (including granted authorities) if successful.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public TokenAuthenticationFilter authenticationTokenFilter() throws Exception {
        TokenAuthenticationFilter filter = new TokenAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(new TokenAuthenticationSuccessHandler());
        return filter;
    }

    /*
     * Used by the default implementation of authenticationManager() to attempt
     * to obtain an AuthenticationManager
     * If overridden, the AuthenticationManagerBuilder should be used to specify
     * the AuthenticationManager
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(tokenAuthenticationProvider);
    }

    /*
     * Override this method to configure the HttpSecurity
     * Typically subclasses should not invoke this method by calling super as
     * it may override their configuration
     * The default configuration is:
     * http.authorizeRequests()
     *      .anyRequest()
     *      .authenticated()
     *      .and()
     *      .formLogin()
     *      .and()
     *      .httpBasic();
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("**/secured/**")
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, exception) -> response
                                .sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                        "401 - UNAUTHORIZED"))
                .and()
                .addFilterBefore(authenticationTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class);
    }

}///:~
