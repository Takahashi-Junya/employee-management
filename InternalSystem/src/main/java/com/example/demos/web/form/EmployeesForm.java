package com.example.demos.web.form;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeesForm {

    @NotBlank(message="社員番号が入力されていません",
            groups = ValidGroup1.class)
    private String id;

    @NotBlank(message="氏名が入力されていません",
            groups = ValidGroup1.class)
    private String name;

    @NotBlank(message="担当地区が入力されていません",
            groups = ValidGroup1.class)
    private String area;

    @NotNull(message="売上が入力されていません",
            groups = ValidGroup1.class)
    @Min(value = 0, message="売上は0以上の数値を入力してください",
            groups = ValidGroup2.class)
    private int sales;

    @NotNull(message="保有顧客数が入力されていません",
            groups = ValidGroup1.class)
    @Min(value = 0, message="保有顧客数は0以上の数値を入力してください",
            groups = ValidGroup2.class)
    private int customers;

    @NotNull(message="更新日時が入力されていません",
            groups = ValidGroup1.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    private boolean deleted;

    @NotBlank(message="セクションIDが入力されていません",
            groups = ValidGroup1.class)
    private String sectionId;
}
