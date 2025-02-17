package com.example.app.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.model.EmployeesModel;
import com.example.repository.EmployeesMapper;

// Mybatisのテストをするときに必要
@SpringBootTest
public class EmployeesMapperTest {

    @Autowired
    private EmployeesMapper employeesMapper;
    
    

    @ParameterizedTest
    @CsvSource({
        "all, name, ASC, 10, 0, 1",
        "active, name, ASC, 10, 10, 2",
        "retired, name, DESC, 5, 0, 3",
        "all, id, ASC, 20, 5, 4",
        "active, area, DESC, 20, 10, 1",
        "retired, sales, ASC, 10, 20, 2"
    })
    @SuppressWarnings("unchecked")
    void testFindEmployees_ShouldReturnEmployeesList(String status, String sortBy, String order, int size, int offset, String sectionId) {
        
        int maxRecords = size + offset;  // `offset` 分を確保する
        List<EmployeesModel> allData = employeesMapper.findEmployees(status, sortBy, order, maxRecords, 0, sectionId);

        // ✅ ページネーション適用後のデータ取得
        List<EmployeesModel> pagedData = employeesMapper.findEmployees(status, sortBy, order, size, offset, sectionId);

        assertNotNull(pagedData);
        assertTrue(pagedData.size() <= size);

        // ✅ is_deleted フィルタリング
        for (EmployeesModel employee : pagedData) {
            if ("active".equals(status)) {
                assertFalse(employee.isDeleted());
            } else if ("retired".equals(status)) {
                assertTrue(employee.isDeleted());
            }
        }

        // ✅ ソートの確認
        for (int i = 1; i < pagedData.size(); i++) {
            EmployeesModel prev = pagedData.get(i - 1);
            EmployeesModel current = pagedData.get(i);

            if (sortBy.equals("is_deleted")) {
                continue;
            }

            Comparable<Object> prevValue = (Comparable<Object>) prev.get(sortBy);
            Comparable<Object> currentValue = (Comparable<Object>) current.get(sortBy);

            if ("ASC".equals(order)) {
                assertTrue(prevValue.compareTo(currentValue) <= 0);
            } else if ("DESC".equals(order)) {
                assertTrue(prevValue.compareTo(currentValue) >= 0);
            }
        }

        // ✅ ページネーションの確認
        if (offset < allData.size()) {
            EmployeesModel expectedFirst = allData.get(offset);
            EmployeesModel actualFirst = pagedData.get(0);
            assertEquals(expectedFirst.getId(), actualFirst.getId());
        } else {
            assertTrue(pagedData.isEmpty());
        }

        // ✅ セクションIDの確認
        for (EmployeesModel employee : pagedData) {
            assertEquals(sectionId, employee.getSectionId());
        }
    }



    /*
    @Test
    void testFindAll_ShouldReturnEmployeeList() {
       

        List<EmployeesModel> result = employeesMapper.findAll();

        assertNotNull(result);
        assertEquals(6, result.size());
     // 1件目のデータ（青木）
        assertEquals("1", result.get(0).getId());
        assertEquals("青木", result.get(0).getName());
        assertEquals("新宿", result.get(0).getArea());
        assertEquals(23123, result.get(0).getSales());
        assertEquals(21, result.get(0).getCustomers());
        assertEquals(false, result.get(0).isDeleted());
        assertEquals("1", result.get(0).getSectionId());

        // 2件目のデータ（赤城）
        assertEquals("2", result.get(1).getId());
        assertEquals("赤城", result.get(1).getName());
        assertEquals("渋谷", result.get(1).getArea());
        assertEquals(33423, result.get(1).getSales());
        assertEquals(43, result.get(1).getCustomers());
        assertEquals(false, result.get(1).isDeleted());
        assertEquals("2", result.get(1).getSectionId());

        // 3件目のデータ（青山）
        assertEquals("3", result.get(2).getId());
        assertEquals("青山", result.get(2).getName());
        assertEquals("池袋", result.get(2).getArea());
        assertEquals(3234, result.get(2).getSales());
        assertEquals(33, result.get(2).getCustomers());
        assertEquals(false, result.get(2).isDeleted());
        assertEquals("3", result.get(2).getSectionId());

        // 4件目のデータ（池田）
        assertEquals("4", result.get(3).getId());
        assertEquals("池田", result.get(3).getName());
        assertEquals("吉祥寺", result.get(3).getArea());
        assertEquals(3222, result.get(3).getSales());
        assertEquals(33, result.get(3).getCustomers());
        assertEquals(false, result.get(3).isDeleted());
        assertEquals("4", result.get(3).getSectionId());

        // 5件目のデータ（池山）
        assertEquals("5", result.get(4).getId());
        assertEquals("池山", result.get(4).getName());
        assertEquals("下北沢", result.get(4).getArea());
        assertEquals(34322, result.get(4).getSales());
        assertEquals(22, result.get(4).getCustomers());
        assertEquals(false, result.get(4).isDeleted());
        assertEquals("4", result.get(4).getSectionId());

        // 6件目のデータ（小山）
        assertEquals("6", result.get(5).getId());
        assertEquals("小山", result.get(5).getName());
        assertEquals("秋葉原", result.get(5).getArea());
        assertEquals(21332, result.get(5).getSales());
        assertEquals(11, result.get(5).getCustomers());
        assertEquals(false, result.get(5).isDeleted());
        assertEquals("3", result.get(5).getSectionId());
    }
    
    @Test
    void testFindEmployeesBySectionId_ShouldReturnEmployeeList() {
    	
    	List<EmployeesModel> result = employeesMapper.findEmployeesBySectionId("4");
    	
    	assertEquals("4", result.get(0).getId());
    	assertEquals("池田", result.get(0).getName());
    	assertEquals("吉祥寺", result.get(0).getArea());
    	assertEquals(3222, result.get(0).getSales());
    	assertEquals(33, result.get(0).getCustomers());
    	assertEquals(false, result.get(0).isDeleted());
    	assertEquals("4", result.get(0).getSectionId());

    	assertEquals("5", result.get(1).getId());
    	assertEquals("池山", result.get(1).getName());
    	assertEquals("下北沢", result.get(1).getArea());
    	assertEquals(34322, result.get(1).getSales());
    	assertEquals(22, result.get(1).getCustomers());
    	assertEquals(false, result.get(1).isDeleted());
    	assertEquals("4", result.get(1).getSectionId());


    }
    
    @Test
    void testFindEmployeesBySection_NoData() {
    	List<EmployeesModel> result = employeesMapper.findEmployeesBySectionId("9999999");
    	
    	assertNotNull(result); // `null` でないことを確認
        assertTrue(result.isEmpty()); // 空のリストであることを確認
    }
    
*/
}

