package com.dcode7.iwell.utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
 *Created By Pavan on 21/6/19
 *
 */

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class ObjectMapperUtils {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(FORMATTER));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(FORMATTER));
        mapper.registerModule(javaTimeModule);
    }


    public static <E extends Object,T extends Object> T copyPropertiesByMapper(E object,Class<T> valueType){
        try {
            String json = mapper.writeValueAsString(object);
            return mapper.readValue(json, valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T extends Object,E extends Object> List<E> copyPropertiesOfListByMapper(List<T> objects, Class<?> className) {
        try {
            return mapper.readValue(mapper.writeValueAsString(objects), mapper.getTypeFactory().constructCollectionType(
                    List.class, className));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}