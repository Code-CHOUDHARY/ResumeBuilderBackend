package com.resumebuilder.placeholders;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class PlaceholderMapping {
	Map<String, String> profileMap ;
    Map<String, String> technologyMap ;
    Map<String, String> experienceMap;
    Map<String, String> projectMap;
    Map<String, String> certificateMap ;
}
