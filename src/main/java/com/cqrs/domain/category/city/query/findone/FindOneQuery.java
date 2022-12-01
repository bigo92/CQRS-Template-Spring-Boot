package com.cqrs.domain.category.city.query.findone;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shortbus.Request;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindOneQuery implements Request<FindOneQueryResult> {

    private String id;
}
