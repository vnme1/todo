package com.co.kr.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

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
	      
	      //mav = bdSelectOneCall(todoVO, String.valueOf(bdSeq),request);
	      mav.setViewName("todo/todoList.html");
	      return mav;
	}
}
