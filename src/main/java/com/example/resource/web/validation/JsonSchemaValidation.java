package com.example.resource.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables JSON Schema validation on an object by spring.
 *
 * @since 0.0.1
 * @author Simeon Iliev
 * */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JsonSchemaValidator.class)
public @interface JsonSchemaValidation {

    /**
     * Defines the name of the schema to use.
     * Expected on the classpath.
     * */
    String value();

    String message() default "Unable to validate JSON schema";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

