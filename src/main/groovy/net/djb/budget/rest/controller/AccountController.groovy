package net.djb.budget.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.djb.budget.rest.service.AccountService;

@RestController
@RequestMapping("/accounts")
class AccountController {

	@Autowired AccountService accountService;

	@RequestMapping(method = RequestMethod.GET)
	Map<String,Long> list() {
		return null; 
	}

}
