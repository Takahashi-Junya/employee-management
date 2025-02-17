package com.example.demos.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demos.web.form.EmployeePageResponse;
import com.example.demos.web.form.EmployeesForm;
import com.example.demos.web.form.SearchForm;
import com.example.demos.web.service.EmployeesService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/employees/list")
@Slf4j
public class ShowEmployeesController {

	@Autowired
	private EmployeesService employeesService;
	
	// 全社員表示
	@GetMapping("")
    public String showEmployeeList(Model model) {
        model.addAttribute("searchForm", new SearchForm()); // 空の検索フォームを設定
        model.addAttribute("empList", employeesService.findAll()); // 初回は全件取得

        return "all-list"; // ThymeleafのHTMLテンプレート
    }

	// 検索結果表示
    @PostMapping("")
    public String searchEmployees(@ModelAttribute SearchForm searchForm, Model model) {
        log.info("検索フォームのデータ: {}", searchForm);

        List<EmployeesForm> empList = employeesService.findMany(searchForm);

        model.addAttribute("searchForm", searchForm);
        model.addAttribute("empList", empList);

        return "all-list";
    }
	

	
	// セクションごとの社員リスト
	@GetMapping("/section/{sectionId}/{sectionName}")
	public String showEmpListBySection(
			 	@PathVariable String sectionId,  // sectionId は必須
			 	@PathVariable String sectionName,
			 	HttpSession session,
	            @RequestParam(defaultValue = "all") String status,
	            @RequestParam(defaultValue = "name") String sortBy,
	            @RequestParam(defaultValue = "ASC") String order,
	            @RequestParam(defaultValue = "10") int size,  // ひとまずデフォルト 10
	            @RequestParam(defaultValue = "0") int page,
	            @ModelAttribute SearchForm searchForm, Model model) {
		
		session.setAttribute("sectionId", sectionId);
		session.setAttribute("sectionName", sectionName);
		
		EmployeePageResponse response = employeesService.getEmployees(status, sortBy, order, size, page, sectionId);
		model.addAttribute("empList", response.getEFormList());
		model.addAttribute("totalPages", response.getTotalPages());
	    model.addAttribute("totalCount", response.getTotalCount());
		model.addAttribute("sectionName", sectionName);
		return "list";
	}
	
	// ２回目以降の遷移（sectionIdとsectionNameをsessionで保持
	@GetMapping("/section")
	public String showAgainEmpListBySection(
			HttpSession session,
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String order,
            @RequestParam(defaultValue = "10") int size,  // ひとまずデフォルト 10
            @RequestParam(defaultValue = "0") int page,
            @ModelAttribute SearchForm searchForm, Model model
			) {
		
		String sectionId = (String) session.getAttribute("sectionId");
		String sectionName = (String) session.getAttribute("sectionName");
		
		EmployeePageResponse response = employeesService.getEmployees(status, sortBy, order, size, page, sectionId);
		model.addAttribute("empList", response.getEFormList());
		model.addAttribute("totalPages", response.getTotalPages());
	    model.addAttribute("totalCount", response.getTotalCount());
		model.addAttribute("sectionName", sectionName);
		
		return "list";
		
	}
	
	// 全社員表示画面から遷移される削除メソッド
	@GetMapping("/delete/{id}")
	public String deleteForAllListPage(@PathVariable String id) {
		employeesService.delete(id);
		return "redirect:/employees/list";
		
	}
	
	
	// セクションごとの社員一覧画面から遷移される削除メソッド
	@GetMapping("/section/delete/{id}")
	public String deleteForListBySectionPage(@PathVariable String id) {
		employeesService.delete(id);
		return "redirect:/employees/list/section";
	}
	
}
