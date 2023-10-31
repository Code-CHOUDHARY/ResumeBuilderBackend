package com.resumebuilder.downloadhistory;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.resumebuilder.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="DownloadHistory")
public class DownloadHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long download_history_id;
	private String version;
	private String doc_link;
	private String pdf_link;
	private String modified_by;
	@CreationTimestamp
	private Date modified_on;
	private boolean is_deleted;
	
	@ManyToOne
    private User user; // Reference to the User entity

}
