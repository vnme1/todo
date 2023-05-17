package com.co.kr.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.co.kr.domain.TodoContentDomain;
import com.co.kr.domain.TodoListDomain;

@Mapper
public interface TodoMapper {

	//list
	public List<TodoListDomain> todoList();
	
	public void insertToDoList(TodoContentDomain todoContentDomain);
    public void updateToDoList(TodoContentDomain todoContentDomain);
    public void deleteToDoList(HashMap<String, Object> map);


}