package com.spenzr.data_aggregation_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    /**
     * Creates a customized ObjectMapper bean.
     * Spring Boot will automatically use this bean for all JSON serialization
     * and deserialization, including for the Kafka JsonDeserializer.
     * We explicitly register the JavaTimeModule to ensure it can handle
     * modern date/time types like LocalDate.
     *
     * @return A configured ObjectMapper instance.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
