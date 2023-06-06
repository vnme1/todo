package com.co.kr.service;


import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.co.kr.domain.TodoListDomain;
import com.co.kr.mapper.TodoMapper;
import com.co.kr.vo.TodoVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoMapper todoMapper;
	
	@Override
	public List<TodoListDomain> todoList() {
		// TODO Auto-generated method stub
		return todoMapper.todoList();
	}

	@Override
	public int fileProcess(TodoVo todoVO, MultipartHttpServletRequest request, HttpServletRequest httpReq) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteToDoList(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		todoMapper.deleteToDoList(map);
	}
	
	@Override
	public TodoListDomain todoSelectOne(HashMap<String, Object> map) {
		return todoMapper.boardSelectOne(map);
	}
	

}