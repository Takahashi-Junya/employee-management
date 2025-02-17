package com.example.demos.web.form;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SectionSummaryForm {

	private String sectionId;
	private int totalSales;
	private int totalEmployees;
	private int avgSalesPerPerson;
}
