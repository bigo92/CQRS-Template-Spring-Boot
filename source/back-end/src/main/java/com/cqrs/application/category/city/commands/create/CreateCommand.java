package com.cqrs.application.category.city.commands.create;

import com.cqrs.base.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommand implements Request<CreateCommandResult> {
    private String id;

    private String name;
}
