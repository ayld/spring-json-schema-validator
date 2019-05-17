package com.example.resource.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration;
import com.github.fge.jsonschema.core.load.configuration.LoadingConfigurationBuilder;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

@Configuration
public class ValidationConfig {

    private static final String SCHEMAS_PATH_SUFFIX = "*-schema.json";
    private final String schemasPattern;

    @Autowired
    public ValidationConfig(ValidationProperties validationProperties) {
        this.schemasPattern = validationProperties.getSchemaPath() + SCHEMAS_PATH_SUFFIX;
    }

    @Bean
    public JsonSchemaFactory jsonStore() throws IOException, URISyntaxException {
        final PathMatchingResourcePatternResolver schemaResolver = new PathMatchingResourcePatternResolver();
        final Resource[] resources = schemaResolver.getResources(schemasPattern);

        final LoadingConfigurationBuilder builder = LoadingConfiguration.newBuilder();
        for (Resource resource : resources) {
            final URL resourceUrl = resource.getURL();
            final JsonNode jsonNode = JsonLoader.fromURL(resourceUrl);

            builder.preloadSchema(resourceUrl.toURI().toString(), jsonNode);
        }
        return JsonSchemaFactory.newBuilder().setLoadingConfiguration(builder.freeze()).freeze();
    }
}
