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
import net.djb.budget.rest.data.schema.Account;

@RestController
@RequestMapping("/accounts")
class AccountController {

	@Autowired AccountService accountService;

	@RequestMapping(method = RequestMethod.GET)
	List<Account> list(@RequestParam(required = false, defaultValue = "false") boolean includeInactive) {
		if(includeInactive){
			return accountService.listAll();
		} else {
			return accountService.listActive();
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	void insert(@RequestBody Account account) {
		accountService.save(account);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	ResponseEntity<String> update(@PathVariable long id, @RequestBody Account account) {
		if(id == account.id ){
			accountService.save(account);
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Path ID must match ID in request body.", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	void update(@PathVariable long id) {
		accountService.delete(account);
	}

}
