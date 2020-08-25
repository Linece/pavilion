package com.pavilion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class BigScreenController{
	@RequestMapping("/view")
	public String index(){
		return "index";
	}
}
