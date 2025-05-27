package org.pencil.web.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author pencil
 * @date 25/05/27 10:46
 */
public class CustomMessageConverterConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        converters.add(0, new MappingJackson2HttpMessageConverter(objectMapper));
    }
}
