package ru.vtb.msa.detr.tavrida.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode readTree(String eventObject) throws JsonProcessingException {
        return objectMapper.readTree(eventObject);
    }

    public static String writeValue(JsonNode node) throws JsonProcessingException {
        return objectMapper.writeValueAsString(node);
    }

}
