package com.example.demos.web.form;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePageResponse {

	private List<EmployeesForm> eFormList;
	private int totalCount;
	private int totalPages;
}
