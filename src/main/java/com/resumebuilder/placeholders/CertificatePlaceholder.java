package com.resumebuilder.placeholders;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@NoArgsConstructor
public class CertificatePlaceholder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id; 
	
    private String 	name;
    private String placeholder;
}
