package com.cqrs.application.category.city.commands.create;

import com.cqrs.base.RequestHandler;

public class CreateCommandHandler implements RequestHandler<CreateCommand, CreateCommandResult> {

    @Override
    public CreateCommandResult handle(CreateCommand request) {
        return new CreateCommandResult("123","sada");
    }
    
}
