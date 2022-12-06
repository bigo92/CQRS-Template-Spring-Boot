package com.cqrs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cqrs.base.*;

@Configuration
public class WebConfig {

    @Bean
    public Mediator getMediator() {
        return new MediatorImpl();
    }
}
