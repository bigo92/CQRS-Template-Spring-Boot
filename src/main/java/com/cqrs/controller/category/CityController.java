package com.cqrs.controller.category;

import com.cqrs.domain.category.city.query.findone.FindOneQuery;
import com.cqrs.domain.category.city.query.findone.FindOneQueryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shortbus.Mediator;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CityController {

    private final Mediator mediator;

    @GetMapping("/findOne")
    public FindOneQueryResult findOne(FindOneQuery query) {
        return mediator.request(query).data;
    }
}
