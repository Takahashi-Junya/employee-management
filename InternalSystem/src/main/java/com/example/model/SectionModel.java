package com.example.model;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Table("sections")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SectionModel {

	private String sectionId;
	private String sectionName;
}
