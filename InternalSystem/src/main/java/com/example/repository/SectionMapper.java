package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.model.SectionModel;

@Mapper
public interface SectionMapper {
	public List<SectionModel> findAll();
}
