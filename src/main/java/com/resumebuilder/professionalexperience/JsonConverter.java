package com.resumebuilder.professionalexperience;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	/**
     * Converts a Java object to JSON.
     *
     * @param object The Java object to convert.
     * @param <T>    The type of the Java object.
     * @return JSON representation of the object.
     * @throws Exception If there is an error during the conversion.
     */
    public static <T> String convertToJson(T object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }
	
}
