package com.cqrs;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shortbus.Mediator;
import shortbus.MediatorImpl;

@Configuration
public class WebConfig {

    @Bean
    public Mediator getMediator() {
        return new MediatorCustomImpl();
    }
}
