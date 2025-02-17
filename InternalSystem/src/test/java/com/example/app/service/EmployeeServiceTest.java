package com.example.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.example.demos.web.form.EmployeesForm;
import com.example.demos.web.service.EmployeesService;
import com.example.model.EmployeesModel;
import com.example.repository.EmployeesMapper;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    
    @Mock
    private EmployeesMapper employeesMapper;
    
    @Mock
    private ModelMapper modelMapper;
    
    @InjectMocks
    private EmployeesService employeesService;
    
    // ✅ 共通のモックデータを作成
    private EmployeesModel createEmployee(String id, String name, String area, int sales, int customers, String sectionId) {
        return new EmployeesModel(id, name, area, sales, customers, new Date(), false, sectionId);
    }
    
    // ✅ modelMapper のモック設定
    private void setupModelMapperMock() {
        when(modelMapper.map(any(EmployeesModel.class), eq(EmployeesForm.class)))
        .thenAnswer(invocation -> {
            EmployeesModel source = invocation.getArgument(0);
            return new EmployeesForm(
                source.getId(),
                source.getName(),
                source.getArea(),
                source.getSales(),
                source.getCustomers(),
                source.getUpdatedAt(),
                source.isDeleted(),
                source.getSectionId()
            );
        });
    }
    
    @ParameterizedTest
    @CsvSource({
        "all, name, ASC, 10, 0, 0, 1",
        "active, name, ASC, 10, 1, 10, 2",
        "retired, name, DESC, 5, 2, 10, 3",
        "all, id, ASC, 20, 3, 60, 4",
        "active, area, DESC, 15, 4, 60, 1",
        "retired, sales, ASC, 25, 5, 125, 2"
    })
    void testGetEmployees_ShouldReturnEmpList(String status, String sortBy, String order, int size, int page, int expectedOffset, String sectionId) {
        // テスト対象メソッドが受け取ったページ数を適切にオフセットに変換し、
        // `employeesMapper.findEmployees` が適切な引数で呼ばれているかを検証

        employeesService.getEmployees(status, sortBy, order, size, page, sectionId);

        verify(employeesMapper, times(1)).findEmployees(status, sortBy, order, size, expectedOffset, sectionId);
    }

    
    @Test
    void testFindAll_ShouldReturnEmployeeList() {
        // モックデータの作成
        EmployeesModel emp1 = createEmployee("1", "Alice", "新宿", 23123, 21, "1");
        EmployeesModel emp2 = createEmployee("2", "Bob", "渋谷", 33423, 43, "2");

        // モックの動作を定義
        when(employeesMapper.findAll()).thenReturn(Arrays.asList(emp1, emp2));
        
        // modelMapper の設定
        setupModelMapperMock();

        // 実際のメソッドを呼び出し
        List<EmployeesForm> result = employeesService.findAll();
            
        // 検証
        validateEmployeeList(result);
        
        // findAll() が1回だけ呼ばれたことを検証
        verify(employeesMapper, times(1)).findAll();
        
        // modelMapper.map() が2回呼ばれたことを検証（2人分）
        verify(modelMapper, times(2)).map(any(EmployeesModel.class), eq(EmployeesForm.class));
    }

    @Test
    void testGetEmployeesBySection_ShouldReturnEmployeeList() {
        // モックデータの作成
        EmployeesModel emp1 = createEmployee("1", "Alice", "新宿", 23123, 21, "4");
        EmployeesModel emp2 = createEmployee("2", "Bob", "渋谷", 33423, 43, "4");

        // モックの動作を定義
        when(employeesMapper.findEmployeesBySectionId("4")).thenReturn(Arrays.asList(emp1, emp2));

        // modelMapper の設定
        setupModelMapperMock();

        // 実際のメソッドを呼び出し
        List<EmployeesForm> result = employeesService.getEmployeesBySection("4");
        
        // 検証
        validateEmployeeList(result);

        // findEmployeesBySectionId() が1回だけ呼ばれたことを検証
        verify(employeesMapper, times(1)).findEmployeesBySectionId("4");
        
        // modelMapper.map() が2回呼ばれたことを検証（2人分）
        verify(modelMapper, times(2)).map(any(EmployeesModel.class), eq(EmployeesForm.class));
    }
    
    @Test
    void testGetEmployeesBySection_NoData() {
        when(employeesMapper.findEmployeesBySectionId("999")) // 存在しないセクションID
            .thenReturn(Collections.emptyList()); // 空のリストを返すようにモック

        List<EmployeesForm> result = employeesService.getEmployeesBySection("999");

        assertNotNull(result); // `null` でないことを確認
        assertTrue(result.isEmpty()); // 空のリストであることを確認
    }

    // ✅ 検証ロジックを共通メソッド化
    private void validateEmployeeList(List<EmployeesForm> result) {
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("Bob", result.get(1).getName());
        assertEquals("新宿", result.get(0).getArea());
        assertEquals("渋谷", result.get(1).getArea());
        assertEquals(23123, result.get(0).getSales());
        assertEquals(33423, result.get(1).getSales());
    }
}

