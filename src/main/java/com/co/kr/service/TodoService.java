package com.co.kr.service;


import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.co.kr.domain.TodoListDomain;
import com.co.kr.vo.TodoVo;

public interface TodoService {
	
	// 전체 리스트 조회
	public List<TodoListDomain> todoList();
	
	public int fileProcess(TodoVo todoVO, MultipartHttpServletRequest request, HttpServletRequest httpReq);

	// 하나 삭제
	public void deleteToDoList(HashMap<String, Object> map);

	public TodoListDomain todoSelectOne(HashMap<String, Object> map);



}