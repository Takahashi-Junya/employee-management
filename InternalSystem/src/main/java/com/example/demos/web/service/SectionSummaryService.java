package com.example.demos.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demos.web.form.EmployeesForm;
import com.example.demos.web.form.SectionForm;
import com.example.demos.web.form.SectionSummaryForm;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SectionSummaryService {

	@Autowired
	private EmployeesService employeesService;
	
	@Autowired
	private SectionService sectionService;
	
	@SuppressWarnings("unchecked")
	public List<SectionSummaryForm> getSummaryFormList() {
		
		// セクション全件取得
		List<SectionForm> secList = sectionService.getAllSections();
		
		
		
		// 返り値用集計フォームリスト初期化
		List<SectionSummaryForm> summaryList = new ArrayList<SectionSummaryForm>();
		
		// 全セクションで処理を繰り返す
		for (SectionForm sec: secList) {
			log.info("受け取ったフォームデータ: {}", sec.getSectionId());
			
			// 現在のSectionでempListを作る
			// 退社済み社員は含まない
			List<EmployeesForm> empList = (List<EmployeesForm>) employeesService.getEmployeesBySection(sec.getSectionId()).stream()
					.filter(emp -> emp.isDeleted() == false)
					.collect(Collectors.toList());
			for (EmployeesForm emp: empList) {
				log.info("受け取ったフォームデータ: {}", emp);
			}
			
			// 集計フォーム
			SectionSummaryForm summaryForm = new SectionSummaryForm("", 0, 0, 0);
			
			
			// 集計用変数
			int totalSales = summaryForm.getTotalSales();
			int totalEmp = summaryForm.getTotalEmployees();
			
			// 現在のセクションのempListのすべての要素で処理を繰り返す
			for (EmployeesForm emp: empList) {
				totalSales += emp.getSales();
				totalEmp += 1;
			}
			// 集計結果格納
			summaryForm.setSectionId(sec.getSectionId());
			summaryForm.setTotalSales(totalSales);
			summaryForm.setTotalEmployees(totalEmp);
			if (totalEmp != 0) {
				summaryForm.setAvgSalesPerPerson(totalSales / totalEmp);
			} else {
				summaryForm.setAvgSalesPerPerson(0);
			}
			
			log.info("受け取ったフォームデータ: {}", summaryForm);
			
			// 返り値用リストに格納
			summaryList.add(summaryForm);
			
			log.info("受け取ったフォームデータ: {}", summaryList);
		}
		log.info("受け取ったフォームデータ: {}", summaryList);
		return summaryList;
	}
	
}

