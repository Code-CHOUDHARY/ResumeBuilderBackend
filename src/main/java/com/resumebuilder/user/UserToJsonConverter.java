package com.resumebuilder.user;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class UserToJsonConverter {
	
	ObjectMapper mapper = new ObjectMapper();
	
	public String convertUserToJSON(User user) throws JsonProcessingException {
		
		mapper.registerModule(new JavaTimeModule());
		
		return mapper.writeValueAsString(user);
	}
	public String convertChangesToJson(Map<String, String> changes) {
        try {
            // Convert the map of changes to a JSON string
            return mapper.writeValueAsString(changes);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); 
            return "{}"; // Return an empty JSON object as a fallback
        }
    }
}
