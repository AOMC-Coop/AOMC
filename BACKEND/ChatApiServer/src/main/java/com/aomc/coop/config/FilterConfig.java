package com.aomc.coop.config;

import com.aomc.coop.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean getCORSFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CorsFilter());
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setName("corsFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
