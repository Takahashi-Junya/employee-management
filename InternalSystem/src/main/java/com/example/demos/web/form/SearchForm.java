package com.example.demos.web.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SearchForm {

    private String id;   // ID で検索
    private String name; // 氏名 で検索
    private String area; // 担当地区 で検索
}
