package com.cqrs.application.category.city.queries.findone;

import com.cqrs.base.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindOneQuery implements Request<FindOneQueryResult> {

    private String id;
}
