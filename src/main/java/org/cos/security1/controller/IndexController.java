package org.cos.security1.controller;

import org.cos.security1.model.User;
import org.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping({"","/"})
	public String index() {
		//머스테치 기본폴더 src/main/resources/
		//뷰리졸버 설정: templates(prefix),mustache
		return "index"; // src/main/resources/templates/index.mustache
	}
	
	@GetMapping("/user")
	public @ResponseBody String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	//스프링 시큐리티가 해당 주소를 낚아채버림 -> SecurityConfig 파일 생성 후 작동안함
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	//회원가입 페이지 이동
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	//실제 회원가입 
	@PostMapping("/join")
	public String join(User user) {
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		
		userRepository.save(user); //회원가입 잘됨. 비밀번호:1234=> 시큐리티로 로그인 할 수 없음 암호화가 안되어있기 때문에  
		return "redirect:/loginForm";
	}

}
