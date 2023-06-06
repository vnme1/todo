package com.co.kr.controller;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.co.kr.service.TodoService;
import com.co.kr.service.UserService;
import com.co.kr.domain.LoginDomain;
import com.co.kr.domain.TodoListDomain;
import com.co.kr.util.CommonUtils;
import com.co.kr.util.Pagination;
import com.co.kr.vo.LoginVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j 
@RequestMapping(value = "/")
public class TodoController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TodoService todoService;
	
	@RequestMapping(value = "todo")
	public ModelAndView login(LoginVO loginDTO, HttpServletRequest request, HttpServletResponse response) throws IOException {
		//session 처리 
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		
		// 중복체크
		Map<String, String> map = new HashMap();
		map.put("mbId", loginDTO.getId());
		map.put("mbPw", loginDTO.getPw());
		
		int dupleCheck = userService.mbDuplicationCheck(map);
		LoginDomain loginDomain = userService.mbGetId(map);
		
		if(dupleCheck == 0) {  
			String alertText = "없는 아이디이거나 패스워드가 잘못되었습니다. 가입해주세요";
			String redirectPath = "/todo/signin";
			CommonUtils.redirect(alertText, redirectPath, response);
			return mav;
		}
		String IP = CommonUtils.getClientIP(request);
		
		//session 저장
		session.setAttribute("ip",IP);
		session.setAttribute("id", loginDomain.getMbId());
		session.setAttribute("mbLevel", loginDomain.getMbLevel());

		List<TodoListDomain> items = todoService.todoList();
		System.out.println("items ==> "+ items);
		mav.addObject("items", items);
		
		mav.setViewName("todo/todoList.html"); 
		return mav;
	};
	@RequestMapping(value = "bdList")
	public ModelAndView bdList() { 
		ModelAndView mav = new ModelAndView();
		List<TodoListDomain> items = todoService.todoList();
		System.out.println("items ==> "+ items);
		mav.addObject("items", items);
		mav.setViewName("todo/todoList.html");
		return mav; 
	}
	//회원가입창으로 이동
	@GetMapping("signin")
    public ModelAndView signIn() throws IOException {
		ModelAndView mav = new ModelAndView();
        mav.setViewName("signin/signin.html"); 
        return mav;
    }
	
	@PostMapping("create")
	public ModelAndView create(LoginVO loginVO, HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		ModelAndView mav = new ModelAndView();
		
		//session 처리 
		HttpSession session = request.getSession();
		
		//페이지 초기화
		String page = (String) session.getAttribute("page");
		if(page == null)page = "1";
		
		// 중복체크
		Map<String, String> map = new HashMap();
		map.put("mbId", loginVO.getId());
		map.put("mbPw", loginVO.getPw());
		map.put("mbPwx", loginVO.getPwx());
		
		
		// 중복체크
		int dupleCheck = userService.mbDuplicationCheck(map);
		//int dublepxCheck = userService.mbDublePxCheck(map);
		System.out.println(dupleCheck);

		if(dupleCheck > 0) { // 가입되있으면  
			String alertText = "비밀번호가 다릅니다";
			String redirectPath = "/todo";
			System.out.println(loginVO.getAdmin());
			if(loginVO.getAdmin() != null) {
				redirectPath = "/todo/mbList?page="+page;
			}
			CommonUtils.redirect(alertText, redirectPath, response);
		}
		else if(request.getParameter("mbPw") != request.getParameter("mbPwx")){
			String alertText = "비밀번호가 다릅니다";
			String redirectPath = "/todo";
			System.out.println(loginVO.getAdmin());
			if(loginVO.getAdmin() != null) {
				redirectPath = "/todo/mbList?page="+page;
			}
			CommonUtils.redirect(alertText, redirectPath, response);
		} 
		
		else {
			
			//현재아이피 추출
			String IP = CommonUtils.getClientIP(request);
			
			//전체 갯수
			int totalcount = userService.mbGetAll();
			
			//db insert 준비
			LoginDomain loginDomain = LoginDomain.builder()
					.mbId(loginVO.getId())
					.mbPw(loginVO.getPw())
					.mbPwx(loginVO.getPwx())
					.mbLevel((totalcount == 0) ? "3" : "2") // 최초가입자를 level 3 admin 부여
					.mbIp(IP)
					.mbUse("Y")
					.build();
			
       // 저장
			userService.mbCreate(loginDomain);
			
			if(loginVO.getAdmin() == null) { // 'admin'들어있을때는 alert 스킵이다
				// session 저장 
				session.setAttribute("ip",IP);
				session.setAttribute("id", loginDomain.getMbId());
				session.setAttribute("mbLevel", (totalcount == 0) ? "3" : "2");   // 최초가입자를 level 3 admin 부여
				mav.setViewName("redirect:/bdList");
			}else { // admin일때
				mav.setViewName("redirect:/mbList?page=1");
			}
		}
		
		return mav;

	};
	
	
	//로그아웃
	@RequestMapping("logout")
	public ModelAndView logout(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		session.invalidate(); // 전체삭제
		mav.setViewName("index.html");
		return mav;
	}
	
	@GetMapping("mbList")
	public ModelAndView mbList(HttpServletRequest request) {
			
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		String page = (String) session.getAttribute("page"); // session에 담고 있는 page 꺼냄
		if(page == null)page = "1"; // 없으면 1
		
		//클릭페이지 세션에 담아줌
		session.setAttribute("page", page);
		
		//페이지네이션
		mav = mbListCall(request);  //리스트만 가져오기
		
		mav.setViewName("admin/adminList.html");
		return mav; 
	};
	
	
	//페이징으로 리스트 가져오기 
    public ModelAndView mbListCall(HttpServletRequest request) { //클릭페이지 널이면 
		ModelAndView mav = new ModelAndView();
		//페이지네이션 쿼리 참고
    // SELECT * FROM jsp.member order by mb_update_at limit 1, 5; {offset}{limit}

		//전체 갯수
		int totalcount = userService.mbGetAll();
		int contentnum = 10; // 데이터 가져올 갯수 
		
		
		//데이터 유무 분기때 사용
		boolean itemsNotEmpty;
		
		if(totalcount > 0) { // 데이터 있을때
			
			// itemsNotEmpty true일때만, 리스트 & 페이징 보여주기
			itemsNotEmpty = true;
			//페이지 표현 데이터 가져오기
			Map<String,Object> pagination = Pagination.pagination(totalcount, request);
			
			Map map = new HashMap<String, Integer>();
	        map.put("offset",pagination.get("offset"));
	        map.put("contentnum",contentnum);
			
	        //페이지별 데이터 가져오기
			List<LoginDomain> loginDomain = userService.mbAllList(map);
			
			//모델객체 넣어주기
			mav.addObject("itemsNotEmpty", itemsNotEmpty);
			mav.addObject("items", loginDomain);
			mav.addObject("rowNUM", pagination.get("rowNUM"));
			mav.addObject("pageNum", pagination.get("pageNum"));
			mav.addObject("startpage", pagination.get("startpage"));
			mav.addObject("endpage", pagination.get("endpage"));
			
		}else {
			itemsNotEmpty = false;
		}
		
		return mav;
	};
	
}
