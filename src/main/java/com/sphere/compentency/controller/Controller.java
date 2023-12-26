package com.sphere.compentency.controller;

import com.sphere.compentency.model.GetPassbook;
import com.sphere.compentency.model.Passbook;
import com.sphere.compentency.repository.passbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/compentencytool/v1")
public class Controller {

	@Autowired
	passbookRepository PassbookRepository;


	// 1. Insert passbook
	@PatchMapping(value = "/passbook", produces = "application/json")
	public Passbook Add_Passbook(@RequestBody Passbook passbook) {
		System.out.println(passbook);
	   return this.PassbookRepository.save(passbook);
	}
	// 2. Get Passbook
	@PostMapping("/passbook")
	public List<Passbook> GetPassbook(@RequestBody GetPassbook getpassbook){
		String TypeName=getpassbook.getRequest().getTypeName();
		List<String> userId=getpassbook.getRequest().getUserId();
		System.out.println("Userid = "+userId);
		List<Passbook> result = null;
		if (TypeName != null & userId != null) {
			System.out.println(userId);
			System.out.println(TypeName);
			result=  this.PassbookRepository.searchByTypeNameUserId(TypeName,userId);
		}
		else if (TypeName != null & userId == null) {
			result= this.PassbookRepository.searchByTypeName(TypeName);
		}


		return result;
	}
   
   
	@GetMapping("/competencyList")
	public List<Passbook> getAllCompetency(){
		return PassbookRepository.findAll();
	}
		

}	

