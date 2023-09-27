package com.resumebuilder.bulkupload;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="BulkUpload")
public class BulkUpload {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bulk_upload_id;
	private String employee_name;
	private String employee_id;
	private String status;
	private String remark;

}
