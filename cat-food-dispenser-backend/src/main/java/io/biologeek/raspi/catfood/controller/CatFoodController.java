package io.biologeek.raspi.catfood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.biologeek.raspi.catfood.services.CatFoodServices;

@RestController
@RequestMapping(path="/catfood")
public class CatFoodController {
	
	@Autowired
	private CatFoodServices service;
	
	@RequestMapping(path="/feed", method=RequestMethod.POST)
	public ResponseEntity<Void> feedTheCat(){
		
		
		try {
			this.service.feedTheCat();
		} catch (InterruptedException e) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
