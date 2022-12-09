package com.cqrs.application.category.city.queries.findone;

import org.springframework.stereotype.Component;

import com.cqrs.base.RequestHandler;

@Component
public class FindOneQueryHandler implements RequestHandler<FindOneQuery, FindOneQueryResult> {

    @Override
    public FindOneQueryResult handle(FindOneQuery request) {
        //
        return new FindOneQueryResult("Bach");

    }
}
