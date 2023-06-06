package com.co.kr.controller;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.co.kr.domain.TodoListDomain;
import com.co.kr.service.TodoService;
import com.co.kr.vo.TodoVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TodoListController {
	@Autowired
	private TodoService todoService;
	
	
	@PostMapping(value = "upload")
	   public ModelAndView bdUpload(TodoVo todoVO, MultipartHttpServletRequest request, HttpServletRequest httpReq) throws IOException, ParseException{
	      
	      ModelAndView mav = new ModelAndView();
	      int bdSeq = todoService.fileProcess(todoVO, request, httpReq);
	      todoVO.setContent(""); 
	      //todoVO.setTitle(""); 
	      
	      mav = bdSelectOneCall(todoVO, String.valueOf(bdSeq),request);
	      mav.setViewName("todo/todoList.html");
	      return mav;
	}
	public ModelAndView bdSelectOneCall(@ModelAttribute("todoVO") TodoVo todoVO, String bdSeq, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> map = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		
		map.put("bdSeq", Integer.parseInt(bdSeq));
		TodoListDomain todoListDomain =todoService.todoSelectOne(map);
		System.out.println("todoListDomain"+todoListDomain);
		
		mav.addObject("detail", todoListDomain);
		return mav;
	}
}
