package com.example.resource.web.dto;

import com.example.resource.web.validation.JsonSchemaValidation;
import lombok.Data;

@Data
@JsonSchemaValidation("user-schema")
public class UserDto implements Dto{
    private String twoNames;
    private String email;
    private Integer age;
}
