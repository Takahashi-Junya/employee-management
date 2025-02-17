package com.example.demos.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demos.web.form.EmployeesForm;
import com.example.demos.web.form.GroupOrder;
import com.example.demos.web.service.EmployeesService;

@Controller
@RequestMapping("/employees/add")
public class AddEmployeesController {

	@Autowired
	private EmployeesService employeesService;
	
	@GetMapping("")
	public String add(@ModelAttribute EmployeesForm employeesForm) {
		return "add";
	}
	
	@PostMapping("")
	public String add(@Validated(GroupOrder.class) @ModelAttribute EmployeesForm employeesForm,
			BindingResult result) {
		if (result.hasErrors()) {
			return "add";
		}
		employeesService.insertOne(employeesForm);
		return "redirect:/employees/list";
	}
}
