package com.cqrs.controller.category;

import com.cqrs.application.category.city.queries.findone.FindOneQuery;
import com.cqrs.application.category.city.queries.findone.FindOneQueryResult;
import com.cqrs.base.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CityController {

    private final Mediator mediator;

    @GetMapping("/findOne")
    public Response<FindOneQueryResult> findOne(FindOneQuery query) {
        return mediator.send(query);
    }
}
