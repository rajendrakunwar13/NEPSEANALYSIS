package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupportResistanceController {
	@Autowired
	private UtilityClass utilityClass;
@GetMapping("/supportResistance")
public List<Double>getPrice(@RequestParam String scrip){
	return(utilityClass.supportResistanceIdentifier(scrip));
}
}
