package com.cqrs.application.category.city.commands.create;

import br.com.fluentvalidator.AbstractValidator;

public class CreateCommandValidator extends AbstractValidator<CreateCommand> {

    @Override
    public void rules() {
        ruleFor(CreateCommand::getId)
                .must(x -> x != null && !x.isEmpty())
                .withMessage("Không được để trống")
                .must(x -> x.length() <= 20)
                .withMessage("Mã không được lớn hơn 20 ký tự")
                .withFieldName("id");

        ruleFor(CreateCommand::getName)
                .must(x -> x != null && !x.isEmpty())
                .withMessage("Không được để trống")
                .must(x -> x.length() <= 200)
                .withMessage("Tên không được lớn hơn 200 ký tự")
                .withFieldName("name");

    }

}
