//: com.yulikexuan.security.jwtlab.filters.samples.TransactionFilter.java


package com.yulikexuan.security.jwtlab.filters.samples;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/*
 * In order for Spring to be able to recognize a filter, we needed to define it
 * as a bean with the @Component annotation
 *
 * To have the filters fire in the right order – we needed to use the @Order
 * annotation
 */
@Slf4j
@Order(1)
@Component
public class TransactionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        log.info(">>>>>>> Starting a transaction for request: {}",
                req.getRequestURI());

        filterChain.doFilter(servletRequest, servletResponse);

        log.info(">>>>>>> Committing a transaction for request: {}",
                req.getRequestURI());
    }

}///:~