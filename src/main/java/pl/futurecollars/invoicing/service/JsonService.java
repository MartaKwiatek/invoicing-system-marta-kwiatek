package pl.futurecollars.invoicing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

@Service
public class JsonService {

    private final ObjectMapper objectMapper;

    public JsonService() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public String objectToJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object) + "\n";
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Serialization from object to json string failed", e);
        }
    }

    public <T> T stringToObject(String objectAsString, Class<T> className) {
        try {
            return objectMapper.readValue(objectAsString, className);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Serialization from string to object failed", e);
        }
    }
}
