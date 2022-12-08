package com.cqrs.application.category.city.commands.create;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommandResult {
    private String id;
    private String name;
}
