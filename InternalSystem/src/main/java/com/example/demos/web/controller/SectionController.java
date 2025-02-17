package com.example.demos.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demos.web.form.SectionDisplayForm;
import com.example.demos.web.service.SectionDisplayService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/sections/list")
@Slf4j
public class SectionController {
	
	@Autowired
	private SectionDisplayService sectionDisplayService;

	@GetMapping("")
	public String showSections(
			@RequestParam(value = "sortColumn", required = false) String sortColumn,
			@RequestParam(value = "ascending", defaultValue = "true") boolean ascending,
			Model model) {
		
		List<SectionDisplayForm> dispList = sectionDisplayService.getSectionDisplayList();
		log.info("最終取得フォームデータ: {}", dispList);
		
		if (sortColumn != null) {
			dispList = sectionDisplayService.sortBy(sortColumn, dispList, ascending);
		}
		
		model.addAttribute("dispList", dispList);
		model.addAttribute("sortColumn", sortColumn);
		model.addAttribute("ascending", ascending);
		return "section-list";
	}
}
