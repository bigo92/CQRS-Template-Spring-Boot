package com.cqrs.application.category.city.query.findone;

import br.com.fluentvalidator.AbstractValidator;

public class FindOneQueryValidator extends AbstractValidator<FindOneQuery> {

    @Override
    public void rules() {
        setPropertyOnContext("findOneQuery");

        ruleFor(FindOneQuery::getId)
                .must(x -> x == null || !x.isEmpty())
                .withMessage("Không được để trống")
                .withFieldName("id");
    }

}
