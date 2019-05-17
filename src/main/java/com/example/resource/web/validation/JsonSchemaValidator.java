package com.example.resource.web.validation;

import com.example.resource.config.ValidationProperties;
import com.example.resource.web.dto.Dto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JsonSchemaValidator implements ConstraintValidator<JsonSchemaValidation, Dto> {

    private static final String JSON_FILE_SUFFIX = ".json";
    private static final Set<LogLevel> ERROR_LEVELS = Set.of(LogLevel.ERROR, LogLevel.FATAL);

    private final ObjectMapper jsonSerializer;
    private final JsonSchemaFactory jsonSchemaFactory;
    private final PathMatchingResourcePatternResolver resourceResolver;
    private final ValidationProperties validationProperties;

    private String schemaPath;

    @Autowired
    public JsonSchemaValidator(ObjectMapper jsonSerializer, JsonSchemaFactory jsonSchemaFactory, ValidationProperties validationProperties) {
        this.jsonSerializer = jsonSerializer;
        this.jsonSchemaFactory = jsonSchemaFactory;
        this.validationProperties = validationProperties;
        this.resourceResolver = new PathMatchingResourcePatternResolver();
    }

    @Override
    public void initialize(JsonSchemaValidation constraintAnnotation) {
        this.schemaPath = validationProperties.getSchemaPath() + constraintAnnotation.value() + JSON_FILE_SUFFIX;
    }

    @Override
    public boolean isValid(Dto dto, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        final URI jsonSchemaURI;
        try {
            jsonSchemaURI = resourceResolver.getResource(schemaPath).getURL().toURI();
        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException("Could not resolve resource at: " + schemaPath, e);
        }

        final JsonSchema schema;
        final ProcessingReport validationReport;
        try {
            schema = jsonSchemaFactory.getJsonSchema(jsonSchemaURI.toString());
            validationReport = schema.validate(jsonSerializer.valueToTree(dto));
        } catch (ProcessingException e) {
            throw new IllegalStateException("Could not process schema at: " + schemaPath, e);
        }

        for (ProcessingMessage processingMessage : validationReport) {
            if (ERROR_LEVELS.contains(processingMessage.getLogLevel())) {
                context.buildConstraintViolationWithTemplate(processingMessage.getMessage()).addConstraintViolation();
            }
        }
        return validationReport.isSuccess();
    }
}
