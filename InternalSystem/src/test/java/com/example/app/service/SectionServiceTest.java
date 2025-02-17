package com.example.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.example.demos.web.form.SectionForm;
import com.example.demos.web.service.SectionService;
import com.example.model.SectionModel;
import com.example.repository.SectionMapper;

@ExtendWith(MockitoExtension.class)
public class SectionServiceTest {

	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private SectionMapper sectionMapper;
	
	@InjectMocks
	private SectionService sectionService;
	
	@Test
	void getAllSection_ShouldReturnSectionList() {
		
		SectionModel section1 = new SectionModel("1", "営業一課");
		SectionModel section2 = new SectionModel("2", "営業二課");
		SectionModel section3 = new SectionModel("3", "営業三課");
		SectionModel section4 = new SectionModel("4", "営業四課");

		when(sectionMapper.findAll()).thenReturn(Arrays.asList(section1, section2, section3, section4));
		
		when(modelMapper.map(any(SectionModel.class), eq(SectionForm.class)))
		.thenAnswer(invocation -> {
			SectionModel source = invocation.getArgument(0);
			return new SectionForm(
					source.getSectionId(),
		            source.getSectionName()
					);
		});
		
		List<SectionForm> result = sectionService.getAllSections();
		
		assertNotNull(result);
		assertEquals(4, result.size());

		assertEquals("1", result.get(0).getSectionId());
		assertEquals("営業一課", result.get(0).getSectionName());

		assertEquals("2", result.get(1).getSectionId());
		assertEquals("営業二課", result.get(1).getSectionName());

		assertEquals("3", result.get(2).getSectionId());
		assertEquals("営業三課", result.get(2).getSectionName());

		assertEquals("4", result.get(3).getSectionId());
		assertEquals("営業四課", result.get(3).getSectionName());

	}
}
