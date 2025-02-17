package com.example.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demos.web.form.SectionDisplayForm;
import com.example.demos.web.form.SectionForm;
import com.example.demos.web.form.SectionSummaryForm;
import com.example.demos.web.service.SectionDisplayService;
import com.example.demos.web.service.SectionService;
import com.example.demos.web.service.SectionSummaryService;

@ExtendWith(MockitoExtension.class)
public class SectionDisplayServiceTest {
	
	@Mock
	private SectionService sectionService;
	
	@Mock
	private SectionSummaryService sectionSummaryService;
	
	@InjectMocks
	private SectionDisplayService sectionDisplayService;

	@Test
	void combineProperties_ShouldReturnDisplayForm() {
		
		SectionForm section1 = new SectionForm("1", "営業一課");
		SectionSummaryForm sectionSummary = new SectionSummaryForm("1", 120000, 2, 60000);
		
		SectionDisplayForm result = sectionDisplayService.combineProperties(section1, sectionSummary);
		
		// Nullでないことを確認
	    assertNotNull(result);

	    // IDが正しく統合されていることを確認
	    assertEquals("1", result.getSectionId());

	    // 名称が正しく統合されていることを確認
	    assertEquals("営業一課", result.getSectionName());

	    // 売上合計が正しく統合されていることを確認
	    assertEquals(120000, result.getTotalSales());

	    // 従業員数が正しく統合されていることを確認
	    assertEquals(2, result.getTotalEmployees());

	    // 平均売上が正しく統合されていることを確認
	    assertEquals(60000, result.getAvgSalesPerPerson());
	}
	
	@Test
	void sortBy_test() {
		// SectionDisplayForm のリストを初期化
        List<SectionDisplayForm> sectionList = new ArrayList<>();

        // デモデータを追加
        sectionList.add(new SectionDisplayForm("01", "Section 0", 1825, 22, 609));
        sectionList.add(new SectionDisplayForm("02", "Section 1", 7284, 100, 228));
        sectionList.add(new SectionDisplayForm("03", "Section 2", 1614, 68, 552));
        sectionList.add(new SectionDisplayForm("04", "Section 3", 8419, 81, 650));
        sectionList.add(new SectionDisplayForm("05", "Section 4", 2836, 57, 554));
        sectionList.add(new SectionDisplayForm("06", "Section 5", 9356, 39, 478));
        sectionList.add(new SectionDisplayForm("07", "Section 6", 4625, 77, 312));
        sectionList.add(new SectionDisplayForm("08", "Section 7", 3241, 44, 589));
        sectionList.add(new SectionDisplayForm("09", "Section 8", 2876, 65, 421));
        sectionList.add(new SectionDisplayForm("10", "Section 9", 6782, 90, 375));
        
     // sectionId 昇順ソート
        List<SectionDisplayForm> sorted1List = sectionDisplayService.sortBy("sectionId", sectionList, true);
        assertEquals("01", sorted1List.get(0).getSectionId());
        assertEquals("02", sorted1List.get(1).getSectionId());
        assertEquals("10", sorted1List.get(9).getSectionId());

        // totalSales 降順ソート
        List<SectionDisplayForm> sorted2List = sectionDisplayService.sortBy("totalSales", sectionList, false);
        assertEquals(9356, sorted2List.get(0).getTotalSales());
        assertEquals(8419, sorted2List.get(1).getTotalSales());
        assertEquals(1614, sorted2List.get(9).getTotalSales());

        // totalEmployees 昇順ソート
        List<SectionDisplayForm> sorted3List = sectionDisplayService.sortBy("totalEmployees", sectionList, true);
        assertEquals(22, sorted3List.get(0).getTotalEmployees());
        assertEquals(39, sorted3List.get(1).getTotalEmployees());
        assertEquals(100, sorted3List.get(9).getTotalEmployees());

        // avgSalesPerPerson 降順ソート
        List<SectionDisplayForm> sorted4List = sectionDisplayService.sortBy("avgSalesPerPerson", sectionList, false);
        assertEquals(650, sorted4List.get(0).getAvgSalesPerPerson());
        assertEquals(609, sorted4List.get(1).getAvgSalesPerPerson());
        assertEquals(228, sorted4List.get(9).getAvgSalesPerPerson());

        // sectionName 昇順ソート
        List<SectionDisplayForm> sorted5List = sectionDisplayService.sortBy("sectionName", sectionList, true);
        assertEquals("Section 0", sorted5List.get(0).getSectionName());
        assertEquals("Section 1", sorted5List.get(1).getSectionName());
        assertEquals("Section 9", sorted5List.get(9).getSectionName());
	}
	
	@Test
	void getSectionDisplayList_ShouldReturnSectionDisplayList() {
		
		// SectionForm のデモデータ
        SectionForm section1 = new SectionForm("1", "営業一課");
        SectionForm section2 = new SectionForm("2", "営業二課");
        SectionForm section3 = new SectionForm("3", "営業三課");
        SectionForm section4 = new SectionForm("4", "営業四課");

        // SectionSummaryForm のデモデータ
        SectionSummaryForm summary1 = new SectionSummaryForm("1", 5000, 25, 200);
        SectionSummaryForm summary2 = new SectionSummaryForm("2", 7000, 30, 233);
        SectionSummaryForm summary3 = new SectionSummaryForm("3", 6000, 28, 214);
        SectionSummaryForm summary4 = new SectionSummaryForm("4", 8000, 35, 229);

        // モックの設定
        when(sectionService.getAllSections()).thenReturn(Arrays.asList(section1, section2, section3, section4));
        when(sectionSummaryService.getSummaryFormList()).thenReturn(Arrays.asList(summary1, summary2, summary3, summary4));
    
		List<SectionDisplayForm> result = sectionDisplayService.getSectionDisplayList();
		
		assertNotNull(result); // null でないこと
        assertEquals(4, result.size()); // データが 4 件返ってくること

        // 各データの検証
        assertEquals("1", result.get(0).getSectionId());
        assertEquals("営業一課", result.get(0).getSectionName());
        assertEquals(5000, result.get(0).getTotalSales());
        assertEquals(25, result.get(0).getTotalEmployees());
        assertEquals(200, result.get(0).getAvgSalesPerPerson());

        assertEquals("2", result.get(1).getSectionId());
        assertEquals("営業二課", result.get(1).getSectionName());
        assertEquals(7000, result.get(1).getTotalSales());
        assertEquals(30, result.get(1).getTotalEmployees());
        assertEquals(233, result.get(1).getAvgSalesPerPerson());

        assertEquals("3", result.get(2).getSectionId());
        assertEquals("営業三課", result.get(2).getSectionName());
        assertEquals(6000, result.get(2).getTotalSales());
        assertEquals(28, result.get(2).getTotalEmployees());
        assertEquals(214, result.get(2).getAvgSalesPerPerson());

        assertEquals("4", result.get(3).getSectionId());
        assertEquals("営業四課", result.get(3).getSectionName());
        assertEquals(8000, result.get(3).getTotalSales());
        assertEquals(35, result.get(3).getTotalEmployees());
        assertEquals(229, result.get(3).getAvgSalesPerPerson());
		
	}
}
