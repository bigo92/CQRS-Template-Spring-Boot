package com.cqrs.domain.category.city.query.findone;

import org.springframework.stereotype.Component;
import shortbus.RequestHandler;

@Component
public class FindOneQueryHandler implements RequestHandler<FindOneQuery, FindOneQueryResult> {

    @Override
    public FindOneQueryResult handle(FindOneQuery request) {
        return new FindOneQueryResult("Bach");

    }
}
