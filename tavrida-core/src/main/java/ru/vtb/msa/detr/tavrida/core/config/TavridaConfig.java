package ru.vtb.msa.detr.tavrida.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TavridaConfig {

    @Bean
    public ObjectMapper generalObjectMapper() {
        return new ObjectMapper();
    }

}
