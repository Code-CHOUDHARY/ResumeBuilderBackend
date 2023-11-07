package com.resumebuilder.certifications;

import java.util.Date;import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.resumebuilder.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

//Certifications Entity class with all required fields.

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Certification")
public class Certifications {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long certification_id;
	private String certificate_name;
	private String certificate_date;
	private String certificate_url;
	private boolean show_dates;
	private String show_duration;
	private boolean show_nothing;
	private boolean is_deleted;
	
	@ManyToOne
    @JoinColumn(name = "employee_id")
    private User user; // Reference to the User entity

}
