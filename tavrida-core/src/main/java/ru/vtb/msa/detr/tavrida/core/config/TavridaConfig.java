package ru.vtb.msa.detr.tavrida.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TavridaConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Включаем поддержку java.time
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Для вывода дат в ISO-8601 формате
        return mapper;
    }

}
