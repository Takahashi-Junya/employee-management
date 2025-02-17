package com.example.model;

import java.lang.reflect.Field;
import java.util.Date;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Table("employees")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeesModel {

	private String id;            // VARCHAR(15)
    private String name;          // VARCHAR(50)
    private String area;          // VARCHAR(50)（担当地区）
    private int sales;            // INT（売上）
    private int customers;        // INT（保有顧客数）
    private Date updatedAt;       // DATE（更新日時）
    private boolean deleted;  // TINYINT(1)（論理削除フラグ）
    private String sectionId;     // VARCHAR(15)（セクションID）
    
    public Object get(String fieldName) {
    	try {
    		Field field = this.getClass().getDeclaredField(fieldName);
    		field.setAccessible(true);
    		return field.get(this);
    	} catch (NoSuchFieldException | IllegalAccessException e) {
    		throw new IllegalArgumentException("フィールドが存在しません: " + fieldName, e);
    	}
    }
}
