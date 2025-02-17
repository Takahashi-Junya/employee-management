package com.example.demos.web.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demos.web.form.SectionDisplayForm;
import com.example.demos.web.form.SectionForm;
import com.example.demos.web.form.SectionSummaryForm;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SectionDisplayService {

	@Autowired
	private SectionService sectionService;
	
	@Autowired
	private SectionSummaryService sectionSummaryService;
	
	public SectionDisplayForm combineProperties(SectionForm sectionForm, SectionSummaryForm sectionSummaryForm) {
		
		SectionDisplayForm sectionDisplayForm = new SectionDisplayForm();
		
			sectionDisplayForm.setSectionId(sectionForm.getSectionId());
			sectionDisplayForm.setSectionName(sectionForm.getSectionName());
			sectionDisplayForm.setTotalSales(sectionSummaryForm.getTotalSales());
			sectionDisplayForm.setTotalEmployees(sectionSummaryForm.getTotalEmployees());
			sectionDisplayForm.setAvgSalesPerPerson(sectionSummaryForm.getAvgSalesPerPerson());
			log.info("結合結果: {}", sectionDisplayForm);
			return sectionDisplayForm;
	}
	
	public List<SectionDisplayForm> sortBy(String column, List<SectionDisplayForm> dispList, boolean ascending) {
		Comparator<SectionDisplayForm> comparator;
		
		switch (column) {
			case "sectionId":
				comparator = Comparator.comparing(SectionDisplayForm::getSectionId);
				break;
			case "sectionName":
				comparator = Comparator.comparing(SectionDisplayForm::getSectionName);
				break;
			case "totalSales":
				comparator = Comparator.comparing(SectionDisplayForm::getTotalSales);
				break;
			case "totalEmployees":
				comparator = Comparator.comparing(SectionDisplayForm::getTotalEmployees);
				break;
			case "avgSalesPerPerson":
				comparator = Comparator.comparing(SectionDisplayForm::getAvgSalesPerPerson);
				break;
			default:
				throw new IllegalArgumentException("Invalid sort column: " + column);
		}
		
		if (!ascending) {
			comparator = comparator.reversed();
		}
		
		dispList.sort(comparator);
		
		return dispList;
		
	}
	
	public List<SectionDisplayForm> getSectionDisplayList() {
		
		List<SectionDisplayForm> dispList = new ArrayList<>();
		
		List<SectionForm> secList = sectionService.getAllSections();
		log.info("ディスプレイメソッド内secList: {}", secList);
		List<SectionSummaryForm> summaryList = sectionSummaryService.getSummaryFormList();
		log.info("ディスプレイメソッド内summaryList: {}", summaryList);
		
		for (SectionForm sec: secList) {
			for (SectionSummaryForm summary: summaryList) {
				if (sec.getSectionId().equals(summary.getSectionId())) {
					dispList.add(combineProperties(sec, summary));
				}
			}
		}
		
		return dispList;
	}
}

