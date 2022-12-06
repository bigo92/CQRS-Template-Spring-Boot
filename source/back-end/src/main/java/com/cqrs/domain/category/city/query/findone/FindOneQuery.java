package com.cqrs.domain.category.city.query.findone;

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
