package com.example.app.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.model.SectionModel;
import com.example.repository.SectionMapper;

// findAllだけだから（書き込みがないから）本番DBでテスト！
// 本番DBのときは@SpringBootTestが便利
// 書き込みがあるときは＠MyBatisTestでH2を使ってテストする！！
@SpringBootTest
public class SectionMapperTest {

	@Autowired
	private SectionMapper sectionMapper;
	
	@Test
	void findAll_Should_getSectionsList() {
		
		List<SectionModel> sectionsList = sectionMapper.findAll();
		
		assertNotNull(sectionsList);
		assertEquals(sectionsList.size(), 4);
		assertEquals(sectionsList.get(0).getSectionName(), "営業一課");
	}
}
