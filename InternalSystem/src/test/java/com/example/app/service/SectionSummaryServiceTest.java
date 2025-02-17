package com.example.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demos.web.form.EmployeesForm;
import com.example.demos.web.form.SectionForm;
import com.example.demos.web.form.SectionSummaryForm;
import com.example.demos.web.service.EmployeesService;
import com.example.demos.web.service.SectionService;
import com.example.demos.web.service.SectionSummaryService;


@ExtendWith(MockitoExtension.class)
public class SectionSummaryServiceTest {

	@Mock
	private EmployeesService employeesService;
	
	@Mock
	private SectionService sectionService;
	
	@InjectMocks
	private SectionSummaryService sectionSummaryService;
	
	@Test
	void getSummaryFormList_ShouldReturnSummaryList() {
		
		SectionForm section1 = new SectionForm("1", "営業一課");
		SectionForm section2 = new SectionForm("2", "営業二課");
		SectionForm section3 = new SectionForm("3", "営業三課");
		SectionForm section4 = new SectionForm("4", "営業四課");
		
		when(sectionService.getAllSections()).thenReturn(Arrays.asList(section1, section2, section3, section4));
		
		when(employeesService.getEmployeesBySection(anyString()))
	    .thenAnswer(invocation -> {
	        String sectionId = invocation.getArgument(0);
	        List<EmployeesForm> employees = getEmployeesForSection(sectionId);
	        return employees != null ? employees : Collections.emptyList();
	    });

		
		List<SectionSummaryForm> result = sectionSummaryService.getSummaryFormList();
		
		assertEquals(4, result.size());

	    SectionSummaryForm section1Summary = result.get(0);
	    assertEquals("1", section1Summary.getSectionId());
	    assertEquals(120000, section1Summary.getTotalSales()); // 50000 + 70000
	    assertEquals(2, section1Summary.getTotalEmployees()); // 退社済みを除いた2人
	    assertEquals(60000, section1Summary.getAvgSalesPerPerson()); // 120000 / 2

	    SectionSummaryForm section2Summary = result.get(1);
	    assertEquals("2", section2Summary.getSectionId());
	    assertEquals(145000, section2Summary.getTotalSales()); // 80000 + 65000
	    assertEquals(2, section2Summary.getTotalEmployees());
	    assertEquals(72500, section2Summary.getAvgSalesPerPerson());

	    SectionSummaryForm section3Summary = result.get(2);
	    assertEquals("3", section3Summary.getSectionId());
	    assertEquals(170000, section3Summary.getTotalSales()); // 40000 + 55000 + 75000
	    assertEquals(3, section3Summary.getTotalEmployees());
	    assertEquals(56666, section3Summary.getAvgSalesPerPerson()); // 切り捨て考慮

	    SectionSummaryForm section4Summary = result.get(3);
	    assertEquals("4", section4Summary.getSectionId());
	    assertEquals(0, section4Summary.getTotalSales());
	    assertEquals(0, section4Summary.getTotalEmployees());
	}
	
	public static List<EmployeesForm> getEmployeesForSection(String sectionId) {
        List<EmployeesForm> employees = new ArrayList<>();

        switch (sectionId) {
            case "1": // 営業一課
                employees.add(new EmployeesForm("101", "山田 太郎", "東京", 50000, 10, new Date(), false, "1"));
                employees.add(new EmployeesForm("102", "佐藤 次郎", "大阪", 70000, 15, new Date(), false, "1"));
                employees.add(new EmployeesForm("103", "鈴木 三郎", "名古屋", 60000, 8, new Date(), true, "1")); // 退社済み
                break;
            case "2": // 営業二課
                employees.add(new EmployeesForm("201", "田中 一郎", "福岡", 80000, 20, new Date(), false, "2"));
                employees.add(new EmployeesForm("202", "高橋 二郎", "札幌", 65000, 12, new Date(), false, "2"));
                break;
            case "3": // 営業三課
                employees.add(new EmployeesForm("301", "伊藤 三郎", "広島", 40000, 7, new Date(), false, "3"));
                employees.add(new EmployeesForm("302", "渡辺 四郎", "仙台", 55000, 9, new Date(), false, "3"));
                employees.add(new EmployeesForm("303", "中村 五郎", "横浜", 30000, 6, new Date(), true, "3")); // 退社済み
                employees.add(new EmployeesForm("304", "小林 六郎", "新潟", 75000, 14, new Date(), false, "3"));
                break;
            case "4": // 営業四課（社員なし）
                break;
        }
        return employees;
    }
}


