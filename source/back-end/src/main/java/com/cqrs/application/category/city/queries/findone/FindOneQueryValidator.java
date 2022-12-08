package com.cqrs.application.category.city.queries.findone;

import br.com.fluentvalidator.AbstractValidator;

public class FindOneQueryValidator extends AbstractValidator<FindOneQuery> {

    @Override
    public void rules() {
        ruleFor(FindOneQuery::getId)
                .must(x -> x != null && !x.isEmpty())
                .withMessage("Không được để trống")
                .must(x -> x.length() <= 8)
                .withMessage("Mã không được lớn hơn 8 ký tự")
                .withFieldName("id");
    }

}
