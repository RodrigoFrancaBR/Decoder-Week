package com.ead.course.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_MODULES")
public class ModuleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID moduleId;
	@Column(nullable = false, length = 150)
	private String title;
	@Column(nullable = false, length = 250)
	private String description;	
	@Column(nullable = false)
	private LocalDateTime creationDate;

}
