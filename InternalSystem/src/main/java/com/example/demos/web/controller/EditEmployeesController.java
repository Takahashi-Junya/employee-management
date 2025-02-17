package com.example.demos.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demos.web.form.EmployeesForm;
import com.example.demos.web.form.GroupOrder;
import com.example.demos.web.service.EmployeesService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/employees/edit")
@Slf4j
public class EditEmployeesController {
	

	@Autowired
	private EmployeesService employeesService;
	
	
	// 全社員表示ページからの編集
	@GetMapping("/{id}")
	public String editForAllListPage(
	        HttpSession session,
	        @PathVariable("id") String id, 
	        Model model) {
	    String sectionName = (String) session.getAttribute("sectionName");
	    
	    EmployeesForm employeesForm = employeesService.findOne(id);
	    model.addAttribute("id", id);
	    model.addAttribute("employeesForm", employeesForm);
	    model.addAttribute("sectionName", sectionName);
	    model.addAttribute("returnUrl", "/employees/list"); // 戻るボタン用
	    model.addAttribute("editUrl", "/employees/edit/" + id); // 動的な編集URL

	    return "edit";
	}

	
	@PostMapping("/{id}")
	public String editForAllListPage(
			@PathVariable("id") String id, 
			@Validated(GroupOrder.class) @ModelAttribute EmployeesForm employeesForm,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("EmploueesForm", employeesForm);
			return "edit";
		}
		log.info("受け取ったフォームデータ: {}", employeesForm);
		
		employeesService.update(employeesForm);
		return "redirect:/employees/list";
		
	}
	
	
	// セクションごとの社員一覧ページからの編集
	@GetMapping("/section/{id}")
	public String editForListBySectionPage(
	        HttpSession session,
	        @PathVariable("id") String id, 
	        Model model) {
	    String sectionName = (String) session.getAttribute("sectionName");
	    
	    EmployeesForm employeesForm = employeesService.findOne(id);
	    model.addAttribute("id", id);
	    model.addAttribute("employeesForm", employeesForm);
	    model.addAttribute("sectionName", sectionName);
	    model.addAttribute("returnUrl", "/employees/list/section"); // 戻るボタン用
	    model.addAttribute("editUrl", "/employees/edit/section/" + id); // 動的な編集URL

	    return "edit";
	}

	
	@PostMapping("/section/{id}")
	public String editForListBySectionPage(
			@PathVariable("id") String id, 
			@Validated(GroupOrder.class) @ModelAttribute EmployeesForm employeesForm,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("EmploueesForm", employeesForm);
			return "edit";
		}
		log.info("受け取ったフォームデータ: {}", employeesForm);
		
		employeesService.update(employeesForm);
		return "redirect:/employees/list/section";
		
	}
}
