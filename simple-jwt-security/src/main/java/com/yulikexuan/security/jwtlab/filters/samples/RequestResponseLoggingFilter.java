//: com.yulikexuan.utils.jwtlab.filters.samples.RequestResponseLoggingFilter.java


package com.yulikexuan.security.jwtlab.filters.samples;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@Order(2)
//@Component
public class RequestResponseLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final HttpServletResponse res = (HttpServletResponse) servletResponse;

        log.info(">>>>>>> Logging Request {} : {}", req.getMethod(),
                req.getRequestURL());

        filterChain.doFilter(servletRequest, servletResponse);

        log.info(">>>>>>> Logging Response Content Type: {}",
                res.getContentType());
    }

}///:~