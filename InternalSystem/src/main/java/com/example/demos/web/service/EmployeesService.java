package com.example.demos.web.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demos.web.form.EmployeePageResponse;
import com.example.demos.web.form.EmployeesForm;
import com.example.demos.web.form.SearchForm;
import com.example.model.EmployeesModel;
import com.example.repository.EmployeesMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeesService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private EmployeesMapper employeesMapper;
	
	public EmployeePageResponse getEmployees(String status, String sortBy, String order, int size, int page, String sectionId) {
		int offset = size * page;
		List<EmployeesModel> empList = employeesMapper.findEmployees(status, sortBy, order, size, offset, sectionId);
		List<EmployeesForm> eFormList = new ArrayList<>();
		for (EmployeesModel emp : empList) {
			EmployeesForm empForm = modelMapper.map(emp, EmployeesForm.class);
			eFormList.add(empForm);
		}
		
		int totalCount = employeesMapper.countEmployees(status, sectionId);
		int totalPages = (int)Math.ceil((double) totalCount / size);
		
		
		return new EmployeePageResponse(eFormList, totalCount, totalPages);
	}
	
	public List<EmployeesForm> findAll() {
		List<EmployeesModel> empList = employeesMapper.findAll();
		List<EmployeesForm> eFormList = new ArrayList<EmployeesForm>();
		for (EmployeesModel emp: empList) {
			eFormList.add(modelMapper.map(emp, EmployeesForm.class));
		}
		return eFormList;
	}
	
	public List<EmployeesForm> getEmployeesBySection(String sectionId) {
		List<EmployeesModel> empList = employeesMapper.findEmployeesBySectionId(sectionId);
		log.info("受け取ったフォームデータ: {}", empList);
		
		List<EmployeesForm> eFormList = new ArrayList<EmployeesForm>();
		for (EmployeesModel emp: empList) {
			eFormList.add(modelMapper.map(emp, EmployeesForm.class));
		}
		
		log.info("受け取ったフォームデータ: {}", eFormList);
		return eFormList;
	}
	
	
	
	public void insertOne(EmployeesForm employeesForm) {
		EmployeesModel employeesModel = modelMapper.map(employeesForm, EmployeesModel.class);
		employeesMapper.insertOne(employeesModel);
	}

	public EmployeesForm findOne(String id) {
		EmployeesForm employeesForm = 
				modelMapper.map(employeesMapper.findOne(id), EmployeesForm.class);
		return employeesForm;
	}

	public void update(EmployeesForm employeesForm) {
		// 更新日の更新
		LocalDate now = LocalDate.now();
		Date date = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());
		employeesForm.setUpdatedAt(date);
		
		// マッピングして更新
		EmployeesModel employeesModel = modelMapper.map(employeesForm, EmployeesModel.class);
		employeesMapper.update(employeesModel);
	}

	public List<EmployeesForm> findMany(SearchForm searchForm) {
		List<EmployeesModel> empList = employeesMapper.findMany(searchForm);
		List<EmployeesForm> eFormList = new ArrayList<EmployeesForm>();
		for (EmployeesModel emp: empList) {
			eFormList.add(modelMapper.map(emp, EmployeesForm.class));
			log.info("受け取ったフォームデータ: {}", emp);
		}
		return eFormList;
	}

	public void delete(String id) {
		EmployeesModel employeesModel = employeesMapper.findOne(id);
		employeesModel.setDeleted(true);
		log.info("受け取ったフォームデータ: {}", employeesModel);
		employeesMapper.update(employeesModel);
	}
}
