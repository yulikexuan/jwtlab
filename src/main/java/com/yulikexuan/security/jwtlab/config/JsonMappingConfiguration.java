//: com.yulikexuan.security.jwtlab.config.JsonMappingConfiguration.java


package com.yulikexuan.security.jwtlab.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.JacksonDeserializer;
import io.jsonwebtoken.io.JacksonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JsonMappingConfiguration {

    private static ObjectMapper objectMapper;
    private static JacksonSerializer jacksonSerializer;
    private static JacksonDeserializer jacksonDeserializer;

    static {
        objectMapper = new ObjectMapper();
        jacksonSerializer = new JacksonSerializer(objectMapper);
        jacksonDeserializer = new JacksonDeserializer(objectMapper);
    }

    @Bean
    public static ObjectMapper objectMapper() {
        return objectMapper;
    }

    @Bean
    public static JacksonSerializer jacksonSerializer() {
        return jacksonSerializer;
    }

    @Bean
    public static JacksonDeserializer jacksonDeserializer() {
        return jacksonDeserializer;
    }

}///:~