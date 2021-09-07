package com.java.Sprintboot_HelloWorld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class WelcomeController
{
	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome to springboot app development.";
	}
	
	@GetMapping("/{name}")
	public String welcome(@PathVariable String name) {
	    return "Hello and welcome to COMS 309: " + name;
	}
}
