package com.resumebuilder.education;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.resumebuilder.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Education")
public class Education {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long education_id;
	private String school_college;
	private String degree;
	@JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
	private Date start_date;
	@JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
	private Date end_date;
	private boolean show_dates;
	private String show_duration;
	private boolean show_nothing;
	private boolean is_deleted;

	private String Modified_by;
	@UpdateTimestamp
	@Column(name = "modified_on")
	private LocalDateTime modified_on;
	

	@ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
