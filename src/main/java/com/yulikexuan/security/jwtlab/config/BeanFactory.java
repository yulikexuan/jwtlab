//: com.yulikexuan.security.jwtlab.config.BeanFactory.java


package com.yulikexuan.security.jwtlab.config;


import com.yulikexuan.security.jwtlab.filters.samples.RequestResponseLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanFactory {

    @Bean
    public FilterRegistrationBean<RequestResponseLoggingFilter> loggingFilter() {

        FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean =
                new FilterRegistrationBean<>();

        registrationBean.setFilter(new RequestResponseLoggingFilter());
        registrationBean.addUrlPatterns("/users/*");

        return registrationBean;
    }

}///:~