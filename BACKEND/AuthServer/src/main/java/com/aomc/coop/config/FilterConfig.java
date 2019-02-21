//package com.aomc.coop.config;
//
//import com.aomc.coop.filter.CORSFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FilterConfig {
//
//    @Bean
//    public FilterRegistrationBean getCORSFilterRegistrationBean() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CORSFilter());
//        registrationBean.addUrlPatterns("/api/*");
//        registrationBean.setName("corsFilter");
//        registrationBean.setOrder(1);
//        return registrationBean;
//    }
//}
