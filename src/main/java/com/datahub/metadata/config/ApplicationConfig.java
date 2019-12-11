package com.datahub.metadata.config;

import com.datahub.metadata.utils.IncomingRequestFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@Configuration
@SpringBootApplication
@ComponentScan("com.datahub.metadata")
public class ApplicationConfig implements WebMvcConfigurer{

    @Bean
    public Filter incomingRequestFilter() {
        return new IncomingRequestFilter();
    }

   /* @Bean
    public MethodInvokingFactoryBean invokeFactory() {
        ReloadableResourceBundleMessageSource resourceBundle = new ReloadableResourceBundleMessageSource();
        resourceBundle.setBasename("classpath:messages");

        Object[] args = new Object[1];
        args[0] = resourceBundle;

        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setArguments(args);
        bean.setStaticMethod("com.datahub.metadata.util.MessageService.setMessageSource");

        return bean;
    }*/
}

