package com.example.demos.web.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demos.web.form.SectionForm;
import com.example.model.SectionModel;
import com.example.repository.SectionMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SectionService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private SectionMapper sectionMapper;
	
	public List<SectionForm> getAllSections() {
		List<SectionModel> sectionModels = sectionMapper.findAll();
		List<SectionForm> sectionForms = new ArrayList<SectionForm>();
		for (SectionModel sModel: sectionModels) {
			sectionForms.add(modelMapper.map(sModel, SectionForm.class));
		}
		log.info("セクションフォームリスト: {}", sectionForms);
		return sectionForms;
		
	}
}

