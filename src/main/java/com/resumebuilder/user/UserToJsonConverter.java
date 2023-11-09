package com.resumebuilder.user;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserToJsonConverter {
	
	ObjectMapper mapper = new ObjectMapper();
	
	public String convertUserToJSON(User user) throws JsonProcessingException {
		
		mapper.registerModule(new JavaTimeModule());
		
		String userJson = mapper.writeValueAsString(user);
		ObjectNode filteredJson = mapper.readValue(userJson, ObjectNode.class);
		filteredJson.remove("password");
		filteredJson.remove("modified_on");
		filteredJson.remove("modified_by");
		filteredJson.remove("roles");
		filteredJson.remove("projects");
		filteredJson.remove("assignedProjects");
		filteredJson.remove("technologies");
		filteredJson.remove("activityHistories");
		filteredJson.remove("appRole");
		filteredJson.remove("_deleted");

		return filteredJson.toString();
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
