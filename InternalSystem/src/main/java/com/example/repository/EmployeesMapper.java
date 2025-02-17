package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demos.web.form.SearchForm;
import com.example.model.EmployeesModel;

@Mapper
public interface EmployeesMapper {
	
	public List<EmployeesModel> findEmployees(String status, String sortBy, String order, int size, int offset, String sectionId);
	
	public int countEmployees(String status, String sectionId);

	public List<EmployeesModel> findAll();
	
	public List<EmployeesModel> findEmployeesBySectionId(String sectionId);
	
	public void insertOne(EmployeesModel employeesModel);
	
	public EmployeesModel findOne(String id);
	
	public void update(EmployeesModel employeesModel);

	public List<EmployeesModel> findMany(SearchForm searchForm);
}
